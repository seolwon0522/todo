package com.example.demo.Controller;

import com.example.demo.Dto.ApiResponse;
import com.example.demo.Dto.ItemResponseDto;
import com.example.demo.Dto.PurchaseResponseDto;
import com.example.demo.Entity.User;
import com.example.demo.Service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class ShopController extends BaseController {
    
    private final ShopService shopService;
    
    @GetMapping("/items")
    public ResponseEntity<ApiResponse<List<ItemResponseDto>>> getAllItems(Principal principal) {
        User user = getCurrentUser(principal);
        List<ItemResponseDto> items = shopService.getAllItems(user);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/items/category/{category}")
    public ResponseEntity<ApiResponse<List<ItemResponseDto>>> getItemsByCategory(
            @PathVariable String category, Principal principal) {
        User user = getCurrentUser(principal);
        List<ItemResponseDto> items = shopService.getItemsByCategory(category, user);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @PostMapping("/purchase/{itemId}")
    public ResponseEntity<ApiResponse<PurchaseResponseDto>> purchaseItem(
            @PathVariable Long itemId, Principal principal) {
        try {
            User user = getCurrentUser(principal);
            PurchaseResponseDto purchase = shopService.purchaseItem(itemId, user);
            return ResponseEntity.ok(ApiResponse.success(purchase, "구매가 완료되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/purchases")
    public ResponseEntity<ApiResponse<List<PurchaseResponseDto>>> getPurchaseHistory(Principal principal) {
        User user = getCurrentUser(principal);
        List<PurchaseResponseDto> purchases = shopService.getPurchaseHistory(user);
        return ResponseEntity.ok(ApiResponse.success(purchases));
    }
}