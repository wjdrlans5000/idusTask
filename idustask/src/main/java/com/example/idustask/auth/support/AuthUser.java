package com.example.idustask.auth.support;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
/*
@AuthenticationPrincipal은 로그인한 사용자의 정보를 파라메터로 받고 싶을때 기존에는 다음과 같이 Principal 객체로 받아서 사용한다.
UserDetailsService에서 Return한 객체 를 파라메터로 직접 받아 사용할 수 있다.
따라서 member 객체를 감싸는 Adapter 클래스인 UserMember 객체를 받아서 사용한다.
SpEL 을 사용해서 Adapter클래스가 아닌 member 객체를 직접 가져온다.
Member객체를 직접 참조하여 사용하려면 getMember() 코드가 게속해서 중복되기 때문.
*/
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : member")
public @interface AuthUser {

}
