package it.unipd.dei.cyclek.dao.food;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Food;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.ResourceList;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

public final class GetFoodDao extends AbstractDAO<List<Food>> {
    /**
     * Creates a new DAO object.
     *
     * @param con the connection to be used for accessing the database.
     */
    private static final String QUERY = "SELECT * FROM foods WHERE 1=1";

    private final Integer id;         //food identificator
    private final String fdnm;    //food name
    private final int kcal;       //number of Calories for 100g of food
    private final int fats;       //grams of fats for 100g of food
    private final int carbs;      //grams of carbs for 100g of food
    private final int prot;       //grams of prot for 100g of food
    public GetFoodDao(Connection con, Food food) {
        super(con);
        this.id=food.getId();
        this.fdnm= food.getFdnm();
        this.kcal=food.getKcal();
        this.fats= food.getFats();
        this.carbs=food.getCarbs();
       this.prot=food.getProt();
    }
    @Override
    protected void doAccess() throws Exception {
        PreparedStatement pstmt = null;
        String sql=QUERY;
        ResultSet rs = null;
        // the results of the search
        final List<Food> foodList = new ArrayList<>();
        try {
            //parameterized queries prevents sql injection
            if (id != null)
                sql+=" and id =? ";
            if (StringUtils.isNotBlank(fdnm.trim()))
                sql+=" and fdnm =? ";
            if (kcal!=0)
                sql+=" and kcal =? ";
            if (fats != 0)
                sql+=" and fats =? ";
            if (carbs != 0)
                sql+=" and carbohydrates =? ";
            if(prot != 0)
                sql+=" and proteins =?";
            pstmt = con.prepareStatement(sql);
            int i = 1;
            if (id != null)
                pstmt.setInt(i++, id);
            if (StringUtils.isNotBlank(fdnm.trim()))
                pstmt.setString(i++, fdnm);
            if (kcal!=0)
                pstmt.setInt(i++, kcal);
            if (fats != 0)
                pstmt.setInt(i++, fats);
            if (carbs != 0)
                pstmt.setInt(i++, carbs);
            if(prot != 0)
                pstmt.setInt(i++, prot);
            rs = pstmt.executeQuery();
            while (rs.next())
            {
                foodList.add(new Food(
                        rs.getInt("id"),
                        rs.getString("fdnm"),
                        rs.getInt("kcal"),
                        rs.getInt("fats"),
                        rs.getInt("carbohydrates"),
                        rs.getInt("proteins")
                ));
            }
            LOGGER.info("Foods successfully fetched.");
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        this.outputParam = foodList;
    }
}
