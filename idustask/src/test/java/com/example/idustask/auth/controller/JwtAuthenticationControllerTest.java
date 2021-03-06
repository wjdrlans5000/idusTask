package com.example.idustask.auth.controller;

import com.example.idustask.common.BaseControllerTest;
import com.example.idustask.common.TestDescription;
import com.example.idustask.member.Member;
import com.example.idustask.member.MemberRepository;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        mockMvc.perform(post("/api/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(jwtRequest))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("token").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Login Success"))
                .andExpect(jsonPath("_links.self").exists())
                ;
    }


    @Test
//    @WithMockCustomUser(username = "tester@mail.com", roles = "USER")
    @TestDescription("로그아웃 테스트")
    public void logout() throws Exception{

        Member member = memberRepository.findByEmail("testUser0@mail.com")
                .orElseThrow(() -> new UsernameNotFoundException("testUser0@mail.com"));
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setEmail(member.getEmail());
        //member.getPassword() 는 인코딩 되어있어서 직접 비밀번호 셋팅
        jwtRequest.setPassword("Aa12345678!");

        ResultActions perform =  mockMvc.perform(post("/api/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(jwtRequest))
        )
                .andDo(print())
                .andExpect(status().isCreated());
        var responseBody = perform.andReturn().getResponse().getContentAsString();

        HashMap<String, String> hashmap = new HashMap<String, String>();

        JSONObject json = new JSONObject(String.valueOf(responseBody)); // 받아온 string을 json 으로로 변환

        Iterator i = json.keys(); // json key 요소읽어옴

        while(i.hasNext()){

            String k = i.next().toString(); // key 순차적으로 추출

            hashmap.put(k, json.getString(k)); // key, value를 map에 삽입
        }

        String loginToken = hashmap.get("token");

        mockMvc.perform(post("/api/logout")
                .header(HttpHeaders.AUTHORIZATION, loginToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("Logout Success"))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.login").exists())
        ;

        //로그아웃 후 회원상세조회 요청
        mockMvc.perform(get("/api/member/myinfo")
                .header(HttpHeaders.AUTHORIZATION, loginToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;

    }



}
