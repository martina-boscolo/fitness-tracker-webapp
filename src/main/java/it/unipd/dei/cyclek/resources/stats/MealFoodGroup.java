package it.unipd.dei.cyclek.resources.stats;

import it.unipd.dei.cyclek.resources.entity.Food;
import it.unipd.dei.cyclek.resources.entity.Meal;

import java.util.List;

public class MealFoodGroup {
    private final List<Meal> meals;
    private final List<Food> foods;

    public MealFoodGroup(List<Meal> meals, List<Food> foods) {
        this.meals = meals;
        this.foods = foods;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public List<Food> getFoods() {
        return foods;
    }
}
