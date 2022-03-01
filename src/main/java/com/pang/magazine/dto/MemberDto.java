package com.pang.magazine.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
public class MemberDto {


    @Getter
    @AllArgsConstructor
    @Builder
    public static class reqRegisterDto{
        @NotEmpty(message ="형식에 맞춰 입력")
        @Pattern(regexp = "^[a-zA-Z0-9]*$")
        @Size(min = 3, message = "닉네임 길이가 맞지 않습니다.")
        private String username;

        @NotEmpty(message ="형식에 맞춰 입력")
        @Size(min = 4, message ="4자 이상 입력가능")
        private String password;

        @NotEmpty(message ="형식에 맞춰 입력")
        @Size(min = 4, message ="4자 이상 입력가능")
        private String check_password;

        @NotEmpty(message ="형식에 맞춰 입력")
        private String name;
    }


    @Getter
    @AllArgsConstructor
    @Builder
    public static class reqLoginDto{
        @NotEmpty(message ="형식에 맞춰 입력")
        @Pattern(regexp = "^[a-zA-Z0-9]*$")
        @Size(min = 3, message = "닉네임 길이가 맞지 않습니다.")
        private String username;

        @NotEmpty(message ="형식에 맞춰 입력")
        @Size(min = 4, message ="4자 이상 입력가능")
        private String password;
    }
}
