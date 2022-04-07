package com.example.recipe;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }


    public ResponseEntity<Object> createTheRecipe(Recipe recipe) {
        Set<Ingredient> verifiedIngredients = checkForOldIngredientsForCreate(recipe.getIngredients());
        recipe.setIngredients(verifiedIngredients);
        Recipe newRecipe = recipeRepository.save(recipe);
        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> readTheRecipe(long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);

        if(recipe.isEmpty()) {
            return new ResponseEntity<>("Recipe Not Found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(recipe.get(), HttpStatus.FOUND);
    }

    public ResponseEntity<Object> updateTheRecipe(long id, HashMap<String, Object> map) {
        Optional<Recipe> oldRecipe = recipeRepository.findById(id);

        if(oldRecipe.isEmpty()) {
            return new ResponseEntity<>("Recipe Not Found", HttpStatus.NOT_FOUND);
        }

        if(map.containsKey("ingredients")) {
            Set<Ingredient> changedIngredients = new HashSet<>();
            ArrayList<Ingredient> arrayList = (ArrayList) map.get("ingredients");
            changedIngredients.addAll(arrayList);

            Set<Ingredient> verifiedIngredients = checkForOldIngredientsForPatch(changedIngredients);
            oldRecipe.get().setIngredients(verifiedIngredients);
        }

        map.forEach((key, value) -> {
            if(!key.equals("ingredients")) {
                Field field = ReflectionUtils.findField(Recipe.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, oldRecipe.get(), value);
            }
        });

        Recipe newRecipe = recipeRepository.save(oldRecipe.get());
        return new ResponseEntity<>(newRecipe, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<Object> deleteTheRecipe(long id) {
        Optional<Recipe> oldRecipe = recipeRepository.findById(id);

        if(oldRecipe.isEmpty()) {
            return new ResponseEntity<>("Recipe Not Found", HttpStatus.NOT_FOUND);
        }

        recipeRepository.deleteById(id);
        return new ResponseEntity<>("Recipe " + id + " deleted", HttpStatus.ACCEPTED);
    }

    public ResponseEntity<Object> listTheRecipes() {
        return new ResponseEntity<>(recipeRepository.findAll(), HttpStatus.OK);
    }


    private Set<Ingredient> checkForOldIngredientsForCreate(Set<Ingredient> ingredients) {
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

    private Set<Ingredient> checkForOldIngredientsForPatch(Set<Ingredient> ingredients) {
        // Had to break this into 2 methods because Jackson is mapping the ingredients to LinkedHashMap<Object, String>
        // Even though I take in a Set<Ingredients> it's not really true

        Set<Ingredient> newIngredients = new HashSet<>();

        Iterator iterator = ingredients.iterator();
        while(iterator.hasNext()) {
            LinkedHashMap<String, Object> shouldBeIngredient = (LinkedHashMap<String, Object>) iterator.next();
            Ingredient newIngredient = new Ingredient();
            newIngredient.setName((String) shouldBeIngredient.get("name"));
            newIngredient.setContainsGluten((boolean) shouldBeIngredient.get("containsGluten"));

            newIngredients.add(newIngredient);
        }

        for (Ingredient eachIngredient : newIngredients) {
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