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
    private static final String QUERY = "SELECT * FROM meal WHERE 1=1";

    private final Integer id;       //meal identificator
    private final Integer idUser;   //id of user who registered it
    private final Date date;        //day
    private final String meal;      //meal

    /**
     * Creates a new DAO object.
     *
     * @param con the connection to be used for accessing the database.
     */
    public GetMealDao(Connection con, Meal meal) {
        super(con);
        this.id=meal.getId();
        this.idUser= meal.getIdUte();
        this.date=meal.getDay();
        this.meal=meal.getMeal();
    }

    @Override
    protected void doAccess() throws Exception {
        PreparedStatement pstmt = null;
        String sql=QUERY;
        ResultSet rs = null;
        // the results of the search
        final List<Food> foodList = new ArrayList<>();

        if(id!=null)
            sql+=" and id = ? ";
        if(idUser!=null)
            sql+=" and id_user = ? ";
        if(date!=null)
            sql+=" and day = ? ";
        if(StringUtils.isNotBlank(meal.trim()))
            sql+=" and meal = ?";

        pstmt = con.prepareStatement(sql);
        int i = 1;

        if(id!=null)
            pstmt.setInt(i++, id);
        if(idUser!=null)
            pstmt.setInt(i++, idUser);
        if(date!=null)
            pstmt.setDate(i++, date);
        if(StringUtils.isNotBlank(meal.trim()))
            pstmt.setString(i++, meal.trim());
    }
}
