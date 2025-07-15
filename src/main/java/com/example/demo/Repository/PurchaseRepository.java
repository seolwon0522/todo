package com.example.demo.Repository;

import com.example.demo.Entity.Purchase;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByUser(User user);
    boolean existsByUserAndItem_Id(User user, Long itemId);
}