package com.example.idustask.member;

import com.example.idustask.member.dto.MemberRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import javax.validation.Valid;
import javax.validation.Validation;
import java.util.regex.Pattern;

@Component
public class MemberVaildator {

    public void validate(MemberRequestDto memberRequestDto, Errors errors){


    }
}
