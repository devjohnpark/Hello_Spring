package com.hello_spring.hello_spring.repository;

import com.hello_spring.hello_spring.domain.Member;

import java.util.*;

public class  MemoryMemberRepository implements MemberRepository {
    /*
    회원 정보를 저장하고 관리하기 위한 저장소 역할이기 때문에, 모든 인스턴스들이 동일한 저장소를 공유하도록 static 변수로 선언
    여러 인스턴스가 공유하는 클래스 변수(static variable)는 인스턴스 변수(instance variable)보다 동시성 문제가 발생할 가능성이 더 높다.
    인스턴스 변수는 특정 객체를 공유하는 스레드 사이에서만 동시성 문제가 발생한다.
    클래스 변수는 특정 클래스를 사용하는 스레드 사이에서 동시성 문제가 발생한다. 즉, 클래스 변수에 접근하는 모든 스레드에서 동시성 문제가 발생한다.
    */

    /* 변수 갱신시에 동시성 문제가 발생 가능 */
    // HashMap 대신 ConcurrentHashMap의 사용 필요
    public static Map<Long, Member> store = new HashMap<>(); // 회원 저장소

    // long 대신 AtomicLong 사용 필요
    private static long sequence = 0L; // 연속적인  인덱스

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional findByName(String name) {
        return store.values().stream().
                filter(member -> member.getName().equals(name))
                .findAny(); // 조건에 일치하는 값 없을시, Optional에 null 값이 wrapping되서 반환된다.
    }

    // 실무에서는 리스트를 많이 쓴다. 리스트는 루프 돌리기도 편하기 때문.
    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
