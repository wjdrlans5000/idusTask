package com.example.idustask.auth.service;

import com.example.idustask.auth.model.UserMember;
import com.example.idustask.member.Member;
import com.example.idustask.member.MemberRepository;
import com.example.idustask.member.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@ConfigurationProperties("admin")
@Getter
@Setter
public class JwtUserDetailsService implements UserDetailsService {

    private String admin;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    private ConcurrentHashMap<String,UserDetails> userDetails = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, UserDetails> getUserDetails() {
        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


    public Member authenticateByEmailAndPassword(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new BadCredentialsException("Password not matched");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
        if (email.equals(this.admin)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        }
        this.userDetails.put("userDetails",new UserMember(member, grantedAuthorities));

        return member;
    }

}
