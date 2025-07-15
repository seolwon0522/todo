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
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @Builder.Default
    private Long totalFocusTime = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 할 일을 소유한 사용자
}
