package com.hello_spring.hello_spring.controller;

import com.hello_spring.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

// @Controller에는 @Component가 포함되어 있어서, annotation을 붙인 클래스의 싱글톤 객체인 Spring Bean이 Spring IoC에 등록된다.
// @Controller annotation은 스프링은 해당 클래스가 클라이언트로 부터 요청을 처리하는 Controller의 역할을 하도록 지정한다.
@Controller
public class MemberController {
    private final MemberService memberService;

    // 생성자에 @Autowired 를 사용하면 @Component가 붙여진 클래스의 객체 생성 시점에 스프링 컨테이너에서 해당 스프링 빈을 찾아서 주입한다.
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

/*
    // 필드 주입
    @Autowired private MemberService memberService; // 필드로 생성자 주입하여 자동 의존관계 설정
*/


/*
    // settter 주입
    private MemberService memberService;

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }
*/
}
