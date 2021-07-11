package com.example.idustask.order;

import com.example.idustask.order.dto.OrderInfoResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class OrderInfoResource extends EntityModel<OrderInfoResponseDto> {
    public OrderInfoResource(OrderInfoResponseDto content, Link... links) {
        super(content, links);
        add(linkTo(OrderInfoController.class).withSelfRel());
    }
}