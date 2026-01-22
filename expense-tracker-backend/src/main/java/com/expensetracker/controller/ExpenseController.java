package com.expensetracker.controller;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.model.User;
import com.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // Criar despesa
    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(
            @Valid @RequestBody ExpenseDTO expenseDTO,
            @AuthenticationPrincipal User user) {

        if (user == null) return ResponseEntity.status(401).build();

        ExpenseDTO created = expenseService.createExpense(expenseDTO, user.getId());
        return ResponseEntity.ok(created);
    }

    // Listar despesas do utilizador
    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getUserExpenses(
            @AuthenticationPrincipal User user) {

        if (user == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(expenseService.getUserExpenses(user.getId()));
    }

    // Editar despesa
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseDTO expenseDTO,
            @AuthenticationPrincipal User user) {

        if (user == null) return ResponseEntity.status(401).build();

        ExpenseDTO updated = expenseService.updateExpense(id, expenseDTO, user.getId());
        return ResponseEntity.ok(updated);
    }

    // Apagar despesa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user == null) return ResponseEntity.status(401).build();

        expenseService.deleteExpense(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    // Filtrar despesas
    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseDTO>> filterExpenses(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount) {

        if (user == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(expenseService.filterExpenses(
                user.getId(),
                categoryId,
                startDate,
                endDate,
                paymentMethod,
                minAmount,
                maxAmount
        ));
    }

    // Pesquisar por descrição
    @GetMapping("/search")
    public ResponseEntity<List<ExpenseDTO>> searchExpenses(
            @AuthenticationPrincipal User user,
            @RequestParam String query) {

        if (user == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(
                expenseService.searchExpensesByDescription(user.getId(), query)
        );
    }
}