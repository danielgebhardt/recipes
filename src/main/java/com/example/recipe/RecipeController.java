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

    @GetMapping(value = {"", "/{min}/{max}"})
    public ResponseEntity<Object> listRecipes(@PathVariable(required = false) Integer min, @PathVariable(required = false) Integer max, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if(!sortBy.equals("DESC") && !sortBy.equals("ASC")) {
            sortBy = "";
        }

        if(min != null && max != null) {
            return recipeService.listTheRecipes(min, max, sortBy);
        } else {
            return recipeService.listTheRecipes(sortBy);
        }
    }
}
