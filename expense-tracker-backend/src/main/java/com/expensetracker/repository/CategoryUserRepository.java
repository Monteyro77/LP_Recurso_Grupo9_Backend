package com.expensetracker.repository;

import com.expensetracker.model.CategoryUser;
import com.expensetracker.model.Category;
import com.expensetracker.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryUserRepository extends JpaRepository<CategoryUser, Long> {

    // Verifica se uma categoria já está associada a um utilizador
    boolean existsByUserAndCategory(User user, Category category);

    // Listar categorias associadas a um utilizador
    List<CategoryUser> findByUser(User user);
}