package com.example.idustask.auth.config;

import com.example.idustask.member.Member;
import com.example.idustask.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        //prifix를 붙여 어떠한 방식으로 인코딩 된건지 알수있도록 해줌.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {

            @Autowired
            MemberRepository memberRepository;

            @Autowired
            PasswordEncoder encode;

            @Override
            @Transactional
            public void run(ApplicationArguments args) throws Exception {
                //테스트용 멤버 20명생성
                for (int i = 0; i < 20; i++) {
                    Member member = Member.builder()
//                            .id(i)
                            .email("testUser" + i  + "@mail.com")
                            .password(encode.encode("Aa12345678!"))
                            .name("네임")
                            .nickName("nickname")
                            .phoneNumber("010111111"+i)
                            .gender(Member.Gender.MALE)
                            .build();
                    memberRepository.save(member);
                }
            }
        };
    }

}
