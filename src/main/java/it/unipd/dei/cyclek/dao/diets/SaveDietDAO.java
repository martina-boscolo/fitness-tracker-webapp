package it.unipd.dei.cyclek.dao.diets;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Diet;
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

        try (PreparedStatement pstmt = con.prepareStatement(QUERY)) {

            pstmt.setInt(1, diet.getIdUser());
            pstmt.setString(2, diet.getPlanName());

            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(diet.getDiet());
            pstmt.setObject(3, jsonObject);

            int rowsAffected = pstmt.executeUpdate();

            this.outputParam = rowsAffected > 0;

            LOGGER.info("Diet successfully saved. ");
        }
    }
}

