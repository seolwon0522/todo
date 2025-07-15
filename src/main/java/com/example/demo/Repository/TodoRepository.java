package com.example.demo.Repository;

import com.example.demo.Entity.Todo;
import com.example.demo.Entity.User;
import com.example.demo.Enum.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUser(User user);

    Optional<Todo> findByIdAndUser(Long id, User user);
}
