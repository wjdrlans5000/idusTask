package com.example.idustask.member;

import com.example.idustask.member.dto.MemberRequestDto;
import com.example.idustask.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
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

    @GetMapping("/{id}")
    public ResponseEntity getDocument(@PathVariable Integer id) {

        MemberResponseDto memberResponseDto = service.getMember(id);
        MemberResource memberResource = new MemberResource(memberResponseDto);
//        memberResource.add(linkTo(methodOn(ApprovalController.class).approval(id,null,null,null,null)).withRel("approval"));
//        memberResource.add(new Link("/docs/index.html#resources-document-get").withRel("profile"));
        return ResponseEntity.ok(memberResource);
    }


}
