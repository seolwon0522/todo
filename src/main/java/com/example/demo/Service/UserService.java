package com.example.demo.Service;

import com.example.demo.Dto.UserRequestDto;
import com.example.demo.Dto.UserResponseDto;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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
    
    public UserResponseDto login(UserRequestDto userRequestDto, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userRequestDto.getUserId(), 
                userRequestDto.getUserPw()
            )
        );
        

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        request.getSession().setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
            securityContext
        );
        
        User user = userRepository.findByUserId(userRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        return UserResponseDto.fromEntity(user);
    }
    
    public UserResponseDto getCurrentUser(User user) {
        // DB에서 최신 정보 조회
        User freshUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        return UserResponseDto.fromEntity(freshUser);
    }
    

    

}
