package com.example.idustask.order;

import com.example.idustask.domain.BaseEntity;
import com.example.idustask.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;


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

    @CreatedDate
    private LocalDateTime buyDate;

    public OrderInfo(Member member, String productName) {
        this.member = member;
        this.productName = productName;
    }

    //주문 생성시 멤버에 주문정보 UPDATE
    public static OrderInfo createOrderInfo(Member member, String productName) {
        return new OrderInfo(member, productName);
    }
}
