package com.example.demo.Controller;

import com.example.demo.Dto.ApiResponse;
import com.example.demo.Dto.TimeSessionResponseDto;
import com.example.demo.Dto.UserRequestDto;
import com.example.demo.Dto.UserResponseDto;
import com.example.demo.Entity.User;
import com.example.demo.Service.PointsService;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController extends BaseController {
    
    private final UserService userService;
    private final PointsService pointsService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto response = userService.register(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Registration successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDto>> login(@RequestBody UserRequestDto userRequestDto, 
                                               HttpServletRequest request) {
        try {
            UserResponseDto response = userService.authenticateUser(userRequestDto);
            
            // 세션에 저장
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                                SecurityContextHolder.getContext());
            
            return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(ApiResponse.error("Invalid credentials"));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(ApiResponse.success(null, "Logout successful"));
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getMyInfo(Principal principal) {
        User user = getCurrentUser(principal);
        UserResponseDto response = userService.getCurrentUser(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    // Points endpoints under user context
    @GetMapping("/me/points")
    public ResponseEntity<ApiResponse<Long>> getMyPoints(Principal principal) {
        User user = getCurrentUser(principal);
        return ResponseEntity.ok(ApiResponse.success(user.getCurrentPoints()));
    }
    
    @GetMapping("/me/points/history")
    public ResponseEntity<ApiResponse<List<TimeSessionResponseDto>>> getMyPointsHistory(Principal principal) {
        User user = getCurrentUser(principal);
        List<TimeSessionResponseDto> history = pointsService.getPointsHistory(user);
        return ResponseEntity.ok(ApiResponse.success(history));
    }
    

}
