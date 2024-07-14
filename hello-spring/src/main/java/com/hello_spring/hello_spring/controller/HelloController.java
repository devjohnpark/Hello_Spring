package com.hello_spring.hello_spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// Controller은 클라이언트의 요청을 받는 클래스이며, 스프링 컨테이너가 생성한 객체가 역할을 수행한다.
@Controller
public class HelloController {
    /*
    @GetMapping: HTTP 메서드인 GET 요청이 들어오면, 아래의 메서드와 맵핑되어서 호출된다.
    hello: 클라이언트가 요청할 리소스인 파일명이다.
    */
    @GetMapping("hello")
    public String hello(Model model) { // Model은 클라이언트에게 응답할 데이터를 가진 클래스이며, 스프링 컨테이너가  Model 객체를 생성하여 인자로 넣어준다.
        /* attributeName의 값과 동일한 hello.html의 data 변수에 attributeValue의 값을 넣게 된다. */
        model.addAttribute("data", "Hello!"); // model에 속성값 할당

        /*
        http 메서드 요청과 일치하는 리소스 요청이 있을때, 스프링 컨테이너가 /resource/에서 리소스를 찾는다.
        Template Engine을 사용해서 dynamic html content를 제공할려면, /src/main/resources/templates에 dynamic html 파일을 저장해야한다.
        Controller에서 리턴값으로 문자열을 반환하면, ViewResolver 클래스가 화면을 찾고 이후에 렌더링 후에 리턴하는 역할을 한다.
        스프링 부트의 템플릿 엔진은 기본적으로 ViewName 맵핑이 된다. 따라서 ViewResolver 클래스의 객체는 /resource/templates/{ViewName}.html 해당 리소스를 찾는다..
        반환값이 "hello" 문자열이므로 찾는 리소스는 /resource/templates/hello.html을 가리킨다.
        이렇게 View를 찾은후 thymeleaf의 template 엔진이 렌더링을하고 클라에게 응답을 준다.
        */
        return "hello";
    }

//    @GetMapping("hello-static")
//    public String hello() {
//        System.out.println("hello-static");
//        return "hello-static.html";
//    }

    // 클라언트의 요청 처리
    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) { // 클라이언트가 url에 "name"이란 parameter을 넣어서 요청

        // 모델 데이터를 생성
        model.addAttribute("name", name); // 모델에 파라매터 담아서 뷰에 쓴다.

        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody // http message의 body에 데이터 저장해서 응답
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        hello.setNo(10);
        return hello; // JSON 형식으로 반환
    }

    static class Hello {
        private String name; // { "name", "jun" }
        private int no; // { "no", 10  }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        // getter, setter를 JavaBean 규약(표준 방식 )이라고 한다. (멤버 변수는 private으로 외부에서 직접적인 접근은 막고, getter와 setter 메서드를 통하여 접근하는 것 )
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
