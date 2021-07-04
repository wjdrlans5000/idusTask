package com.example.idustask.order.dto;

import com.example.idustask.member.Member;
import com.example.idustask.member.dto.MemberRequestDto;
import com.example.idustask.order.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderInfoRequestDto {
    private Integer memberId;

    private String productName;

    private String buyDate;

    public OrderInfo toEntity(Member member) {
        return OrderInfo.createOrderInfo(member, productName, buyDate);
    }

    public static OrderInfoRequestDto from(final OrderInfo orderInfo) {
        return new OrderInfoRequestDto(
                orderInfo.getMember().getId(),
                orderInfo.getProductName(),
                orderInfo.getBuyDate()
        );
    }
}
