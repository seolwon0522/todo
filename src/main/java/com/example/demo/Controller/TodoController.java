package com.example.demo.Controller;

import com.example.demo.Dto.TodoRequestDto;
import com.example.demo.Dto.TodoResponseDto;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
@CrossOrigin(
    origins = {"http://localhost:3000", "http://127.0.0.1:3000"}, 
    allowCredentials = "true",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class TodoController {
    private final TodoService todoService;
    private final UserRepository userRepository;

    // 현재 사용자 정보 조회 헬퍼 메서드
    private User getCurrentUser(Principal principal) {
        return userRepository.findByUserId(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
    }

    @PostMapping
    public TodoResponseDto createTodo(@RequestBody TodoRequestDto todoRequestDto, Principal principal) {
        User user = getCurrentUser(principal);
        return todoService.createTodo(todoRequestDto, user);
    }

    @GetMapping("/{id}")
    public TodoResponseDto getTodoById(@PathVariable Long id, Principal principal) {
        User user = getCurrentUser(principal);
        return todoService.getTodoById(id, user);
    }

    @GetMapping
    public List<TodoResponseDto> getAllTodos(Principal principal) {
        User user = getCurrentUser(principal);
        return todoService.getAllTodos(user);
    }

    @PutMapping("/{id}")
    public TodoResponseDto updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto todoRequestDto, Principal principal) {
        User user = getCurrentUser(principal);
        return todoService.updateTodo(id, todoRequestDto, user);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id, Principal principal) {
        User user = getCurrentUser(principal);
        todoService.deleteTodo(id, user);
    }

    @PostMapping("/{id}/focus-time")
    public TodoResponseDto updateFocusTime(@PathVariable Long id, @RequestBody Map<String, Long> request, Principal principal) {
        User user = getCurrentUser(principal);
        Long additionalSeconds = request.get("additionalSeconds");
        return todoService.updateFocusTime(id, additionalSeconds, user);
    }
}
