package com.example.demo.Dto;

import com.example.demo.Entity.Todo;
import com.example.demo.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TodoResponseDto {
    private Long id; // Todo 아이디
    private String title; // Todo 제목
    private String description; // Todo 설명
    private Status status; // Todo 상태 (예: "완료", "진행 중", "대기 중")
    private Long totalFocusTime; // 총 집중시간 (초 단위)

    public static TodoResponseDto fromEntity(Todo todo) {
        return new TodoResponseDto(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getStatus(),
                todo.getTotalFocusTime() != null ? todo.getTotalFocusTime() : 0L
        );
    }
}
