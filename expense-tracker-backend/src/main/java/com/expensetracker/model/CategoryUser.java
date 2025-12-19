package com.expensetracker.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "user_categories",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "category_id"})
    }
)
public class CategoryUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Construtor
    public CategoryUser() {}

    public CategoryUser(User user, Category category) {
        this.user = user;
        this.category = category;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

