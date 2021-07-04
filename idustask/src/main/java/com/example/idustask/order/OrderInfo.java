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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_id")
    private Member member;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String buyDate;

    public void setMember(Member member) {
        this.member = member;
    }

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
