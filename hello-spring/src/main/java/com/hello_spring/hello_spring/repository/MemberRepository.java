package com.hello_spring.hello_spring.repository;

import com.hello_spring.hello_spring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional findMemberById(Long id);
    Optional findMemberByName(String name);
    List<Member> findAll();
}
