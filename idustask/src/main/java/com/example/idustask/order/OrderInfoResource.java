package com.example.idustask.order;

import com.example.idustask.member.MemberController;
import com.example.idustask.member.dto.MemberResponseDto;
import com.example.idustask.order.dto.OrderInfoResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class OrderInfoResource extends EntityModel<OrderInfoResponseDto> {
    public OrderInfoResource(OrderInfoResponseDto content, Link... links) {
        super(content, links);
        // withSelfRel(): 리소스에 대한 링크를 type-safe 한 method로 제공한다.
        add(linkTo(OrderInfoController.class).slash(content.getId()).withSelfRel());
    }
}