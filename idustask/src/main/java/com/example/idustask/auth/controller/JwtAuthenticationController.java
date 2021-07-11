package com.example.idustask.auth.controller;

import com.example.idustask.auth.config.InMemoryTokenStore;
import com.example.idustask.auth.config.JwtTokenUtil;
import com.example.idustask.auth.service.JwtUserDetailsService;
import com.example.idustask.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailService;

    @Autowired
    private InMemoryTokenStore tokenStore;

    @PostMapping("/api/signin")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        final Member member = userDetailService.authenticateByEmailAndPassword
                (authenticationRequest.getEmail(), authenticationRequest.getPassword());
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",member.getId());
        claims.put("userEmail",member.getEmail());
        final String token = jwtTokenUtil.generateToken(member.getEmail(),claims);
        // tokenStore에 토큰 저장
        tokenStore.setTokenStore(token);

        WebMvcLinkBuilder selfLinkBuilder =  linkTo(methodOn(JwtAuthenticationController.class).createAuthenticationToken(null));
        URI createUri = selfLinkBuilder.toUri();

        EntityModel<JwtResponse> entityModel = EntityModel.of(new JwtResponse(token,"Login Success"));
        entityModel.add(linkTo(methodOn(JwtAuthenticationController.class).createAuthenticationToken(authenticationRequest)).withSelfRel());

        return ResponseEntity.created(createUri).body(entityModel);
    }


    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) throws Exception {
        //로그아웃시 InMemoryTokenStore에 토큰을 제거한다.
        tokenStore.removeAccessToken();

        EntityModel<LogoutResponse> entityModel = EntityModel.of(new LogoutResponse("Logout Success"));
        entityModel.add(linkTo(methodOn(JwtAuthenticationController.class).logout(null)).withSelfRel());
        entityModel.add(linkTo(methodOn(JwtAuthenticationController.class).createAuthenticationToken(null)).withRel("login"));

         return ResponseEntity.ok(entityModel);
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
    private String message;

}

@Data
@AllArgsConstructor
class LogoutResponse {
    private String message;
}