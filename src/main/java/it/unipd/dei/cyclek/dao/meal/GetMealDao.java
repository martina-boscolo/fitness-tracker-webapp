package it.unipd.dei.cyclek.dao.meal;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Meal;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GetMealDao extends AbstractDAO<List<Meal>> {
    /**
     * Creates a new DAO object.
     *
     * @param con the connection to be used for accessing the database.
     */
    private static final String QUERY = "SELECT * FROM meal WHERE 1=1";

    private final Meal meal;


    /**
     * Creates a new DAO object.
     *
     * @param con the connection to be used for accessing the database.
     */
    public GetMealDao(Connection con, Meal meal) {
        super(con);
        this.meal = meal;
    }

    @Override
    protected void doAccess() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        final List<Meal> mealList = new ArrayList<>();

        try {
            StringBuilder sb = new StringBuilder(QUERY);
            if (meal.getId() != null)
                sb.append(" and id = ").append(meal.getId());
            if (meal.getIdUser() != null)
                sb.append(" and id_user = ").append(meal.getIdUser());
            if (StringUtils.isNotBlank(meal.getDate()))
                sb.append(" and meal_date = '").append(meal.getDate()).append("' ");
            if (meal.getMealType() != null)
                sb.append(" and meal_type = ").append(meal.getMealType());
            if (StringUtils.isNotBlank(meal.getMeal()))
                sb.append(" and meal = '").append(meal.getMeal()).append("' ");

            pstmt = con.prepareStatement(sb.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                mealList.add(new Meal(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getString("meal_date"),
                        rs.getInt("meal_type"),
                        rs.getString("meal")
                ));
            }

            LOGGER.info("Meals successfully fetched.");
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }
        this.outputParam = mealList;
    }
}
