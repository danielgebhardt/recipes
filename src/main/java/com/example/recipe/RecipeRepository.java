package com.example.recipe;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    public Iterable<Recipe> findAllByCaloriesBetweenOrderByCaloriesDesc(int min, int max);
    public Iterable<Recipe> findAllByCaloriesBetweenOrderByCaloriesAsc(int min, int max);
    public Iterable<Recipe> findByOrderByCaloriesAsc();
    public Iterable<Recipe> findByOrderByCaloriesDesc();
}
