package com.example.demo.Dto;

import com.example.demo.Entity.TimeSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSessionResponseDto {
    private Long id;
    private Long userId;
    private Long todoId;
    private Integer sessionSeconds;
    private Long pointsEarned;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    public static TimeSessionResponseDto fromEntity(TimeSession timeSession) {
        return TimeSessionResponseDto.builder()
                .id(timeSession.getId())
                .userId(timeSession.getUser().getId())
                .todoId(timeSession.getTodo().getId())
                .sessionSeconds(timeSession.getSessionSeconds())
                .pointsEarned(timeSession.getPointsEarned())
                .startTime(timeSession.getStartTime())
                .endTime(timeSession.getEndTime())
                .build();
    }
} 