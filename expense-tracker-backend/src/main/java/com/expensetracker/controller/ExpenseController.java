package com.expensetracker.controller;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    
    @Autowired
    private ExpenseService expenseService;
    
    // ... outros métodos permanecem iguais ...
    
    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseDTO>> getFilteredExpenses(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount) {
        
        List<ExpenseDTO> expenses;
        
        if (paymentMethod != null) {
            // Usa filtro completo com método de pagamento
            expenses = expenseService.getExpensesWithAllFilters(
                userDetails.getUsername(), 
                categoryId, 
                startDate, 
                endDate, 
                paymentMethod,
                minAmount, 
                maxAmount
            );
        } else {
            // Usa filtro básico
            expenses = expenseService.getExpensesWithFilters(
                userDetails.getUsername(), 
                categoryId, 
                startDate, 
                endDate, 
                minAmount, 
                maxAmount
            );
        }
        
        return ResponseEntity.ok(expenses);
    }
    
    // Endpoint para busca por descrição
    @GetMapping("/search")
    public ResponseEntity<List<ExpenseDTO>> searchExpenses(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String query) {
        
        List<ExpenseDTO> expenses = expenseService.searchExpensesByDescription(
            userDetails.getUsername(), query);
        return ResponseEntity.ok(expenses);
    }
}