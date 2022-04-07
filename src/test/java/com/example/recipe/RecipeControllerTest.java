package com.example.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Test
    void canCreateRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setDescription("New Recipe");
        recipe.setCalories(100);
        recipe.setInstructions("Some Instructions");
        recipe.setTitle("Cake Recipe");
        recipe.setDateCreated(LocalDate.now());

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Carrot");
        ingredient1.setContainsGluten(true);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Potato");
        ingredient2.setContainsGluten(false);

        Set<Ingredient> ingredientSet = new HashSet<>();
        ingredientSet.add(ingredient1);
        ingredientSet.add(ingredient2);

        recipe.setIngredients(ingredientSet);

        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        String json = objectMapper.writeValueAsString(recipe);

        this.mvc.perform(post("/recipes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.description", is("New Recipe")));
    }

    @Test
    @Transactional
    @Rollback
    void canReadRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setDescription("New Recipe");
        recipe.setCalories(100);
        recipe.setInstructions("Some Instructions");
        recipe.setTitle("Cake Recipe");
        recipe.setDateCreated(LocalDate.now());

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Carrot");
        ingredient1.setContainsGluten(true);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Potato");
        ingredient2.setContainsGluten(false);

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);

        Recipe newRecipe = recipeRepository.save(recipe);
        int newRecipeId = (int) newRecipe.getId();

        this.mvc.perform(get("/recipes/" + newRecipeId))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id", is(newRecipeId)))
                .andExpect(jsonPath("$.ingredients.[*].name", containsInAnyOrder("Carrot", "Potato")))
                .andExpect(jsonPath("$.description", is("New Recipe")));
    }

    @Test
    @Transactional
    @Rollback
    void canUpdateRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setDescription("New Recipe");
        recipe.setCalories(100);
        recipe.setInstructions("Some Instructions");
        recipe.setTitle("Cake Recipe");
        recipe.setDateCreated(LocalDate.now());

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Carrot");
        ingredient1.setContainsGluten(true);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Potato");
        ingredient2.setContainsGluten(false);

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);

        Recipe newRecipe = recipeRepository.save(recipe);
        int newRecipeId = (int) newRecipe.getId();

        this.mvc.perform(patch("/recipes/" + newRecipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "description": "Fruit Cake",
                                    "instructions": "Some Instructions",
                                    "title": "Cake Recipe",
                                    "calories": 100,
                                    "ingredients": [
                                                    {
                                                        "id": 0,
                                                        "name": "Corn",
                                                        "containsGluten": true
                                                    },
                                                    {
                                                        "id": 0,
                                                        "name": "Flour",
                                                        "containsGluten": true
                                                    }
                                                ]
                                }               
                                    """
                ))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(newRecipeId)))
                .andExpect(jsonPath("$.ingredients.[*].name", containsInAnyOrder("Corn", "Flour")))
                .andExpect(jsonPath("$.description", is("Fruit Cake")));
    }

    @Test
    @Transactional
    @Rollback
    void canDeleteRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setDescription("New Recipe");
        recipe.setCalories(100);
        recipe.setInstructions("Some Instructions");
        recipe.setTitle("Cake Recipe");
        recipe.setDateCreated(LocalDate.now());

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Carrot");
        ingredient1.setContainsGluten(true);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Potato");
        ingredient2.setContainsGluten(false);

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);

        Recipe newRecipe = recipeRepository.save(recipe);
        int newRecipeId = (int) newRecipe.getId();

        this.mvc.perform(delete("/recipes/" + newRecipeId))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Recipe " + newRecipeId + " deleted"));
    }
}
