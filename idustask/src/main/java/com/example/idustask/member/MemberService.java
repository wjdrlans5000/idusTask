package com.example.idustask.member;

import com.example.idustask.member.dto.MemberRequestDto;
import com.example.idustask.member.dto.MemberResponseDto;
import com.example.idustask.member.errors.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encode;

    @Transactional
    public Member saveMember(MemberRequestDto memberDto){
        Member member =  memberRepository.save(Member.createMember(memberDto.getEmail(),
                                                                    encode.encode(memberDto.getPassword()),
                                                                    memberDto.getName(),
                                                                    memberDto.getNickName(),
                                                                    memberDto.getPhoneNumber(),
                                                                    memberDto.getGender()));
        return member;
    }

    public MemberResponseDto getMember(final Integer id) {
        final Member member = findById(id);
        return MemberResponseDto.from(member);
    }

    private Member findById(final int id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id + "is not found"));
    }

}
