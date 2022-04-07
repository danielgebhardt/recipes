package com.example.recipe;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {

    public Iterable<Ingredient> findIngredientByName(String name);
}
