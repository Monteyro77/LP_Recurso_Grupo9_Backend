package com.expensetracker.controller;

import com.expensetracker.dto.CategoryDTO;
import com.expensetracker.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryDTO create(@RequestBody CategoryDTO dto) {
        return categoryService.create(dto);
    }

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
