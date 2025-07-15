package com.example.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId; // 사용자 ID
    private String userPw; // 사용자 비밀번호
    @Builder.Default
    private Long currentPoints = 0L; // 현재 보유 포인트
    @Builder.Default
    private Integer leftoverSeconds = 0; // 자투리 초 (다음 세션에 누적)
    @Builder.Default
    private Long totalFocusTime = 0L; // 총 집중시간 (초 단위)
}
