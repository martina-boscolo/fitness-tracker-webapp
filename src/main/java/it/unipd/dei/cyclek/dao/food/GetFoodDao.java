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
    private final Integer kcal;       //number of Calories for 100g of food
    private final Integer fats;       //grams of fats for 100g of food
    private final Integer carbs;      //grams of carbs for 100g of food
    private final Integer prot;       //grams of prot for 100g of food
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
        StringBuilder sql= new StringBuilder(QUERY);
        ResultSet rs = null;
        // the results of the search
        final List<Food> foodList = new ArrayList<>();
        try {
            if (id != null)
                sql.append(" and id = ").append(id);
            if (StringUtils.isNotBlank(fdnm) && StringUtils.isNotBlank(fdnm.trim()))
                sql.append(" and fdnm = ").append(fdnm.trim());
            if (kcal!=null)
                sql.append(" and kcal = ").append(kcal);
            if (fats != null)
                sql.append(" and fats = ").append(fats);
            if (carbs != null)
                sql.append(" and carbohydrates = ").append(carbs);
            if(prot != null)
                sql.append(" and proteins = ").append(prot);
            pstmt = con.prepareStatement(sql.toString());

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                foodList.add(new Food(
                        (Integer)rs.getInt("id"),
                        rs.getString("fdnm"),
                        (Integer)rs.getInt("kcal"),
                        (Integer) rs.getInt("fats"),
                        (Integer)rs.getInt("carbohydrates"),
                        (Integer)rs.getInt("proteins")
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
