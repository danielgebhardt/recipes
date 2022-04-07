package com.example.recipe;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<Object> createRecipe(@RequestBody Recipe recipe) {
        return recipeService.createTheRecipe(recipe);
    }



    @GetMapping
    public ResponseEntity<Object> listRecipes() {
        return recipeService.listTheRecipes();
    }
}
