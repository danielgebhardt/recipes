package com.example.recipe;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    long id;

    @Column(name = "recipe_description")
    @NotNull
    String description;

    @Column(name = "recipe_instructions")
    String instructions;

    @Column(name = "recipe_title")
    String title;

    @Column(name = "recipe_calories")
    int calories;

    @Column(name = "recipe_date_created")
    LocalDate dateCreated;

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", instructions='" + instructions + '\'' +
                ", title='" + title + '\'' +
                ", calories=" + calories +
                ", dateCreated=" + dateCreated +
                ", ingredients=" + ingredients +
                '}';
    }

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            }, targetEntity = Ingredient.class)
    @JoinTable(name = "recipes_ingredients",
            joinColumns = @JoinColumn(name = "r_id",
                    referencedColumnName = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "i_id",
                    referencedColumnName = "ingredient_id"))
    private Set<Ingredient> ingredients = new HashSet<>();

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        if (this.ingredients.contains(ingredient)) {
            this.ingredients.remove(ingredient);
        }
    }
}
