package com.example.idustask.auth.controller;

import com.example.idustask.auth.model.UserMember;
import com.example.idustask.common.BaseControllerTest;
import com.example.idustask.common.TestDescription;
import com.example.idustask.config.WithMockCustomUser;
import com.example.idustask.member.Member;
import com.example.idustask.member.MemberRepository;
import com.example.idustask.member.dto.MemberRequestDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class JwtAuthenticationControllerTest extends BaseControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder encode;


    @Test
    @TestDescription("로그인 테스트")
    public void createAuthenticationToken() throws Exception{

        Member member = memberRepository.findByEmail("testUser0@mail.com")
                .orElseThrow(() -> new UsernameNotFoundException("testUser0@mail.com"));
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setEmail(member.getEmail());
        //member.getPassword() 는 인코딩 되어있어서 직접 비밀번호 셋팅
        jwtRequest.setPassword("Aa12345678!");

        mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(jwtRequest))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").exists())
                ;
    }

}
