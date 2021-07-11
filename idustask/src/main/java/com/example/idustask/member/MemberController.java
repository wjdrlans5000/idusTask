package com.example.idustask.member;

import com.example.idustask.auth.controller.JwtAuthenticationController;
import com.example.idustask.auth.support.AuthUser;
import com.example.idustask.common.ErrorsResource;
import com.example.idustask.member.dto.MemberRequestDto;
import com.example.idustask.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService service;


    //[1].회원가입 : 리퀘스트 바디로 사용자 정보를 받아서 저장한다.
    @PostMapping("/signup")
    public ResponseEntity saveMember(@Valid @RequestBody MemberRequestDto memberDto, Errors errors) throws Exception {

        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }
        MemberResponseDto memberResponseDto =  service.saveMember(memberDto);

        WebMvcLinkBuilder selfLinkBuilder =  linkTo(methodOn(MemberController.class).saveMember(memberDto, errors));
        URI createUri = selfLinkBuilder.toUri();

        MemberResource memberResource = new MemberResource(memberResponseDto, linkTo(methodOn(MemberController.class).saveMember(memberDto, errors)).withSelfRel());
        memberResource.add(linkTo(methodOn(JwtAuthenticationController.class).createAuthenticationToken(null)).withRel("login"));
        memberResource.add(Link.of("/swagger-ui/index.html").withRel("profile"));
        return ResponseEntity.created(createUri).body(memberResource);
    }

    @GetMapping("/myinfo")
    public ResponseEntity getMember(@AuthUser final Member member) throws Exception {

        MemberResponseDto memberResponseDto = service.getMember(member.getId());
        MemberResource memberResource = new MemberResource(memberResponseDto, linkTo(methodOn(MemberController.class).getMember(member)).withSelfRel());
        memberResource.add(Link.of("/swagger-ui/index.html").withRel("profile"));
        return ResponseEntity.ok(memberResource);
    }

    @GetMapping
    public ResponseEntity getMembers(Pageable pageable,
                                     PagedResourcesAssembler<MemberResponseDto> assembler,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) String email,
                                     @RequestParam(required = false, defaultValue = "false") Boolean last,
                                     @AuthUser final Member member
    ) {

        Page<MemberResponseDto> page = service.getMembers(pageable,name,email,last);

        //Page 를 페이징처리가 된 Model 목록으로 변환해준다.
        PagedModel pagedResources = assembler.toModel(page, e -> new MemberResource(e, linkTo(MemberController.class).withSelfRel()));

        pagedResources.add(Link.of("/swagger-ui/index.html").withRel("profile"));
        return ResponseEntity.ok(pagedResources);
    }

}
