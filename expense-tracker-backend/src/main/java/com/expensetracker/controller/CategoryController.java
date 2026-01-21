package com.expensetracker.controller;

import com.expensetracker.dto.CategoryDTO;
import com.expensetracker.model.User;
import com.expensetracker.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Listar categorias globais
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // Criar categoria global (mais tarde podemos limitar a admin)
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }

    // Associar categoria ao utilizador autenticado
    @PostMapping("/{categoryId}/assign")
    public ResponseEntity<Void> assignCategory(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal User user
    ) {
        categoryService.assignCategoryToUser(user.getId(), categoryId);
        return ResponseEntity.noContent().build();
    }
}
