package it.unipd.dei.cyclek.dao.meal;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Meal;
import org.postgresql.util.PGobject;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RegisterMealDAO extends AbstractDAO<Boolean> {

    public static final String QUERY = "INSERT INTO meal (id_user, meal_date, meal_type, meal) VALUES (?,?,?,?)";

    private final Meal meal;

    public RegisterMealDAO(Connection con, Meal meal) {
        super(con);
        this.meal = meal;
    }

    @Override
    protected final void doAccess() throws SQLException, ParseException {

        try (PreparedStatement pstmt = con.prepareStatement(QUERY)) {

            pstmt.setInt(1, meal.getIdUser());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
            LocalDate date = LocalDate.parse(meal.getDate(), formatter);
            pstmt.setDate(2, java.sql.Date.valueOf(date));

            pstmt.setInt(3, meal.getMealType());

            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(meal.getMeal());
            pstmt.setObject(4, jsonObject);

            int rowsAffected = pstmt.executeUpdate();

            this.outputParam = rowsAffected > 0;

            LOGGER.info("Diet successfully saved. ");
        }
    }
}

