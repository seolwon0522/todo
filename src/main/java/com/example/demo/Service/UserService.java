package com.example.demo.Service;

import com.example.demo.Dto.UserRequestDto;
import com.example.demo.Dto.UserResponseDto;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserResponseDto register(UserRequestDto userRequestDto) {
        if (userRepository.existsByUserId(userRequestDto.getUserId())) {
            throw new RuntimeException("이미 사용중인 아이디입니다");
        }

        User user = userRequestDto.toEntity();
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        
        User savedUser = userRepository.save(user);
        return UserResponseDto.fromEntity(savedUser);
    }
    
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
    }
    
    public UserResponseDto authenticateUser(UserRequestDto userRequestDto, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userRequestDto.getUserId(), userRequestDto.getUserPw())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 세션에 SecurityContext 저장
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                            SecurityContextHolder.getContext());
        
        User user = findByUserId(userRequestDto.getUserId());
        return UserResponseDto.fromEntity(user);
    }
    
    public UserResponseDto getCurrentUser(User user) {
        User freshUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        return UserResponseDto.fromEntity(freshUser);
    }
}
