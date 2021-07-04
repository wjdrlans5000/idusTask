package com.example.idustask.auth.controller;

import com.example.idustask.auth.config.InMemoryTokenStore;
import com.example.idustask.auth.config.JwtTokenUtil;
import com.example.idustask.auth.service.JwtUserDetailsService;
import com.example.idustask.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailService;

    //[2].로그인할 이메일과 패스워드를 사용하여 인증요청을 함
    @PostMapping("/api/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        final Member member = userDetailService.authenticateByEmailAndPassword
                (authenticationRequest.getEmail(), authenticationRequest.getPassword());
        //[4].사용자 인증에 성공하면 토큰을 생성한다.
        //클레임도 파라미터로 넘겨주자
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",member.getId());
        claims.put("userEmail",member.getEmail());
        claims.put("userName", member.getName());
        final String token = jwtTokenUtil.generateToken(member.getEmail(),claims);
        //싱글톤으로 token을 저장할 ConcurrentHashMap 객체 생성
        InMemoryTokenStore inMemoryTokenStore = new InMemoryTokenStore(token);

        return ResponseEntity.ok(new JwtResponse(token));
    }

}

@Data
class JwtRequest {

    private String email;
    private String password;

}

@Data
@AllArgsConstructor
class JwtResponse {

    private String token;

}