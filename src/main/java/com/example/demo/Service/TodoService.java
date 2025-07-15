package com.example.demo.Service;

import com.example.demo.Dto.TodoRequestDto;
import com.example.demo.Dto.TodoResponseDto;
import com.example.demo.Entity.TimeSession;
import com.example.demo.Entity.Todo;
import com.example.demo.Entity.User;
import com.example.demo.Enum.Status;
import com.example.demo.Repository.TimeSessionRepository;
import com.example.demo.Repository.TodoRepository;
import com.example.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final TimeSessionRepository timeSessionRepository;

    private Todo findTodo(Long id, User user) {
        return todoRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("할일을 찾을 수 없습니다"));
    }

    public TodoResponseDto createTodo(TodoRequestDto request, User user) {
        Todo todo = request.toEntity();
        if (todo.getStatus() == null) {
            todo.setStatus(Status.TODO);
        }
        todo.setUser(user);
        return TodoResponseDto.fromEntity(todoRepository.save(todo));
    }

    public TodoResponseDto getTodoById(Long id, User user) {
        Todo todo = findTodo(id, user);
        return TodoResponseDto.fromEntity(todo);
    }

    public TodoResponseDto updateTodo(Long id, TodoRequestDto request, User user) {
        Todo existingTodo = findTodo(id, user);
        
        existingTodo.setTitle(request.getTitle());
        existingTodo.setDescription(request.getDescription());
        existingTodo.setStatus(request.getStatus() != null ? request.getStatus() : Status.TODO);

        return TodoResponseDto.fromEntity(todoRepository.save(existingTodo));
    }

    public void deleteTodo(Long id, User user) {
        Todo todoToDelete = findTodo(id, user);
        todoRepository.delete(todoToDelete);
    }

    public List<TodoResponseDto> getAllTodos(User user) {
        return todoRepository.findByUser(user).stream()
                .map(TodoResponseDto::fromEntity)
                .toList();
    }

    public TodoResponseDto updateFocusTime(Long id, Long additionalSeconds, User user) {
        Todo todo = findTodo(id, user);

        Long earnedPoints = calculateEarnedPoints(user, additionalSeconds);
        
        updateTodoFocusTime(todo, additionalSeconds);
        Todo savedTodo = todoRepository.save(todo);
        
        createTimeSession(user, savedTodo, additionalSeconds, earnedPoints);
        updateUserStats(user, additionalSeconds, earnedPoints);
        
        return TodoResponseDto.fromEntity(savedTodo);
    }
    
    private Long calculateEarnedPoints(User user, Long seconds) {
        Integer leftoverSeconds = user.getLeftoverSeconds() != null ? user.getLeftoverSeconds() : 0;
        
        Long totalSeconds = leftoverSeconds + seconds;
        Long points = totalSeconds / 60;
        Integer newLeftoverSeconds = (int) (totalSeconds % 60);
        
        user.setLeftoverSeconds(newLeftoverSeconds);
        
        return points;
    }
    
    private void updateTodoFocusTime(Todo todo, Long additionalSeconds) {
        Long currentFocusTime = todo.getTotalFocusTime() != null ? todo.getTotalFocusTime() : 0L;
        todo.setTotalFocusTime(currentFocusTime + additionalSeconds);
    }
    
    private void createTimeSession(User user, Todo todo, Long seconds, Long points) {
        TimeSession session = TimeSession.createSession(user, todo, seconds, points);
        timeSessionRepository.save(session);
    }
    
    private void updateUserStats(User user, Long additionalSeconds, Long earnedPoints) {
        Long currentFocusTime = user.getTotalFocusTime() != null ? user.getTotalFocusTime() : 0L;
        user.setTotalFocusTime(currentFocusTime + additionalSeconds);
        
        if (earnedPoints > 0) {
            Long currentPoints = user.getCurrentPoints() != null ? user.getCurrentPoints() : 0L;
            user.setCurrentPoints(currentPoints + earnedPoints);
        }
        
        userRepository.save(user);
    }
}
