package com.expensetracker.service;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.model.Expense;
import com.expensetracker.model.User;
import com.expensetracker.model.Category;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private UserService userService;
    
    // ... outros métodos permanecem iguais ...
    
    public List<ExpenseDTO> getExpensesWithFilters(String username, Long categoryId, 
                                                   LocalDate startDate, LocalDate endDate,
                                                   Double minAmount, Double maxAmount) {
        User user = userService.findByUsername(username);
        
        // Converter Double para BigDecimal (maneira segura)
        BigDecimal minAmountBD = null;
        BigDecimal maxAmountBD = null;
        
        if (minAmount != null) {
            minAmountBD = BigDecimal.valueOf(minAmount);
        }
        
        if (maxAmount != null) {
            maxAmountBD = BigDecimal.valueOf(maxAmount);
        }
        
        List<Expense> expenses = expenseRepository.findWithFilters(
            user.getId(), 
            categoryId, 
            startDate, 
            endDate, 
            null, // paymentMethod - pode ser null se não for filtrado
            minAmountBD, 
            maxAmountBD
        );
        
        return expenses.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    // Método adicional para filtros mais completos (opcional)
    public List<ExpenseDTO> getExpensesWithAllFilters(
            String username, 
            Long categoryId, 
            LocalDate startDate, 
            LocalDate endDate,
            String paymentMethod,
            Double minAmount, 
            Double maxAmount) {
        
        User user = userService.findByUsername(username);
        
        // Converter Double para BigDecimal
        BigDecimal minAmountBD = (minAmount != null) ? BigDecimal.valueOf(minAmount) : null;
        BigDecimal maxAmountBD = (maxAmount != null) ? BigDecimal.valueOf(maxAmount) : null;
        
        List<Expense> expenses = expenseRepository.findWithFilters(
            user.getId(), 
            categoryId, 
            startDate, 
            endDate, 
            paymentMethod,
            minAmountBD, 
            maxAmountBD
        );
        
        return expenses.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    // Método para pesquisa por descrição
    public List<ExpenseDTO> searchExpensesByDescription(String username, String searchTerm) {
        User user = userService.findByUsername(username);
        List<Expense> expenses = expenseRepository.searchByDescription(user.getId(), searchTerm);
        
        return expenses.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    private ExpenseDTO convertToDTO(Expense expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setDescription(expense.getDescription());
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setPaymentMethod(expense.getPaymentMethod());
        dto.setCategoryId(expense.getCategory().getId());
        return dto;
    }
}