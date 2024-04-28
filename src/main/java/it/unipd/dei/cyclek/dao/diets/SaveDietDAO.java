package it.unipd.dei.cyclek.dao.diets;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Diet;
import org.postgresql.util.PGobject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveDietDAO extends AbstractDAO<Boolean> {

    public static final String QUERY = "INSERT INTO dietplans (idUser, planName, diet) VALUES (?, ?, ?)";

    private final Diet diet;

    public SaveDietDAO(Connection con, Diet diet) {
        super(con);
        this.diet = diet;
    }

    @Override
    protected final void doAccess() throws SQLException {

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {

            stmt.setInt(1, diet.getIdUser());
            stmt.setString(2, diet.getPlanName());

            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(diet.getDiet());
            stmt.setObject(3, jsonObject);

            int rowsAffected = stmt.executeUpdate();

            this.outputParam = rowsAffected > 0;

            LOGGER.info("Diet successfully saved. ");
        }
    }
}

