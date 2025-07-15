package com.example.demo.Dto;

import com.example.demo.Entity.Purchase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponseDto {
    private Long id;
    private String itemName;
    private Long pointsSpent;
    private LocalDateTime purchaseDate;
    
    public static PurchaseResponseDto fromEntity(Purchase purchase) {
        return PurchaseResponseDto.builder()
                .id(purchase.getId())
                .itemName(purchase.getItem().getName())
                .pointsSpent(purchase.getPointsSpent())
                .purchaseDate(purchase.getPurchaseDate())
                .build();
    }
}