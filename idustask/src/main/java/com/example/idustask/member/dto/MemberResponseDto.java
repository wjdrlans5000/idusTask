package com.example.idustask.member.dto;

import com.example.idustask.member.Member;
import com.example.idustask.order.OrderInfo;
import com.example.idustask.order.dto.OrderInfoResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class MemberResponseDto {

    private final Integer id;
    private final String email;
    private final String password;
    private final String name;
    private final String nickName;
    private final String phoneNumber;
    private final Member.Gender gender;
    private final List<OrderInfoResponseDto> orders;

    public void setLastOrders(List<OrderInfoResponseDto> orderinfos) {
        OrderInfoResponseDto orderInfo = orderinfos.get(orderinfos.size()-1);
        this.orders.clear();
        this.orders.add(orderInfo);
    }

    public static MemberResponseDto from(final Member member) {
        //엔터티의 경우 그대로 사용하게되면 안됨
        List<OrderInfoResponseDto> list = member.getOrders().stream()
                .map(orderinfo -> OrderInfoResponseDto.from(orderinfo)).collect(Collectors.toList());
        return new MemberResponseDto(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getName(),
                member.getNickName(),
                member.getPhoneNumber(),
                member.getGender(),
                list
        );
    }

}
