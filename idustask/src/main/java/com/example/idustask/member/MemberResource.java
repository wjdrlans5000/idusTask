package com.example.idustask.member;

import com.example.idustask.member.dto.MemberResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class MemberResource extends EntityModel<MemberResponseDto> {
    public MemberResource(MemberResponseDto content, Link... links) {
        super(content, links);
        add(links);
    }
}
