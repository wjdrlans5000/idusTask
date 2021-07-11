package com.example.idustask;

import com.example.idustask.member.Member;
import com.example.idustask.member.MemberRepository;
import com.example.idustask.order.OrderInfo;
import com.example.idustask.order.OrderInfoRepository;
import com.example.idustask.order.dto.OrderInfoRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@EnableJpaAuditing
@SpringBootApplication
public class IdustaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdustaskApplication.class, args);
    }


    @Bean
    public ApplicationRunner InitialUserRunner(){
        return new ApplicationRunner() {

            @Autowired
            MemberRepository memberRepository;

            @Autowired
            OrderInfoRepository orderInfoRepository;

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
                    Member savedMember = memberRepository.save(member);

                    //주문생성 회원당 3개씩
                    for (int j = 0; j < 3; j++) {
                        OrderInfoRequestDto orderInfoRequestDto = new OrderInfoRequestDto(member.getId(),"productName"+i +"-"+j,null);
                        final OrderInfo orderInfo = orderInfoRequestDto.toEntity(member);
                        OrderInfo result = orderInfoRepository.save(orderInfo);

                        savedMember.updateOrders(result);
                    }

                }
            }
        };
    }
}

