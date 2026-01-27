package com.expensetracker.service;

import com.expensetracker.dto.CategoryDTO;
import com.expensetracker.model.Category;
import com.expensetracker.model.User;
import com.expensetracker.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public CategoryService(CategoryRepository categoryRepository,
                           UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    public CategoryDTO create(CategoryDTO dto) {
        User user = userService.getAuthenticatedUser();

        Category category = new Category();
        category.setName(dto.getName());
        category.setUser(user);

        category = categoryRepository.save(category);

        CategoryDTO result = new CategoryDTO();
        result.setId(category.getId());
        result.setName(category.getName());

        return result;
    }

    public List<CategoryDTO> getAll() {
        User user = userService.getAuthenticatedUser();

        return categoryRepository.findByUser(user)
                .stream()
                .map(category -> {
                    CategoryDTO dto = new CategoryDTO();
                    dto.setId(category.getId());
                    dto.setName(category.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getId()
                .equals(userService.getAuthenticatedUser().getId())) {
            throw new RuntimeException("Access denied");
        }

        categoryRepository.delete(category);
    }
}
