package com.hello_spring.hello_spring.repository;

import com.hello_spring.hello_spring.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

// JPA를 쓸려면, 항상 트랜잭션 내에서 DB 변경이 일어나야된다. -> Service Layer에서 @Transactional 지정
public class JpaMemberRepository implements MemberRepository {

    // JPA는 EntityManager 인터페이스를 제공
    private EntityManager em;

    // 스프링 부트에서 DB 커넥션 정보와 함께 EntityManager 구현체 자동 생성 후 주입 (EntityManager 내에 DataSource 포함)
    // EntityManager가 DB 통신 처리함 (구현체 뜯어보기)
    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    // JPA는 PK로 데이터를 가져오는 것이 아니라면 JPQL문 필요함 ->  Spring Data JPA에서 JPA를 감싸서 JPQL문 작성할 필요없게 구현함
    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        // 테이블 대상이 아닌 객체(Entity)를 대상으로 JPQL(Java Persistence Query Language)을 날린다. (결국 JPQL은 SQL로 변환되어 테이블 대상으로 DB 서버에 SQL 날리게 됨)
        // m은 alias
        // 기존에는 column (id, name)을 모두 객체에 매핑시켯어야 됬지만, 바로 객체로 가겨올수 있다.
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}
