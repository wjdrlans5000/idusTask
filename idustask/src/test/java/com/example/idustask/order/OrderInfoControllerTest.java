package com.example.idustask.order;

import com.example.idustask.auth.model.UserMember;
import com.example.idustask.common.BaseControllerTest;
import com.example.idustask.common.TestDescription;
import com.example.idustask.config.WithMockCustomUser;
import com.example.idustask.member.Member;
import com.example.idustask.member.MemberRepository;
import com.example.idustask.member.dto.MemberRequestDto;
import com.example.idustask.order.dto.OrderInfoRequestDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class OrderInfoControllerTest extends BaseControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @WithMockCustomUser(username = "tester@mail.com", roles = "USER")
    @TestDescription("로그인한 사용자로 주문생성 테스트")
    public void createOrderInfo() throws Exception{

        Date date = new Date(System.currentTimeMillis());
        OrderInfo orderInfo = new OrderInfo(getLoginMember(), "떡", date.toString());

        OrderInfoRequestDto orderInfoRequestDto = OrderInfoRequestDto.from(orderInfo);
        Member member = memberRepository.save(getLoginMember());

        mockMvc.perform(post("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(orderInfoRequestDto))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("productName").exists())
                .andExpect(jsonPath("buyDate").exists())
                ;

        //회원 주문 목록 조회
        mockMvc.perform(get("/api/member/{id}",21)
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
        ;
    }

    private Member getLoginMember(){
        // 로그인한 사용자의 id를 userId로 설정
        // email은 writerEmail로 설정
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserMember userMember = (UserMember) principal;
        Member userDetails = userMember.getMember();
        return userDetails;
    }
}