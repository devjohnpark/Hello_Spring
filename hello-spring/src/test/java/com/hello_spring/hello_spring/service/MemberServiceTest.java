package com.hello_spring.hello_spring.service;

import com.hello_spring.hello_spring.domain.Member;
import com.hello_spring.hello_spring.repository.MemberRepository;
import com.hello_spring.hello_spring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

// static import
import static org.assertj.core.api.Assertions.*; // assertThat
import static org.junit.jupiter.api.Assertions.*; // assertThrow

class MemberServiceTest {
    /*
    MemberService 객체의 MemoryMemberRepository와 MemberServiceTest 객체의 MemoryMemberRepository 객체와 서로 달랐지만
    MemberService 생성자로 MemoryMemberRepository 객체를 외부에서 주입시켜서 동일한 MemoryMemberRepository 객체를 참졷하도록한다.
    */
//    MemberService memberService = new MemberService();
//    MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    // 각 테스트의 독립적인 수행을 위해서, 테스트 메서드 실행시 마다 객체 새로 생성
    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    // 각 테스트의 독립적인 수행을 위해서, 테스트 메서드 실행시 마다 수정되는 리포지토리의 멤버 변수 클리어
    @AfterEach
    public void afterEach() {
        // 리포지토리 객체의 멤버 변수 클리어
        memberRepository.clearStore();
    }

    @Test
    void join() {
        // given (주어진 상황)
        Member member = new Member();
        member.setName("jun");

        // when (검증 할 메서드를 실행했을때)
        Long resultId = memberService.join(member);

        // then (그러면 결과가 이렇게 나와야돼)
        Member findedMember = memberService.findOne(resultId).get();
        assertThat(resultId).isEqualTo(findedMember.getId()); //
    }

    /*
    위에는 `join`메서드의 정상적인 동작을 확인하는 테스트 코드이다.
    하지만 `validateDuplicatedMember` 메서드에서 예외가 정상적으로 발생되는지 확인하는 테스트 코드가 훨씬 더 중요하다.
    예상대로 특정 예외가 발생 않고 다른 예외가 발생했다면, 예외가 처리되지 못하고 프로그램이 비정상 종료되기 때문이다.
    따라서 예외를 일부러 발생시켜서, 예상대로 특정 예외가 발생하는지 테스트 코드를 짜야한다.
     */
    @Test
    void duplicatedMemberException() {
        // given (주어진 상황)
        Member member1 = new Member();
        Member member2 = new Member();

        member1.setName("jun");
        member2.setName("jun");

        // when (검증 할 메서드를 실행했을때)
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        // then (그러면 결과가 이렇게 나와야돼)
        assertThat(e.getMessage()).isEqualTo("Already existing member."); // 예외 처리 안발생하게

        /*
        먼저, 예외 처리를 하기 위해 try-catch문을 사용할 수 있다.
        하지만 예외를 처리하는 `catch` 블럭에서, `assertThat` 메서드를 사용하여 예상대로 예외가 발생하는지 확인하는것은, 예외를 처리하는 것이 아니라서 문맥에 맞지않는다.
        try {
            memberService.join(member2); // 에외 발생
            fail("An exception should occur."); // 예외 발생 안할시, 테스트 케이스 실패 처리되어 에러 발생
        } catch (IllegalStateException e) {
            // then (그러면 결과가 이렇게 나와야돼)
            assertThat(e.getMessage()).isEqualTo("Already existing member.");
        }
        */
    }

    @Test
    void findMembers()  {
    }

    @Test
    void findOne() {
    }
}