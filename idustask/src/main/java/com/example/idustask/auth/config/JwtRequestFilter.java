package com.example.idustask.auth.config;

import com.example.idustask.auth.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/*
 * OncePerRequestFilter 를 상속받은 클래스로써 요청당 한번의 filter를 수행
 */
@ConfigurationProperties("exclude-filter")
@Getter
@Setter
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private InMemoryTokenStore tokenStore;

    private List<String> exclude_filter_url_list;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader =  httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.info("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.info("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if(jwtTokenUtil.validateToken(jwtToken)) {
                //InMemoryTokenStore에 저장된 토큰과 비교
                 Optional<String> store = Optional.ofNullable(tokenStore.getTokenStore(jwtToken));
                if(!store.isPresent()){
                    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
//                    throw new BadCredentialsException("UnAuthorized");
                }

                UserDetails userDetails = jwtUserDetailService.getUserDetails(username);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
    /*
    * exclude 시킬 url을 지정
    * */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return exclude_filter_url_list.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
}
