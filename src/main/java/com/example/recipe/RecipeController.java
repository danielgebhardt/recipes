package com.example.recipe;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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

    @GetMapping("/{id}")
    public ResponseEntity<Object> readRecipe(@PathVariable long id) {
        return recipeService.readTheRecipe(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateRecipe(@PathVariable long id, @RequestBody HashMap<String, Object> map) {
        return recipeService.updateTheRecipe(id, map);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRecipe(@PathVariable long id) {
        return recipeService.deleteTheRecipe(id);
    }

    @GetMapping
    public ResponseEntity<Object> listRecipes() {
        return recipeService.listTheRecipes();
    }
}
