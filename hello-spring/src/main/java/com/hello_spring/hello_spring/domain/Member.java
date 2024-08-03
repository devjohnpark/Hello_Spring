package com.hello_spring.hello_spring.domain;


import jakarta.persistence.*;

@Entity
public class Member {

    // Private ID (IDENTITY: DB에서 PK 자동 생성 )
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DB Column명 설정
//    @Column(name = "username")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
