package com.expensetracker.service;

import com.expensetracker.dto.CategoryDTO;
import com.expensetracker.model.Category;
import com.expensetracker.model.User;
import com.expensetracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private UserService userService;
    
    public CategoryDTO createCategory(CategoryDTO categoryDTO, String username) {
        User user = userService.findByUsername(username);
        
        // Check if category name already exists for this user
        if (categoryRepository.existsByNameAndUserId(categoryDTO.getName(), user.getId())) {
            throw new RuntimeException("Category with this name already exists");
        }
        
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setColor(categoryDTO.getColor());
        category.setUser(user);
        
        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }
    
    public List<CategoryDTO> getUserCategories(String username) {
        User user = userService.findByUsername(username);
        List<Category> categories = categoryRepository.findByUserId(user.getId());
        return categories.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO, String username) {
        User user = userService.findByUsername(username);
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        
        // Verify category belongs to user
        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Category does not belong to user");
        }
        
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setColor(categoryDTO.getColor());
        
        Category updatedCategory = categoryRepository.save(category);
        return convertToDTO(updatedCategory);
    }
    
    public void deleteCategory(Long id, String username) {
        User user = userService.findByUsername(username);
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        
        // Verify category belongs to user
        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Category does not belong to user");
        }
        
        // Check if category has expenses
        if (category.getExpenses() != null && !category.getExpenses().isEmpty()) {
            throw new RuntimeException("Cannot delete category with existing expenses");
        }
        
        categoryRepository.delete(category);
    }
    
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setColor(category.getColor());
        return dto;
    }
}