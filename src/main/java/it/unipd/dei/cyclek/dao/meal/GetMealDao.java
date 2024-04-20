package it.unipd.dei.cyclek.dao.meal;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Food;
import it.unipd.dei.cyclek.resources.Meal;
import org.apache.commons.lang3.StringUtils;
import sun.jvm.hotspot.utilities.CStringUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class GetMealDao extends AbstractDAO<List<Meal>> {
    /**
     * Creates a new DAO object.
     *
     * @param con the connection to be used for accessing the database.
     */
    private static final String QUERY="SELECT * FROM meal WHERE 1=1";

    private final Integer id;               //meal identificator
    private final Integer id_user;          //id of user who registered it
    private final Date date;                //day
    private final Integer meal_type;        //meal type
    private final String meal;              //meal


    /**
     * Creates a new DAO object.
     *
     * @param con the connection to be used for accessing the database.
     */
    public GetMealDao(Connection con, Meal meal) {
        super(con);
        this.id=meal.getId();
        this.id_user= meal.getId_user();
        this.date=meal.getDate();
        this.meal_type=meal.getMeal_type();
        this.meal=meal.getMeal();
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
            if (date != null)
                sb.append(" and meal_date = ? ");
            if (meal_type!=null)
                sb.append(" and meal_type = ?");
            if (StringUtils.isNotBlank(meal))
                if(StringUtils.isNotBlank(meal.trim()))
                   sb.append(" and grams = ?");

            pstmt=con.prepareStatement(sb.toString());
            int i=1;

            if (id != null)
                pstmt.setInt(i++, id);
            if (id_user != null)
                pstmt.setInt(i++, id_user);
            if (date != null)
                pstmt.setDate(i++, date);
            if (meal_type!=null)
                pstmt.setInt(i++, meal_type);
            if (StringUtils.isNotBlank(meal))
                if(StringUtils.isNotBlank(meal.trim()))
                    pstmt.setString(i++, meal.trim());

            rs=pstmt.executeQuery();
            while (rs.next())
            {
                mealList.add(new Meal(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getDate("meal_date"),
                        rs.getInt("meal_type"),
                        rs.getString("meal")
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
