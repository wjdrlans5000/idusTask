package com.example.idustask.member;

import com.example.idustask.member.dto.MemberRequestDto;
import com.example.idustask.member.dto.MemberResponseDto;
import com.example.idustask.member.errors.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public MemberResponseDto saveMember(final MemberRequestDto memberDto){
        Member member =  memberRepository.save(Member.createMember(memberDto.getEmail(),
                                                                    encode.encode(memberDto.getPassword()),
                                                                    memberDto.getName(),
                                                                    memberDto.getNickName(),
                                                                    memberDto.getPhoneNumber(),
                                                                    memberDto.getGender()));
        return MemberResponseDto.from(member);
    }

    public MemberResponseDto getMember(final Integer id) {
        final Member member = findById(id);
        return MemberResponseDto.from(member);
    }


    public Page<MemberResponseDto> getMembers(final Pageable pageable, String name, String email, boolean last) {

        Page<Member> page = memberRepository.findAllByMembers(pageable, name, email);

        Page<MemberResponseDto> page2 = page.map(member -> MemberResponseDto.from(member));
        if(last){
            page2.getContent().stream().forEach(memberResponseDto ->  memberResponseDto.setLastOrders(memberResponseDto.getOrders()));
        }
        return page2;
    }

    private Member findById(final int id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id + "is not found"));
    }
}

