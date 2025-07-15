package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "time_sessions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;
    
    private Integer sessionSeconds;
    private Long pointsEarned;
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    public static TimeSession createSession(User user, Todo todo, Long seconds, Long points) {
        LocalDateTime now = LocalDateTime.now();
        return TimeSession.builder()
                .user(user)
                .todo(todo)
                .sessionSeconds(seconds.intValue())
                .pointsEarned(points)
                .startTime(now.minusSeconds(seconds))
                .endTime(now)
                .build();
    }
    
    @PrePersist
    protected void onCreate() {
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
    }
} 