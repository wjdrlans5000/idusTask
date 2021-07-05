package com.example.idustask.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

//Repository명 + Custom >> 명명규칙 해당이름으로 정의하면 jpa에서 매핑해줌
public interface MemberRepositoryCustom {
    Page<Member> findAllByMembers(Pageable pageable);
}
