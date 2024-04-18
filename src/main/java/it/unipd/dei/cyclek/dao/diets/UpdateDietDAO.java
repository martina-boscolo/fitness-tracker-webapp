package it.unipd.dei.cyclek.dao.diets;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Diet;
import org.postgresql.util.PGobject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateDietDAO extends AbstractDAO<Boolean> {

    public static final String QUERY = "UPDATE dietplans SET planName = ?, diet = ? WHERE idUser = ?";

    private final Diet diet;

    public UpdateDietDAO(Connection con, Diet diet) {
        super(con);
        this.diet = diet;
    }

    @Override
    protected final void doAccess() throws SQLException {

        try (PreparedStatement pstmt = con.prepareStatement(QUERY)) {
            pstmt.setString(1, diet.getPlanName());


            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(diet.getDiet());
            pstmt.setObject(2, jsonObject);
            pstmt.setInt(3, diet.getIdUser());


            int rowsAffected = pstmt.executeUpdate();

            this.outputParam = rowsAffected > 0;
        }
    }
}