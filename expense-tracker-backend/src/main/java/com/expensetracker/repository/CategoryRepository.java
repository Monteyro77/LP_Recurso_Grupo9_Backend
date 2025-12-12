package com.expensetracker.repository;

import com.expensetracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(Long userId);
    
    @Query("SELECT c FROM Category c WHERE c.user.id = :userId AND c.name = :name")
    Optional<Category> findByNameAndUserId(@Param("name") String name, @Param("userId") Long userId);
    
    boolean existsByNameAndUserId(String name, Long userId);
    
    @Query("SELECT COUNT(e) > 0 FROM Expense e WHERE e.category.id = :categoryId")
    boolean hasExpenses(@Param("categoryId") Long categoryId);
    
    @Query("SELECT c, COALESCE(SUM(e.amount), 0) FROM Category c " +
           "LEFT JOIN Expense e ON e.category.id = c.id " +
           "WHERE c.user.id = :userId AND (:startDate IS NULL OR e.date >= :startDate) " +
           "AND (:endDate IS NULL OR e.date <= :endDate) " +
           "GROUP BY c.id")
    List<Object[]> getCategoriesWithTotalExpense(@Param("userId") Long userId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);
}