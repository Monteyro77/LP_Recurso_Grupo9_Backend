package com.expensetracker.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Column
    private String color;

    // 1 categoria -> várias despesas
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Expense> expenses;

    // 1 categoria -> vários utilizadores (via CategoryUser)
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<CategoryUser> users;

    // Construtor
    public Category() {}

    public Category(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    // Getters e Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public List<Expense> getExpenses() { return expenses; }

    public void setExpenses(List<Expense> expenses) { this.expenses = expenses; }

    public List<CategoryUser> getUsers() { return users; }

    public void setUsers(List<CategoryUser> users) { this.users = users; }
}