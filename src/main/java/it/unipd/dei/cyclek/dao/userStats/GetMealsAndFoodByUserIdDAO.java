package it.unipd.dei.cyclek.dao.userStats;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Food;
import it.unipd.dei.cyclek.resources.Meal;
import it.unipd.dei.cyclek.resources.MealFoodGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class GetMealsAndFoodByUserIdDAO extends AbstractDAO<MealFoodGroup> {

    private static final String QUERY = "SELECT * FROM meal WHERE id_user = ? order by meal_date desc";
    private static final String QUERY1 = "SELECT * FROM foods";

    private final Integer idUser;

    public GetMealsAndFoodByUserIdDAO(Connection con, Integer idUser) {
        super(con);
        this.idUser = idUser;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt_meals = null;
        PreparedStatement pstmt_foods = null;
        ResultSet rs = null;
        // the results of the search
        final List<Meal> ml = new ArrayList<>();
        final List<Food> fl = new ArrayList<>();

        try {

            pstmt_meals = con.prepareStatement(QUERY);
            pstmt_meals.setInt(1, this.idUser);
            rs = pstmt_meals.executeQuery();

            while (rs.next())
            {
                ml.add(new Meal(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getString("meal_date"),
                        rs.getInt("meal_type"),
                        rs.getString("meal")
                ));
            }

            pstmt_foods = con.prepareStatement(QUERY1);
            rs = pstmt_foods.executeQuery();
            while (rs.next())
            {
                fl.add(new Food(
                        rs.getInt("id"),
                        rs.getString("fdnm"),
                        rs.getInt("kcal"),
                        rs.getInt("fats"),
                        rs.getInt("carbohydrates"),
                        rs.getInt("proteins")
                ));
            }

            LOGGER.info("Body Stats successfully fetched.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt_meals != null) {
                pstmt_meals.close();
            }

            if (pstmt_foods != null) {
                pstmt_foods.close();
            }

        }

        this.outputParam = new MealFoodGroup(ml,fl);
    }
}
