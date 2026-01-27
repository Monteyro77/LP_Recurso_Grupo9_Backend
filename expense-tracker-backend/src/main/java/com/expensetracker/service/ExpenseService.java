package com.expensetracker.service;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.model.User;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public ExpenseService(ExpenseRepository expenseRepository,
                          CategoryRepository categoryRepository,
                          UserService userService) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    public ExpenseDTO create(ExpenseDTO dto) {
        User user = userService.getAuthenticatedUser();

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().equals(user)) {
            throw new RuntimeException("Invalid category");
        }

        Expense expense = new Expense();
        expense.setDescription(dto.getDescription());
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setPaymentMethod(dto.getPaymentMethod());
        expense.setCategory(category);
        expense.setUser(user);

        expense = expenseRepository.save(expense);

        dto.setId(expense.getId());
        dto.setCategoryName(category.getName());

        return dto;
    }

    public List<ExpenseDTO> getAll() {
        User user = userService.getAuthenticatedUser();

        return expenseRepository.findByUser(user)
                .stream()
                .map(e -> {
                    ExpenseDTO dto = new ExpenseDTO();
                    dto.setId(e.getId());
                    dto.setDescription(e.getDescription());
                    dto.setAmount(e.getAmount());
                    dto.setDate(e.getDate());
                    dto.setPaymentMethod(e.getPaymentMethod());
                    dto.setCategoryId(e.getCategory().getId());
                    dto.setCategoryName(e.getCategory().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().equals(userService.getAuthenticatedUser())) {
            throw new RuntimeException("Access denied");
        }

        expenseRepository.delete(expense);
    }
}
