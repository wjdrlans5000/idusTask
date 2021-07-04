package com.example.idustask.order.dto;

import com.example.idustask.member.Member;
import com.example.idustask.order.OrderInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderInfoResponseDto {

    private final Long id;

    //member id만
    private final Integer memberId;

    private final String productName;

    private final String buyDate;


    public static OrderInfoResponseDto from(final OrderInfo orderinfo) {
        return new OrderInfoResponseDto(
                orderinfo.getId(),
                orderinfo.getMember().getId(),
                orderinfo.getProductName(),
                orderinfo.getBuyDate()
        );
    }

}
