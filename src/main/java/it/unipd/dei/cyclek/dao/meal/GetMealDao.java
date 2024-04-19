package it.unipd.dei.cyclek.dao.meal;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Food;
import it.unipd.dei.cyclek.resources.Meal;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class GetMealDao extends AbstractDAO<List<Meal>> {
    private static final String QUERY="SELECT * FROM meal WHERE 1=1";

    private final Integer id;               //meal identificator
    private final Integer id_user;          //id of user who registered it
    private final Integer id_food;          //id of food registered
    private final Date date;                //day
    private final Integer meal_type;        //meal
    private final Integer grams;            //grams of food



    /**
     * Creates a new DAO object.
     *
     * @param con the connection to be used for accessing the database.
     */
    public GetMealDao(Connection con, Meal meal) {
        super(con);
        this.id=meal.getId();
        this.id_user= meal.getId_user();
        this.id_food=meal.getId_food();
        this.date=meal.getDate();
        this.meal_type=meal.getMeal_type();
        this.grams=meal.getGrams();
    }

    @Override
    protected void doAccess() throws Exception {
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        // the results of the search
        final List<Meal> mealList=new ArrayList<>();

        try {
            StringBuilder sb=new StringBuilder(QUERY);
            if (id != null)
                sb.append(" and id = ? ");
            if (id_user != null)
                sb.append(" and id_user = ? ");
            if (id_food != null)
                sb.append(" and id_food = ? ");
            if (date != null)
                sb.append(" and day = ? ");
            if (meal_type!=null)
                sb.append(" and meal_type = ?");
            if (grams!=null)
                sb.append(" and grams = ?");

            pstmt=con.prepareStatement(sb.toString());
            int i=1;

            if (id != null)
                pstmt.setInt(i++, id);
            if (id_user != null)
                pstmt.setInt(i++, id_user);
            if (id_food != null)
                pstmt.setInt(i++, id_food);
            if (date != null)
                pstmt.setDate(i++, date);
            if (meal_type!=null)
                pstmt.setInt(i++, meal_type);
            if (grams!=null)
                pstmt.setInt(i++, grams);

            rs=pstmt.executeQuery();
            while (rs.next())
            {
                mealList.add(new Meal(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_food"),
                        rs.getDate("date"),
                        rs.getInt("meal_type"),
                        rs.getInt("grams")
                ));
            }

            LOGGER.info("Users successfully fetched.");
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }
        this.outputParam=mealList;
    }
}
