package com.example.idustask.order.dto;

import com.example.idustask.member.Member;
import com.example.idustask.order.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderInfoRequestDto {

    private Integer memberId;

    @NotEmpty
    @Length(max = 100)
    private String productName;

    private LocalDateTime buyDate;

    public OrderInfo toEntity(Member member) {
        return OrderInfo.createOrderInfo(member, productName);
    }

    public static OrderInfoRequestDto from(final OrderInfo orderInfo) {
        return new OrderInfoRequestDto(
                orderInfo.getMember().getId(),
                orderInfo.getProductName(),
                orderInfo.getBuyDate()
        );
    }
}
