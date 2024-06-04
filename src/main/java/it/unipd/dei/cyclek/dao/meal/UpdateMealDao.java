package it.unipd.dei.cyclek.dao.meal;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Meal;
import org.postgresql.util.PGobject;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class UpdateMealDao extends AbstractDAO<Boolean> {
    public static final String QUERY = "UPDATE meal \n" +
            "SET meal = ? \n" +
            "WHERE id_user = ? AND meal_type = ? AND meal_date = ?;\n";

    private final Meal meal;

    public UpdateMealDao(Connection con, Meal meal) {
        super(con);
        this.meal = meal;
    }

    @Override
    protected void doAccess() throws Exception {


        try (PreparedStatement stmnt = con.prepareStatement(QUERY)) {
            PGobject o = new PGobject();
            o.setType("json");
            o.setValue(meal.getMeal());
            stmnt.setObject(1, o);
            stmnt.setInt(2, meal.getIdUser());
            stmnt.setInt(3, meal.getMealType());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            stmnt.setDate(4, new Date(format.parse(meal.getDate()).getTime()));
            int rows = stmnt.executeUpdate();
            this.outputParam = rows > 0;
        }
    }
}
