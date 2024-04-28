package it.unipd.dei.cyclek.dao.diets;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Diet;
import org.postgresql.util.PGobject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateDietDAO extends AbstractDAO<Boolean> {

    public static final String QUERY = "UPDATE dietplans SET planName = ?, diet = ? WHERE id = ? AND dietDate > NOW() - INTERVAL '24 hours'";

    private final Diet diet;

    public UpdateDietDAO(Connection con, Diet diet) {
        super(con);
        this.diet = diet;
    }

    @Override
    protected final void doAccess() throws SQLException {

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, diet.getPlanName());


            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(diet.getDiet());
            stmt.setObject(2, jsonObject);
            stmt.setInt(3, diet.getId());


            int rowsAffected = stmt.executeUpdate();

            this.outputParam = rowsAffected > 0;
        }
    }
}