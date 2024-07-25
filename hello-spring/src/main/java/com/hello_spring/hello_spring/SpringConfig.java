package com.hello_spring.hello_spring;

import com.hello_spring.hello_spring.repository.JdbcMemberRepository;
import com.hello_spring.hello_spring.repository.JdbcTemplateMemberRepository;
import com.hello_spring.hello_spring.repository.MemberRepository;
import com.hello_spring.hello_spring.repository.MemoryMemberRepository;
import com.hello_spring.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

// 빈을 정의하는 구성 클래스
@Configuration
public class SpringConfig {

    private final DataSource dataSource;

    // 스프링이 DataSource 인터페이스를 구현한 HikariCP 등의 커넥션 풀 구현체를 사용하여 빈을 생성 및 주입
    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 스프링 빈에 등록
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
        return new JdbcTemplateMemberRepository(dataSource);
    }
}
