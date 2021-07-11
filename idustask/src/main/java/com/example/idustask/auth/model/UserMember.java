package com.example.idustask.auth.model;

import com.example.idustask.member.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

//member 객체를 감싸는 Adapter 클래스 사용
@Getter
public class UserMember extends User {
    private final Member member;

    public UserMember(Member member, Collection<? extends GrantedAuthority> authorities) {
        //User 클래스의 생성자를 호출하여 username, password, role을 세팅한다.
        super(member.getEmail(), member.getPassword(), authorities);
        this.member = member;
    }
}
