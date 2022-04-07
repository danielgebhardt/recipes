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

        //recipeRepository.save(recipe);

        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        String json = objectMapper.writeValueAsString(recipe);

        this.mvc.perform(post("/recipes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.description", is("New Recipe")));
    }
}
