package com.example.demo.Controller;

import com.example.demo.Dto.UserRequestDto;
import com.example.demo.Dto.UserResponseDto;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin(
    origins = {"http://localhost:3000", "http://127.0.0.1:3000"}, 
    allowCredentials = "true",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class UserController {
    
    private final UserService userService;
    private final UserRepository userRepository;

    // 현재 사용자 정보 조회 헬퍼 메서드
    private User getAuthenticatedUser(Principal principal) {
        return userRepository.findByUserId(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto response = userService.register(userRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserRequestDto userRequestDto, 
                                               HttpServletRequest request) {
        UserResponseDto response = userService.login(userRequestDto, request);
        return ResponseEntity.ok(response);
    }
    
    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok("로그아웃되었습니다");
    }
    
    // 현재 사용자 정보 확인
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(Principal principal) {
        User user = getAuthenticatedUser(principal);
        UserResponseDto response = userService.getCurrentUser(user);
        return ResponseEntity.ok(response);
    }
    

}
