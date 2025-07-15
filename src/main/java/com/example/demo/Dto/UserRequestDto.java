package com.example.demo.Dto;

import com.example.demo.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String userId;
    private String userPw;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .userPw(userPw)
                .build();
    }

}
