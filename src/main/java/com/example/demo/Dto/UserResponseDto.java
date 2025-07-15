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
public class UserResponseDto {
    private Long id;
    private String userId;
    private Long currentPoints;
    private Long totalFocusTime;

    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .currentPoints(user.getCurrentPoints())
                .totalFocusTime(user.getTotalFocusTime())
                .build();
    }
}
