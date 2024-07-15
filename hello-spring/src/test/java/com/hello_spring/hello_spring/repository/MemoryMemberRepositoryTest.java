package com.hello_spring.hello_spring.repository;

import com.hello_spring.hello_spring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

// 테스트 클래스를 여러개 테스트할 경우, 패키지 단위로 실행시킨다.
class MemoryMemberRepositoryTest {
    MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    /*
    테스트 클래스 실행시, 테스트 메서드의 실행 순서 보장은 안되기 때문에, 테스트 메서드에서 접근할 객체의 데이터가 예상치 못한 값이 도출될수 있다.
    따라서 테스트 메서드 수행이 완료될때마다 테스트 메서드에서 접근할 객체의 데이터를 비어주어, 각 테스트 메서드가 올바르게 동작하도록한다.
    */
    @AfterEach
    public void afterEach() {
        // 리포지토리 객체의 멤버 변수 클리어
        memberRepository.clearStore();
    }

    // 리포지토리의 메서드와 동일한 테스트 메서드 정의
    @Test
    public void saveMember() {
        Member member = new Member();
        member.setName("spring");

        memberRepository.save(member);

//        // Optional 타입일 경우, get 메서드로 강제로 Optional 타입을 추출하는 것은 좋지  않지만, 테스트 코드에서는 허용된다.
//        Member result = (Member) memberRepository.findMemberById(member.getId()).get();
//
//                System.out.println("result: " + (result == member));
//
//        //  위의 코드처럼 출력문은 서버 실행시에 매번 볼수 없으므로 assert으로 에러를 처리해야한다.
//        // 실무에서는 빌드 툴에서 assert에서 테스트 케이스가 통과 되지못하면 다음 단계로 못넘어가도록 막는다.
//        Assertions.assertThat(result).isEqualTo(member);

        // 아니면, 아래처럼 람다 표현식을 사용할수 있다.
        Optional<Member> result = memberRepository.findById(member.getId());

        result.ifPresent(foundMember -> {
            Assertions.assertThat(foundMember).isEqualTo(member);
        });
    }

    @Test
    public void findMemberByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        memberRepository.save(member2);

        Member result = (Member) memberRepository.findByName(member1.getName()).get();
        Assertions.assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAllMember() {
        Member member1 = new Member();
        member1.setName("spring1");
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        memberRepository.save(member2);

        List<Member> result = memberRepository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(2);
    }
}
