package com.example.demo.Dto;

import com.example.demo.Entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String category;
    private Boolean isPurchased; // 사용자가 구매했는지 여부

    
    public static ItemResponseDto fromEntity(Item item, boolean isPurchased) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .category(item.getCategory())
                .isPurchased(isPurchased)
                .build();
    }
}