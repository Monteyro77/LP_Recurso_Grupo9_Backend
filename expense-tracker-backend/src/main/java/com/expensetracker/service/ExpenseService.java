package com.expensetracker.service;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.model.Expense;
import com.expensetracker.model.User;
import com.expensetracker.model.Category;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          CategoryRepository categoryRepository,
                          UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // Criar despesa
    public ExpenseDTO createExpense(ExpenseDTO dto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Expense expense = new Expense();
        expense.setDescription(dto.getDescription());
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setPaymentMethod(dto.getPaymentMethod());
        expense.setUser(user);
        expense.setCategory(category);

        Expense saved = expenseRepository.save(expense);
        return mapToDTO(saved);
    }

    // Listar despesas do utilizador
    public List<ExpenseDTO> getUserExpenses(Long userId) {

        List<Expense> expenses = expenseRepository.findByUserId(userId);
        return expenses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Filtrar despesas 
    public List<ExpenseDTO> filterExpenses(
            Long userId,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            String paymentMethod,
            Double minAmount,
            Double maxAmount) {

        BigDecimal minBD = (minAmount != null) ? BigDecimal.valueOf(minAmount) : null;
        BigDecimal maxBD = (maxAmount != null) ? BigDecimal.valueOf(maxAmount) : null;

        List<Expense> expenses = expenseRepository.findWithFilters(
                userId,
                categoryId,
                startDate,
                endDate,
                paymentMethod,
                minBD,
                maxBD
        );

        return expenses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Pesquisar despesas por descrição
    public List<ExpenseDTO> searchExpensesByDescription(Long userId, String query) {

        List<Expense> expenses =
                expenseRepository.searchByDescription(userId, query);

        return expenses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    // Editar despesa
    public ExpenseDTO updateExpense(Long expenseId, ExpenseDTO dto, Long userId) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        // Garante que a despesa é do utilizador autenticado
        if (!expense.getUser().getId().equals(userId)) {
            throw new RuntimeException("Expense does not belong to user");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        expense.setDescription(dto.getDescription());
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setPaymentMethod(dto.getPaymentMethod());
        expense.setCategory(category);

        Expense updated = expenseRepository.save(expense);
        return mapToDTO(updated);
    }

    // Apagar despesa
    public void deleteExpense(Long expenseId, Long userId) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        // Garante que a despesa é do utilizador autenticado
        if (!expense.getUser().getId().equals(userId)) {
            throw new RuntimeException("Expense does not belong to user");
        }

        expenseRepository.delete(expense);
    }
    
    // Mapper privado
    private ExpenseDTO mapToDTO(Expense expense) {

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