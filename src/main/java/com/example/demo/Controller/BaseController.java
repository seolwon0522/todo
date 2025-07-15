package com.example.demo.Controller;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
public abstract class BaseController {

    private final UserRepository userRepository;
    
    protected User getCurrentUser(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("인증이 필요합니다");
        }
        return userRepository.findByUserId(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을수없습니다"));
    }
}