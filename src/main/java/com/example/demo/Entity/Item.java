package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private Long price;
    private String category;
    
    @Builder.Default
    private Boolean isActive = true; // 아이템 활성화 상태 기본값 true
}