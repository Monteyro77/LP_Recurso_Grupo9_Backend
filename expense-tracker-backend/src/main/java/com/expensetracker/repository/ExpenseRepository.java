package com.expensetracker.repository;

import com.expensetracker.model.Expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Listar despesas do utilizador
    List<Expense> findByUserId(Long userId);

    // Filtrar despesas
    @Query("""
        SELECT e FROM Expense e
        WHERE e.user.id = :userId
          AND (:categoryId IS NULL OR e.category.id = :categoryId)
          AND (:startDate IS NULL OR e.date >= :startDate)
          AND (:endDate IS NULL OR e.date <= :endDate)
          AND (:paymentMethod IS NULL OR e.paymentMethod = :paymentMethod)
          AND (:minAmount IS NULL OR e.amount >= :minAmount)
          AND (:maxAmount IS NULL OR e.amount <= :maxAmount)
        ORDER BY e.date DESC
    """)
    List<Expense> findWithFilters(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("paymentMethod") String paymentMethod,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount
    );

    // Pesquisa por descrição
    @Query("""
        SELECT e FROM Expense e
        WHERE e.user.id = :userId
          AND LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
    """)
    List<Expense> searchByDescription(
            @Param("userId") Long userId,
            @Param("searchTerm") String searchTerm
    );
}