package com.hello_spring.hello_spring.service;

import com.hello_spring.hello_spring.domain.Member;
import com.hello_spring.hello_spring.repository.MemberRepository;
import com.hello_spring.hello_spring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {
    private final MemberRepository memberRepository = new MemoryMemberRepository();

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
                throw new IllegalStateException("Already Existing Member.");
             });
    }

    // 전체 회원 조회
    private List<Member> findMembers(String name) {
        return memberRepository.findAll();
    }

     // 회원 조회
    private Optional<Member> findOne(Long id) {
        return memberRepository.findById(id);
    }

}
