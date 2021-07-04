package com.example.idustask.member;

import com.example.idustask.member.dto.MemberResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class MemberResource extends EntityModel<MemberResponseDto> {
    public MemberResource(MemberResponseDto content, Link... links) {
        super(content, links);
        // withSelfRel(): 리소스에 대한 링크를 type-safe 한 method로 제공한다.
        add(linkTo(MemberController.class).slash(content.getId()).withSelfRel());
    }
}
