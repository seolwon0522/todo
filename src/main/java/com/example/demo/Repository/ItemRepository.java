package com.example.demo.Repository;

import com.example.demo.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByIsActiveTrue();
    List<Item> findByCategoryAndIsActiveTrue(String category);
}