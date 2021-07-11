package com.example.idustask.order;

import com.example.idustask.auth.support.AuthUser;
import com.example.idustask.common.ErrorsResource;
import com.example.idustask.member.Member;
import com.example.idustask.member.MemberController;
import com.example.idustask.order.dto.OrderInfoRequestDto;
import com.example.idustask.order.dto.OrderInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
            @Valid @RequestBody final OrderInfoRequestDto orderInfoRequestDto,
            final Errors errors,
            @AuthUser final Member member) {

        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }

        OrderInfo orderInfo = orderInfoService.createOrderInfo(orderInfoRequestDto, member);

        OrderInfoResponseDto orderInfoResponseDto = OrderInfoResponseDto.from(orderInfo);

        OrderInfoResource orderInfoResource = new OrderInfoResource(orderInfoResponseDto);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(OrderInfoController.class);
        URI createUri = selfLinkBuilder.toUri();

        orderInfoResource.add(linkTo(methodOn(MemberController.class).getMembers(null,null,null,null,null,null)).withRel("get-members"));
        orderInfoResource.add(Link.of("/swagger-ui/index.html").withRel("profile"));
        return ResponseEntity.created(createUri).body(orderInfoResource);
    }
}
