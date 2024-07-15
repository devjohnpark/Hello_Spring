package com.hello_spring.hello_spring.service;

import com.hello_spring.hello_spring.domain.Member;
import com.hello_spring.hello_spring.repository.MemberRepository;
import com.hello_spring.hello_spring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {
//    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final MemberRepository memberRepository;

    // 의존성을 외부에서 주입하도록 변경하여, MemberServiceTest에서 MemberService와 동일한 MemoryMemberRepository 객체로 테스트 가능
    // MemberServiceTest에서 MemoryMemberRepository 객체를 MemberService에 주입하여 동일한 객체로 테스트를 할 수 있다.
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 가입
    public Long join(Member member) {
        // 동일한 이름의 회원 중복 X
        validateDuplicatedMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicatedMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("Already existing member.");
                });
    }

    // 전체 회원 조회
    public List<Member> findMembers(String name) {
        return memberRepository.findAll();
    }

    // 회원 조회
    public Optional<Member> findOne(Long id) {
        return memberRepository.findById(id);
    }

}
