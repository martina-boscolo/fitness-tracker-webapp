package it.unipd.dei.cyclek.rest.userStats;

import it.unipd.dei.cyclek.dao.userStats.GetMealsAndFoodByUserIdDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetMealsByUserIdRR extends AbstractRR {

    public GetMealsByUserIdRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.GET_MEALS_BY_IDUSER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        MealFoodGroup mealFoodGroup = null;
        Message m = null;

        try {

            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("user") + 4);
            final int idUser = Integer.parseInt(path.substring(1));

            // creates a new DAO for accessing the database and lists the employee(s)
            mealFoodGroup = new GetMealsAndFoodByUserIdDAO(con, idUser).access().getOutputParam();

            List<Meal> meals = mealFoodGroup.getMeals();
            if (meals == null) {
                LOGGER.error("No stats found for searched user.");
                m = ErrorCode.GET_MEALS_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.GET_MEALS_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }

            List<Food> foods = mealFoodGroup.getFoods();
            Map<Integer,Food> foodsMap = new HashMap<>();
            for (Food food : foods)
                foodsMap.put(food.getId(),food);

            LOGGER.info("Meals successfully listed.");

            Integer userId = meals.get(0).getIdUser();
            double avg_kcal = 0.0;
            double avg_fats = 0.0;
            double avg_carb = 0.0;
            double avg_prot = 0.0;
            Map<Integer,Integer> favourite = new HashMap<>();
            for (Meal meal : meals) {
                String date = meal.getDate();
                Integer type = meal.getMealType();
                List<MealAdapter.FoodIntake> intake_list = new MealAdapter(meal.getMeal()).fromJSON();
                double avg_meal_kcal = 0.0;
                double avg_meal_fats = 0.0;
                double avg_meal_carb = 0.0;
                double avg_meal_prot = 0.0;
                for (MealAdapter.FoodIntake fi : intake_list) {
                    avg_meal_kcal += fi.getQty() * foodsMap.get(fi.getIdFood()).getKcal();
                    avg_meal_fats += fi.getQty() * foodsMap.get(fi.getIdFood()).getFats();
                    avg_meal_carb += fi.getQty() * foodsMap.get(fi.getIdFood()).getCarbs();
                    avg_meal_prot += fi.getQty() * foodsMap.get(fi.getIdFood()).getProt();
                    favourite.put(fi.getIdFood(),favourite.getOrDefault(fi.getIdFood(),0)+1);
                }
                avg_kcal += avg_meal_kcal / intake_list.size();
                avg_fats += avg_meal_fats / intake_list.size();
                avg_carb += avg_meal_carb / intake_list.size();
                avg_prot += avg_meal_prot / intake_list.size();
            }
            avg_kcal /= meals.size();
            avg_fats /= meals.size();
            avg_carb /= meals.size();
            avg_prot /= meals.size();
            String favoriteFoodName = "";
            int favoriteFoodCount = 0;
            for (Map.Entry<Integer, Integer> entry : favourite.entrySet()) {
                if (entry.getValue() > favoriteFoodCount) {
                    favoriteFoodCount = entry.getValue();
                    favoriteFoodName = foodsMap.get(entry.getKey()).getFdnm();
                }
            }

            res.setStatus(HttpServletResponse.SC_OK);
            new FoodStats(avg_kcal,avg_fats,avg_carb,avg_prot,favoriteFoodName,favoriteFoodCount).toJSON(res.getOutputStream());

        } catch (SQLException ex) {
            LOGGER.error("Cannot list Body Stats: unexpected database error.", ex);
            m = ErrorCode.GET_MEALS_FOOD_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_MEALS_FOOD_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}