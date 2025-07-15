package com.example.demo.Service;

import com.example.demo.Dto.ItemResponseDto;
import com.example.demo.Dto.PurchaseResponseDto;
import com.example.demo.Entity.Item;
import com.example.demo.Entity.Purchase;
import com.example.demo.Entity.User;
import com.example.demo.Repository.ItemRepository;
import com.example.demo.Repository.PurchaseRepository;
import com.example.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopService {
    
    private final ItemRepository itemRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    
    public List<ItemResponseDto> getAllItems(User user) {
        List<Item> items = itemRepository.findByIsActiveTrue();
        Set<Long> purchasedItemIds = purchaseRepository.findByUser(user)
                .stream()
                .map(purchase -> purchase.getItem().getId())
                .collect(Collectors.toSet());
        
        return items.stream()
                .map(item -> ItemResponseDto.fromEntity(item, purchasedItemIds.contains(item.getId())))
                .toList();
    }
    
    public List<ItemResponseDto> getItemsByCategory(String category, User user) {
        List<Item> items = itemRepository.findByCategoryAndIsActiveTrue(category);
        Set<Long> purchasedItemIds = purchaseRepository.findByUser(user)
                .stream()
                .map(purchase -> purchase.getItem().getId())
                .collect(Collectors.toSet());
        
        return items.stream()
                .map(item -> ItemResponseDto.fromEntity(item, purchasedItemIds.contains(item.getId())))
                .toList();
    }
    
    public PurchaseResponseDto purchaseItem(Long itemId, User user) {
        // 아이템 존재 여부 확인
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템을 찾을 수 없습니다"));
        
        if (!item.getIsActive()) {
            throw new IllegalArgumentException("현재 판매되지 않는 아이템입니다");
        }
        
        // 이미 구매한 아이템인지 확인
        if (purchaseRepository.existsByUserAndItem_Id(user, itemId)) {
            throw new IllegalArgumentException("이미 구매한 아이템입니다");
        }
        
        // 포인트 부족 확인
        if (user.getCurrentPoints() < item.getPrice()) {
            throw new IllegalArgumentException("포인트가 부족합니다");
        }
        
        // 포인트 차감
        user.setCurrentPoints(user.getCurrentPoints() - item.getPrice());
        userRepository.save(user);
        
        // 구매 기록 생성
        Purchase purchase = Purchase.builder()
                .user(user)
                .item(item)
                .pointsSpent(item.getPrice())
                .build();
        
        Purchase savedPurchase = purchaseRepository.save(purchase);
        return PurchaseResponseDto.fromEntity(savedPurchase);
    }
    
    public List<PurchaseResponseDto> getPurchaseHistory(User user) {
        return purchaseRepository.findByUser(user)
                .stream()
                .map(PurchaseResponseDto::fromEntity)
                .toList();
    }
}