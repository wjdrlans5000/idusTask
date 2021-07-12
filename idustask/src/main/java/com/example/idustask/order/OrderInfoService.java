package com.example.idustask.order;

import com.example.idustask.member.Member;
import com.example.idustask.member.MemberRepository;
import com.example.idustask.member.errors.MemberNotFoundException;
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

    private final MemberRepository memberRepository;


    @Transactional
    public OrderInfo createOrderInfo(OrderInfoRequestDto orderInfoRequestDto, Member member){

        final OrderInfo orderInfo = orderInfoRequestDto.toEntity(member);
        OrderInfo result = orderInfoRepository.save(orderInfo);
        // 회원에 order 정보 추가
        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new);

        findMember.updateOrders(result);

        return result;
    }
}
