package com.example.demo.Service;

import com.example.demo.Dto.TimeSessionResponseDto;
import com.example.demo.Entity.TimeSession;
import com.example.demo.Entity.User;
import com.example.demo.Repository.TimeSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointsService {
    
    private final TimeSessionRepository timeSessionRepository;
    
    public List<TimeSessionResponseDto> getPointsHistory(User user) {
        return timeSessionRepository.findByUser(user)
                .stream()
                .map(TimeSessionResponseDto::fromEntity)
                .collect(Collectors.toList());
    }


} 