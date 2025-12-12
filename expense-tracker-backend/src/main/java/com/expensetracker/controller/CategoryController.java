package com.expensetracker.controller;

import com.expensetracker.dto.CategoryDTO;
import com.expensetracker.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        CategoryDTO createdCategory = categoryService.createCategory(
            categoryDTO, userDetails.getUsername());
        return ResponseEntity.ok(createdCategory);
    }
    
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getUserCategories(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<CategoryDTO> categories = categoryService.getUserCategories(
            userDetails.getUsername());
        return ResponseEntity.ok(categories);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        CategoryDTO updatedCategory = categoryService.updateCategory(
            id, categoryDTO, userDetails.getUsername());
        return ResponseEntity.ok(updatedCategory);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        categoryService.deleteCategory(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}