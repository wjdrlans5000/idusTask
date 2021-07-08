package com.example.idustask.member;


import com.example.idustask.auth.model.UserMember;
import com.example.idustask.common.BaseControllerTest;
import com.example.idustask.common.TestDescription;
import com.example.idustask.config.WithMockCustomUser;
import com.example.idustask.member.dto.MemberRequestDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class MemberControllerTest extends BaseControllerTest {


    @Autowired
    MemberRepository memberRepository;

    @Test
    @TestDescription("회원가입 테스트")
    public void saveMember() throws Exception {

        Member member = Member.builder()
                .email("gimun@mail.com")
                .password("Aa12345678!")
                .name("네임")
                .nickName("nickname")
                .phoneNumber("01012345678")
                .gender(Member.Gender.MALE)
                .build();

        MemberRequestDto memberRequestDto = MemberRequestDto.from(member);


        mockMvc.perform(post("/api/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(memberRequestDto))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.login").exists())
//                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @Test
    @TestDescription("회원가입 badRequest 테스트")
    public void saveMember_badRequest() throws Exception {

        Member member = Member.builder()
                //이
                .email("wjdrlans4000@naver.com")
                //잘못된 패스워드
                .password("Aa12345678")
                //잘못된 네임
                .name("네임1")
                //잘못된 닉네임
                .nickName("nickName")
                //잘못된 폰번호
                .phoneNumber("01012345678a")
                .gender(Member.Gender.MALE)
                .build();

        MemberRequestDto memberRequestDto = MemberRequestDto.from(member);


        mockMvc.perform(post("/api/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(memberRequestDto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @WithMockCustomUser(username = "tester@mail.com", roles = "USER")
    @TestDescription("로그인한 회원 상세정보 조회")
    public void getMember() throws Exception {

        Member member = memberRepository.save(getLoginMember());

        //When & Then
        mockMvc.perform(get("/api/member/myinfo")
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
        ;
    }

    @Test
    @WithMockCustomUser(username = "tester@mail.com", roles = "USER")
    @TestDescription(" 회원 목록 조회")
    public void getMembers() throws Exception {

        //회원의 마지막 주문목록만 조회
        mockMvc.perform(get("/api/member")
                .param("page","0") //패이지는 0부터 시작
                .param("size","10")
                .param("sort","id,DESC")
                //이름, 이메일로 검색
                .param("name","네임")
                .param("email","testUser0@mail.com")
                .param("last","true")
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").exists())
        ;

        //회원의 모든 주문목록 조회
        mockMvc.perform(get("/api/member")
                .param("page","0") //패이지는 0부터 시작
                .param("size","10")
                .param("sort","id,DESC")
                //이름, 이메일로 검색
                .param("name","네임")
                .param("email","testUser0@mail.com")
                .param("last","false")
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").exists())
        ;
    }


    private Member getLoginMember(){
        // 로그인한 사용자의 id를 userId로 설정
        // email은 writerEmail로 설정
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserMember userMember = (UserMember) principal;
        Member userDetails = userMember.getMember();
        return userDetails;
    }
}