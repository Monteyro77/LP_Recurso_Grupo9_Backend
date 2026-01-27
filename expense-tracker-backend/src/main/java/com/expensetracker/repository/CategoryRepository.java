package com.expensetracker.repository;

import com.expensetracker.model.Category;
import com.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUser(User user);
}
