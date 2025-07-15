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
    private LocalDateTime startTime;
    private Long focusTime;
    private Long points;
    
    public static TimeSessionResponseDto fromEntity(TimeSession timeSession) {
        return TimeSessionResponseDto.builder()
                .id(timeSession.getId())
                .startTime(timeSession.getStartTime())
                .focusTime(timeSession.getSessionSeconds().longValue())
                .points(timeSession.getPointsEarned())
                .build();
    }
} 