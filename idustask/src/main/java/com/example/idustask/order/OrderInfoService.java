package com.example.idustask.order;

import com.example.idustask.member.Member;
import com.example.idustask.order.dto.OrderInfoRequestDto;
import com.example.idustask.order.dto.OrderInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderInfoService {
    private final OrderInfoRepository orderInfoRepository;

    public OrderInfo createOrderInfo(OrderInfoRequestDto orderInfoRequestDto, Member member){
        final OrderInfo orderInfo = orderInfoRequestDto.toEntity(member);
        OrderInfo result = orderInfoRepository.save(orderInfo);

        List<OrderInfo> orders = new ArrayList<>();

        orders.add(result);
        member.setOrders(orders);
        result.setMember(member);
        return result;
    }
}
