package com.example.idustask.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Integer>{

    Optional<Member> findByEmail(String username);
}