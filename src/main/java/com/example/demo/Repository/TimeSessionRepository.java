package com.example.demo.Repository;

import com.example.demo.Entity.TimeSession;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSessionRepository extends JpaRepository<TimeSession, Long> {
    
    List<TimeSession> findByUser(User user);

}