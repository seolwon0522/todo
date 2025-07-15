package com.example.demo.Config;

import com.example.demo.Entity.Item;
import com.example.demo.Repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class





DataLoader implements CommandLineRunner {
    
    private final ItemRepository itemRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (itemRepository.count() == 0) {
            loadSampleItems();
        }
    }
    
    private void loadSampleItems() {
        // 테마 카테고리
        itemRepository.save(Item.builder()
                .name("다크 테마")
                .description("눈이 편안한 어두운 테마입니다")
                .price(100L)
                .category("테마")
                .build());
                
        itemRepository.save(Item.builder()
                .name("파스텔 테마")
                .description("부드러운 파스텔 색상의 테마입니다")
                .price(150L)
                .category("테마")
                .build());
                
        itemRepository.save(Item.builder()
                .name("네온 테마")
                .description("화려한 네온 색상의 테마입니다")
                .price(200L)
                .category("테마")
                .build());
        
        // 아이콘 카테고리
        itemRepository.save(Item.builder()
                .name("귀여운 동물 아이콘")
                .description("귀여운 동물 모양의 Todo 아이콘 세트")
                .price(80L)
                .category("아이콘")
                .build());
                
        itemRepository.save(Item.builder()
                .name("미니멀 아이콘")
                .description("깔끔한 미니멀 스타일 아이콘 세트")
                .price(60L)
                .category("아이콘")
                .build());
                
        itemRepository.save(Item.builder()
                .name("이모지 아이콘")
                .description("다양한 이모지 아이콘 세트")
                .price(40L)
                .category("아이콘")
                .build());
        
        // 기능 카테고리
        itemRepository.save(Item.builder()
                .name("무제한 Todo")
                .description("Todo 개수 제한을 해제합니다")
                .price(500L)
                .category("기능")
                .build());
                
        itemRepository.save(Item.builder()
                .name("알림 기능")
                .description("Todo 마감일 알림 기능을 활성화합니다")
                .price(300L)
                .category("기능")
                .build());
                
        itemRepository.save(Item.builder()
                .name("통계 대시보드")
                .description("상세한 생산성 통계를 확인할 수 있습니다")
                .price(250L)
                .category("기능")
                .build());
                
        itemRepository.save(Item.builder()
                .name("백업 기능")
                .description("Todo 데이터를 클라우드에 백업합니다")
                .price(400L)
                .category("기능")
                .build());
    }
}