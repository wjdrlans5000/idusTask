package com.example.idustask.member;

import com.example.idustask.auth.controller.JwtAuthenticationController;
import com.example.idustask.auth.support.AuthUser;
import com.example.idustask.member.dto.MemberRequestDto;
import com.example.idustask.member.dto.MemberResponseDto;
import com.example.idustask.order.OrderInfo;
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
import java.util.List;
import java.util.stream.Collectors;

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

//        memberVaildator.validate(memberDto, errors);
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        MemberResponseDto memberResponseDto =  service.saveMember(memberDto);
        /*
         * Location URI 만들기
         * HATEOS가 제공하는 linkTo(), methodOn() 등 사용하여 uri 생성
         * */
        WebMvcLinkBuilder selfLinkBuilder =  linkTo(methodOn(MemberController.class).saveMember(null, errors));
        URI createUri = selfLinkBuilder.toUri();
        MemberResource memberResource = new MemberResource(memberResponseDto);
        memberResource.add(linkTo(methodOn(JwtAuthenticationController.class).createAuthenticationToken(null)).withRel("login"));

        return ResponseEntity.created(createUri).body(memberResource);
    }

    @GetMapping("/myinfo")
    public ResponseEntity getMember(@AuthUser final Member member) {

        MemberResponseDto memberResponseDto = service.getMember(member.getId());
        MemberResource memberResource = new MemberResource(memberResponseDto);
//        memberResource.add(new Link("/docs/index.html#resources-document-get").withRel("profile"));
        return ResponseEntity.ok(memberResource);
    }

    @GetMapping
    public ResponseEntity getMembers(Pageable pageable,
                                     PagedResourcesAssembler<MemberResponseDto> assembler,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) String email,
                                     @RequestParam(required = false) Boolean last,
                                     @AuthUser final Member member
    ) {

        Page<Member> page = service.getMembers(pageable,name,email);
        List<OrderInfo> orders;
//        if(last){
//            MemberResponseDto result = page.map(member2 ->  MemberResponseDto.from(member2).setLastOrders(member2.getOrders())).collect(Collectors.toList());
//        }
        //MemberResponseDto.from(member2).setLastOrders(member2.getOrders())
        Page<MemberResponseDto> page2 = page.map(member2 -> MemberResponseDto.from(member2));
        if(last){
            page2.getContent().stream().forEach(memberResponseDto ->  memberResponseDto.setLastOrders(memberResponseDto.getOrders()));
        }
        //Page 를 페이징처리가 된 Model 목록으로 변환해준다.
        PagedModel pagedResources = assembler.toModel(page2, e -> new MemberResource(e));

        //Profile 에 대한 링크 정보만 추가
//        pagedResources.add(new Link("/docs/index.html#resources-document-list").withRel("profile"));
        return ResponseEntity.ok(pagedResources);
    }

}
