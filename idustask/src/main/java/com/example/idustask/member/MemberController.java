package com.example.idustask.member;

import com.example.idustask.auth.support.AuthUser;
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
    public ResponseEntity saveMember(@Valid @RequestBody MemberRequestDto memberDto, Errors errors) {

//        memberVaildator.validate(memberDto, errors);
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        Member member =  service.saveMember(memberDto);
        /*
         * Location URI 만들기
         * HATEOS가 제공하는 linkTo(), methodOn() 등 사용하여 uri 생성
         * */
        WebMvcLinkBuilder selfLinkBuilder =  linkTo(methodOn(MemberController.class).saveMember(null, errors));
        URI createUri = selfLinkBuilder.toUri();

        return ResponseEntity.created(createUri).body(member);
//        return "success, id : " + member.getId();
    }

    @GetMapping("/myinfo")
    public ResponseEntity getMember(@AuthUser final Member member) {

        MemberResponseDto memberResponseDto = service.getMember(member.getId());
        MemberResource memberResource = new MemberResource(memberResponseDto);
//        memberResource.add(linkTo(methodOn(ApprovalController.class).approval(id,null,null,null,null)).withRel("approval"));
//        memberResource.add(new Link("/docs/index.html#resources-document-get").withRel("profile"));
        return ResponseEntity.ok(memberResource);
    }

    @GetMapping
    public ResponseEntity getMembers(Pageable pageable,
                                     PagedResourcesAssembler<MemberResponseDto> assembler,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) String email,
                                     @AuthUser final Member member
    ) {

        Page<Member> page = service.getMembers(pageable,name,email);
        //page 제네릭타입 DocumentResponse으로 변환
        Page<MemberResponseDto> page2 = page.map(member2 -> MemberResponseDto.from(member2));
        //Page 를 페이징처리가 된 Model 목록으로 변환해준다.
        //e-> new EventResource(e) > 각 Event를 EventResource 로 변환 작업
        PagedModel pagedResources = assembler.toModel(page2, e -> new MemberResource(e));
        //create 링크정보 추가
        pagedResources.add(linkTo(MemberController.class).withRel("get-member"));
        //Profile 에 대한 링크 정보만 추가
//        pagedResources.add(new Link("/docs/index.html#resources-document-list").withRel("profile"));
        return ResponseEntity.ok(pagedResources);
    }

}
