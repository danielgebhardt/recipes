package com.example.recipe;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }


    public ResponseEntity<Object> createTheRecipe(Recipe recipe) {
        Set<Ingredient> verifiedIngredients = checkForOldIngredients(recipe.getIngredients());
        recipe.setIngredients(verifiedIngredients);
        Recipe newRecipe = recipeRepository.save(recipe);
        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }


    public ResponseEntity<Object> listTheRecipes() {
        return new ResponseEntity<>(recipeRepository.findAll(), HttpStatus.OK);
    }


    private Set<Ingredient> checkForOldIngredients(Set<Ingredient> ingredients) {
        Set<Ingredient> newIngredients = new HashSet<>();
        newIngredients.addAll(ingredients);

        for (Ingredient eachIngredient : ingredients) {
            Iterable<Ingredient> checkIfIngredientExists = ingredientRepository.findIngredientByName(eachIngredient.getName());

            for (Ingredient dbIngredient : checkIfIngredientExists) {
                if (dbIngredient.isContainsGluten() == eachIngredient.isContainsGluten()) {
                    newIngredients.remove(eachIngredient);
                    newIngredients.add(dbIngredient);
                }
            }
        }

        return newIngredients;
    }


}