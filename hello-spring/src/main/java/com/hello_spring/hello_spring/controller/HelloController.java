package com.hello_spring.hello_spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Controller은 클라이언트의 요청을 받는 클래스이며, 스프링 컨테이너가 생성한 객체가 역할을 수행한다.
@Controller
public class HelloController {
    /*
    @GetMapping: HTTP 메서드인 GET 요청이 들어오면, 아래의 메서드와 맵핑되어서 호출된다.
    hello: 클라이언트가 요청할 리소스의 저장되어있는 디렉터리 경로
    */
    @GetMapping("hello")
    public String hello(Model model) { // Model은 클라이언트에게 응답할 클래스이며, 스프링 컨테이너가  Model 객체를 생성하여 인자로 넣어준다.
        /* attributeName 값("data")과 동일한 hello.html의 data 변수에 attributeVale("Hello!") 값을 넣게 된다. */
        model.addAttribute("data", "Hello!");

        /*
        루트(/) 디렉터리가 아닌 경우, 스프링 컨테이너가 /resource/templates에서 리소스를 찾는다.
        Controller에서 리턴값으로 문자열을 반환하면, ViewResolver 클래스가 화면을 찾아서 렌더링 후에 리턴하는 역할을 한다.
        스프링 부트의 템플릿 엔진은 기본적으로 ViewName 맵핑이 된다. 따라서 ViewResolver 클래스의 객체는 /resource/templates/{ViewName}.html 해당 리소스를 찾는다..
        반환값이 "hello" 문자열이므로 찾는 리소스는 /resource/templates/hello.html을 가리킨다.
        이렇게 View를 찾은후 thymeleaf의 template 엔진이 렌더링을하고 클라에게 응답을 준다.
        */
        return "hello";
    }
}
