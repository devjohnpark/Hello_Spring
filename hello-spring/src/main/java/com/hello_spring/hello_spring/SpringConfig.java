package com.hello_spring.hello_spring;

import com.hello_spring.hello_spring.repository.MemberRepository;
import com.hello_spring.hello_spring.repository.MemoryMemberRepository;
import com.hello_spring.hello_spring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 빈을 정의하고 관리하는 구성 클래스 정의
@Configuration
public class SpringConfig {

    // 스프링 빈에 등록
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
