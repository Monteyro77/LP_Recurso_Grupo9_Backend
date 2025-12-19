package com.expensetracker.service;

import com.expensetracker.dto.CategoryDTO;
import com.expensetracker.model.Category;
import com.expensetracker.model.CategoryUser;
import com.expensetracker.model.User;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.CategoryUserRepository;
import com.expensetracker.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryUserRepository categoryUserRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryUserRepository categoryUserRepository,
                           UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryUserRepository = categoryUserRepository;
        this.userRepository = userRepository;
    }

    // Criar categoria global (ADMIN)
    @Transactional
    public CategoryDTO createCategory(CategoryDTO dto) {

        if (categoryRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }

        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setColor(dto.getColor());

        Category saved = categoryRepository.save(category);
        return mapToDTO(saved);
    }

    // Listar todas as categorias globais
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Associar categoria ao utilizador
    @Transactional
    public void assignCategoryToUser(Long userId, Long categoryId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        boolean alreadyAssigned =
                categoryUserRepository.existsByUserAndCategory(user, category);

        if (alreadyAssigned) {
            throw new RuntimeException("Category already assigned to user");
        }

        CategoryUser categoryUser = new CategoryUser(user, category);
        categoryUserRepository.save(categoryUser);
    }

    // Mapper Entity -> DTO
    private CategoryDTO mapToDTO(Category category) {

        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getColor()
        );
    }
}