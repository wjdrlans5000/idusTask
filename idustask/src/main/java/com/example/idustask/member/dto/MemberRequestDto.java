package com.example.idustask.member.dto;

import com.example.idustask.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;


@Getter
@AllArgsConstructor
public class MemberRequestDto {
    @NotNull
    @Length(max = 100)
    @Email
    private String email;

    @NotEmpty
    //최소 10자리에 대문자 1자리 소문자 1자리 숫자 1자리 특수문자 1자리 이상 포함
    @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=?])[A-Za-z\\d~!@#$%^&*()+|=?]{10,}")
    private String password;

    @NotEmpty
    @Length(max = 20)
    @Pattern(regexp="^[a-zA-Z가-힣]*$")
    private String name;

    @NotEmpty
    @Length(max = 20)
    @Pattern(regexp="^[a-z]*$")
    private String nickName;

    @NotEmpty
    @Length(max = 20)
    @PositiveOrZero // 양수와 0만 허용
    private String phoneNumber;

    private Member.Gender gender;

}
