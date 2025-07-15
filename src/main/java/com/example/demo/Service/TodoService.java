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
                .orElseThrow(() -> new IllegalArgumentException("Todo not found"));
    }

    public TodoResponseDto createTodo(TodoRequestDto req, User user) {
        Todo todo = req.toEntity();
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

    public TodoResponseDto updateTodo(Long id, TodoRequestDto req, User user) {
        Todo todo = findTodo(id, user);
        
        todo.setTitle(req.getTitle());
        todo.setDescription(req.getDescription());
        todo.setStatus(req.getStatus() != null ? req.getStatus() : Status.TODO);

        return TodoResponseDto.fromEntity(todoRepository.save(todo));
    }

    public void deleteTodo(Long id, User user) {
        Todo todo = findTodo(id, user);
        todoRepository.delete(todo);
    }

    public List<TodoResponseDto> getAllTodos(User user) {
        return todoRepository.findByUser(user).stream()
                .map(TodoResponseDto::fromEntity)
                .toList();
    }

    public TodoResponseDto updateFocusTime(Long id, Long seconds, User user) {
        Todo todo = findTodo(id, user);

        Long points = calcPoints(user, seconds);
        
        Long currentTime = todo.getTotalFocusTime() != null ? todo.getTotalFocusTime() : 0L;
        todo.setTotalFocusTime(currentTime + seconds);
        Todo saved = todoRepository.save(todo);
        
        TimeSession session = TimeSession.createSession(user, saved, seconds, points);
        timeSessionRepository.save(session);
        
        updateUserFocusTime(user, seconds);
        
        if (points > 0) {
            updatePoints(user, points);
        }
        
        return TodoResponseDto.fromEntity(saved);
    }
    
    private Long calcPoints(User user, Long seconds) {
        Integer leftover = user.getLeftoverSeconds();
        if (leftover == null) leftover = 0;
        
        Long total = leftover + seconds;
        Long points = total / 60;
        Integer newLeftover = (int) (total % 60);
        
        user.setLeftoverSeconds(newLeftover);
        userRepository.save(user);
        
        return points;
    }
    
    private void updatePoints(User user, Long points) {
        Long current = user.getCurrentPoints();
        if (current == null) current = 0L;
        user.setCurrentPoints(current + points);
        userRepository.save(user);
    }
    
    private void updateUserFocusTime(User user, Long seconds) {
        Long current = user.getTotalFocusTime();
        if (current == null) current = 0L;
        user.setTotalFocusTime(current + seconds);
        userRepository.save(user);
    }
}
