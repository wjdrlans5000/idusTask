package com.example.idustask.member;


import com.example.idustask.order.OrderInfo;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private String nickName;
    @Column(unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    //1대 N 연관관계
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderInfo> orders = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setOrders(List<OrderInfo> orders) {
        this.orders = orders;
    }

    public void updateOrders(final OrderInfo entity) {
        this.orders.add(entity);
    }

    public Member(String email, String password, String name, String nickName, String phoneNumber, Gender gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    @Builder
    public Member(Integer id, String email, String password, String name, String nickName, String phoneNumber, Gender gender) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public static Member createMember(String email, String password, String name, String nickName, String phoneNumber, Gender gender){
        return new Member(email,password,name,nickName,phoneNumber,gender);
    }


    //성별
    public enum Gender {
        // 승인
        MALE,
        // 거절
        FEMALE,
        ;
    }
}
