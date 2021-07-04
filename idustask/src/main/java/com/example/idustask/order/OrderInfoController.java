package com.example.idustask.order;

import com.example.idustask.auth.model.UserMember;
import com.example.idustask.auth.support.AuthUser;
import com.example.idustask.member.Member;
import com.example.idustask.order.dto.OrderInfoRequestDto;
import com.example.idustask.order.dto.OrderInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/order")
@RequiredArgsConstructor
public class OrderInfoController {

    private final OrderInfoService orderInfoService;

    @PostMapping
    public ResponseEntity createOrderInfo(
            @RequestBody final OrderInfoRequestDto orderInfoRequestDto,
            final BindingResult result,
            @AuthUser final Member member) {

        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result);
        }

        OrderInfo orderInfo = orderInfoService.createOrderInfo(orderInfoRequestDto, member);

        OrderInfoResponseDto orderInfoResponseDto = OrderInfoResponseDto.from(orderInfo);

        OrderInfoResource orderInfoResource = new OrderInfoResource(orderInfoResponseDto);

        /*
         * Location URI 만들기
         * HATEOS가 제공하는 linkTo(), methodOn() 등 사용하여 uri 생성
         * */
        WebMvcLinkBuilder selfLinkBuilder = linkTo(OrderInfoController.class).slash(orderInfoResponseDto.getId());
        URI createUri = selfLinkBuilder.toUri();


        return ResponseEntity.created(createUri).body(orderInfoResource);
    }
}
