package com.example.demo.Entity;

import com.example.demo.Enum.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "todos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 할 일 ID
    private String title; // 할 일 제목
    private String description; // 할 일 상세 설명
    @Enumerated(EnumType.STRING)
    private Status status; // 할 일 상태 (TODO, IN_PROGRESS, DONE)
    
    @Builder.Default
    private Long totalFocusTime = 0L; // 총 집중시간 (초 단위)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 할 일을 소유한 사용자
}
