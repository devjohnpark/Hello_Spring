package com.hello_spring.hello_spring;

import com.hello_spring.hello_spring.repository.MemberRepository;
import com.hello_spring.hello_spring.service.MemberService;

// Singletion 빈 생성 원리 개념 이해 예시 코드
// startup-time에 스프링에서 CGLIB(Compile-time Code Generation Library)을 사용하여 프록시 패턴의 빈 구성 클래스의 서브 클래스를 작성
public class SpringConfigByCGLIB extends SpringConfig {
    private MemberService memberServiceBean;
    private MemberRepository memberRepositoryBean;

    @Override
    public MemberService memberService() {
        if (memberServiceBean == null) {
            memberServiceBean = super.memberService();
        }
        return memberServiceBean;
    }

    @Override
    public MemberRepository memberRepository() {
        if (memberRepositoryBean == null) {
            memberRepositoryBean = super.memberRepository();
        }
        return memberRepositoryBean;
    }
}
