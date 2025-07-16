package com.example.demo.Controller;

import com.example.demo.Dto.ApiResponse;
import com.example.demo.Dto.TodoRequestDto;
import com.example.demo.Dto.TodoResponseDto;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoController {
    private final UserRepository userRepository;
    private final TodoService todoService;
    
    protected User getCurrentUser(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("인증이 필요합니다");
        }
        return userRepository.findByUserId(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을수없습니다"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TodoResponseDto>> createTodo(@RequestBody TodoRequestDto todoRequestDto, Principal principal) {
        User user = getCurrentUser(principal);
        TodoResponseDto response = todoService.createTodo(todoRequestDto, user);
        return ResponseEntity.ok(ApiResponse.success(response, "Todo created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponseDto>> getTodoById(@PathVariable Long id, Principal principal) {
        User user = getCurrentUser(principal);
        TodoResponseDto response = todoService.getTodoById(id, user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoResponseDto>>> getAllTodos(Principal principal) {
        User user = getCurrentUser(principal);
        List<TodoResponseDto> response = todoService.getAllTodos(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponseDto>> updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto todoRequestDto, Principal principal) {
        User user = getCurrentUser(principal);
        TodoResponseDto response = todoService.updateTodo(id, todoRequestDto, user);
        return ResponseEntity.ok(ApiResponse.success(response, "Todo updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(@PathVariable Long id, Principal principal) {
        User user = getCurrentUser(principal);
        todoService.deleteTodo(id, user);
        return ResponseEntity.ok(ApiResponse.success(null, "Todo deleted successfully"));
    }

    @PutMapping("/{id}/focus-time")
    public ResponseEntity<ApiResponse<TodoResponseDto>> updateFocusTime(@PathVariable Long id, @RequestBody Map<String, Long> request, Principal principal) {
        User user = getCurrentUser(principal);
        Long additionalSeconds = request.get("additionalSeconds");
        TodoResponseDto response = todoService.updateFocusTime(id, additionalSeconds, user);
        return ResponseEntity.ok(ApiResponse.success(response, "Focus time updated successfully"));
    }
}
