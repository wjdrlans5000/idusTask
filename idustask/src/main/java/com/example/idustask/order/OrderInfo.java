package com.example.idustask.order;

import com.example.idustask.domain.BaseEntity;
import com.example.idustask.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@Table(name = "orderinfo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderInfo extends BaseEntity {

    //하나의 주문정보는 하나의 멤버와 연관관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_id")
    private Member member;

    private String productName;

    private String buyDate;

    public OrderInfo(Member member, String productName, String buyDate) {
        this.member = member;
        this.productName = productName;
        this.buyDate = buyDate;
    }

    //주문 생성시 멤버에 주문정보 UPDATE
    public static OrderInfo createOrderInfo(Member member, String productName, String buyDate) {
        return new OrderInfo(member, productName, buyDate);
    }
}
