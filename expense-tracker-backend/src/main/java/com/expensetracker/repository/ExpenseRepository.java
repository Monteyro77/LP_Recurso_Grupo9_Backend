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
    List<Expense> findByUserId(Long userId);
    
    List<Expense> findByUserIdAndCategoryId(Long userId, Long categoryId);
    
    List<Expense> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    
    List<Expense> findByUserIdAndDate(Long userId, LocalDate date);
    
    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId " +
           "AND (:categoryId IS NULL OR e.category.id = :categoryId) " +
           "AND (:startDate IS NULL OR e.date >= :startDate) " +
           "AND (:endDate IS NULL OR e.date <= :endDate) " +
           "AND (:paymentMethod IS NULL OR e.paymentMethod = :paymentMethod) " +
           "AND (:minAmount IS NULL OR e.amount >= :minAmount) " +
           "AND (:maxAmount IS NULL OR e.amount <= :maxAmount) " +
           "ORDER BY e.date DESC")
    List<Expense> findWithFilters(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("paymentMethod") String paymentMethod,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId")
    BigDecimal getTotalExpensesByUser(@Param("userId") Long userId);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId " +
           "AND e.date BETWEEN :startDate AND :endDate")
    BigDecimal getTotalExpensesByUserAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT e.category.name, SUM(e.amount) FROM Expense e " +
           "WHERE e.user.id = :userId " +
           "AND e.date BETWEEN :startDate AND :endDate " +
           "GROUP BY e.category.name " +
           "ORDER BY SUM(e.amount) DESC")
    List<Object[]> getExpensesByCategoryAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT e.paymentMethod, SUM(e.amount) FROM Expense e " +
           "WHERE e.user.id = :userId " +
           "GROUP BY e.paymentMethod")
    List<Object[]> getExpensesByPaymentMethod(@Param("userId") Long userId);
    
    @Query("SELECT MONTH(e.date), YEAR(e.date), SUM(e.amount) FROM Expense e " +
           "WHERE e.user.id = :userId " +
           "GROUP BY YEAR(e.date), MONTH(e.date) " +
           "ORDER BY YEAR(e.date), MONTH(e.date)")
    List<Object[]> getMonthlyExpenses(@Param("userId") Long userId);
    
    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId " +
           "AND LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Expense> searchByDescription(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);
}