package com.example.idustask.member;

import com.example.idustask.auth.controller.JwtAuthenticationController;
import com.example.idustask.auth.support.AuthUser;
import com.example.idustask.common.ErrorsResource;
import com.example.idustask.member.dto.MemberRequestDto;
import com.example.idustask.member.dto.MemberResponseDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
    @ApiOperation(value = "회원 가입" , notes = "회원기본정보를 입력하여 회원가입을 한다.")
    public ResponseEntity saveMember(@Valid @RequestBody MemberRequestDto memberDto, @ApiIgnore Errors errors) throws Exception {

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
    @ApiOperation(value = "회원 상세 조회" , notes = "현재 로그인한 회원의 상세 정보 및 주문목록을 조회한다.")
    public ResponseEntity getMember( @ApiIgnore @AuthUser final Member member) throws Exception {

        MemberResponseDto memberResponseDto = service.getMember(member.getId());
        MemberResource memberResource = new MemberResource(memberResponseDto, linkTo(methodOn(MemberController.class).getMember(member)).withSelfRel());
        memberResource.add(Link.of("/swagger-ui/index.html").withRel("profile"));
        return ResponseEntity.ok(memberResource);
    }

    @GetMapping
    @ApiOperation(value = "회원 목록 조회" , notes = "전체 회원 목록을 조회하며, 이름과 이메일로 특정 회원을 검색할수 있다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지번호", required = false, example = "0")
            ,@ApiImplicitParam(name = "size", value = "페이지크기", required = false, example = "0")
            ,@ApiImplicitParam(name = "sort", value = "정렬", required = false, example = "0")
            ,@ApiImplicitParam(name = "name", value = "이름", required = false)
            ,@ApiImplicitParam(name = "email", value = "이메일", required = false)
            ,@ApiImplicitParam(name = "last", value = "마지막 주문정보만 조회할지 여부", required = false)
    })
    public ResponseEntity getMembers(
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer size,
                                     @RequestParam(required = false) String sort,
                                     @ApiIgnore Pageable pageable,
                                     @ApiIgnore PagedResourcesAssembler<MemberResponseDto> assembler,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) String email,
                                     @RequestParam(required = false, defaultValue = "false") Boolean last,
                                     @ApiIgnore @AuthUser final Member member
    ) {

        Page<MemberResponseDto> dtoPage = service.getMembers(pageable,name,email,last);

        //Page 를 페이징처리가 된 Model 목록으로 변환해준다.
        PagedModel pagedResources = assembler.toModel(dtoPage, e -> new MemberResource(e, linkTo(MemberController.class).withSelfRel()));

        pagedResources.add(Link.of("/swagger-ui/index.html").withRel("profile"));
        return ResponseEntity.ok(pagedResources);
    }

}
