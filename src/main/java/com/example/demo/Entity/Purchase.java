package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    
    private Long pointsSpent; // 구매 시 사용한 포인트
    private LocalDateTime purchaseDate;
    
    @PrePersist
    protected void onCreate() {
        purchaseDate = LocalDateTime.now();
    }
}