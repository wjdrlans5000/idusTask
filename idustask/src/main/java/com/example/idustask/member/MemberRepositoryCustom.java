package com.example.idustask.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MemberRepositoryCustom {
    Page<Member> findAllByMembers(Pageable pageable, String name, String email);
}
