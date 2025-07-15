package com.example.demo.Controller;

import com.example.demo.Dto.TimeSessionResponseDto;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.PointsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
@CrossOrigin(
    origins = {"http://localhost:3000"},
    allowCredentials = "true",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class PointsController {
    
    private final PointsService pointsService;
    private final UserRepository userRepository;
    
    private User getCurrentUser(Principal principal) {
        return userRepository.findByUserId(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
    }
    
    @GetMapping("/history")
    public List<TimeSessionResponseDto> getPointsHistory(Principal principal) {
        User user = getCurrentUser(principal);
        return pointsService.getPointsHistory(user);
    }
    
    @GetMapping("/total")
    public Long getTotalPoints(Principal principal) {
        User user = getCurrentUser(principal);
        return pointsService.getTotalPoints(user);
    }
} 