package com.example.idustask.config;

import com.example.idustask.auth.model.UserMember;
import com.example.idustask.member.Member;
import org.assertj.core.util.Lists;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Member member = Member.builder()
                .id(9999)
                .email(customUser.username())
                .password("Aa12345678!")
                .name("네임")
                .nickName("nickname")
                .phoneNumber("01012345678")
                .gender(Member.Gender.MALE)
                .build();
        UserMember principal = new UserMember(member,
                Lists.newArrayList(new SimpleGrantedAuthority(customUser.roles()))
        );
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
