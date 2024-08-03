package com.hello_spring.hello_spring.service;

import com.hello_spring.hello_spring.domain.Member;
import com.hello_spring.hello_spring.repository.MemberRepository;
import com.hello_spring.hello_spring.repository.MemoryMemberRepository;
import jakarta.persistence.Column;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest // 스프링 부트 애플리케이션 통합 테스트 지정 (스프링 컨테이너와 테스트 함께 실행)
@Transactional // 각 테스트 케이스 마다 트랜잭션을 시작하고 테스트 완료 후에 롤백 수행하여 DB 변경 사항이 반영되지 않는다. 이를 통해, 각 테스트 케이스마다 독립적으로 수행되면 반복적으로 테스트 코드를 실행할 수 있다.
class MemberServiceIntegrationTest {

    // 다릌 클래스에서 테스트 클래스에 대한 의존성 없이, 테스트 코드각 독립적으로 실행하므로 간편하게 필드 주입
    @Autowired MemberService memberService;
    @Autowired  MemberRepository memberRepository; // 빈 구성 파일에서 생성하는 빈의 타입만 의존성 주입 가능

//    // 각 테스트의 독립적인 수행을 위해서, 테스트 메서드 실행시 마다 객체 새로 생성
//    @BeforeEach
//    public void beforeEach() {
//        memberRepository = new MemoryMemberRepository();
//        memberService = new MemberService(memberRepository);
//    }


//    // 각 테스트의 독립적인 수행을 위해서, 테스트 메서드 실행시 마다 수정되는 리포지토리의 멤버 변수 클리어
//    @AfterEach
//    public void afterEach() {
//        // 리포지토리 객체의 멤버 변수 클리어
//        memberRepository.clearStore();
//    }

    @Test
//    @Commit Test에서도 커밋하도록 설정
    void join() {
        // given (주어진 상황)
        Member member = new Member();
        member.setName("spring");

        // when (검증 할 메서드를 실행했을때)
        Long resultId = memberService.join(member);

        // then (그러면 결과가 이렇게 나와야돼)
        Member findedMember = memberService.findOne(resultId).get();
        assertThat(resultId).isEqualTo(findedMember.getId()); //
    }

    @Test
    void duplicatedMemberException() {
        // given (주어진 상황)
        Member member1 = new Member();
        Member member2 = new Member();

        member1.setName("john");
        member2.setName("john");

        memberService.join(member1);

        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }

    @Test
    void findMembers()  {
    }

    @Test
    void findOne() {
    }
}