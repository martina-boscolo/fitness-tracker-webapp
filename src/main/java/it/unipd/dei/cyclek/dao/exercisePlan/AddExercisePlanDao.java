package it.unipd.dei.cyclek.dao.exercisePlan;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.ExercisePlan;
import org.postgresql.util.PGobject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddExercisePlanDao extends AbstractDAO<Boolean> {

    public static final String STATEMENT = "INSERT INTO exercise_plan (idUser, planName, plan) VALUES (?, ?, ?)";
    private final ExercisePlan plan;
    public AddExercisePlanDao(Connection con, ExercisePlan plan) {
        super(con);
        this.plan = plan;
    }
    @Override
    protected final void doAccess() throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(STATEMENT)) {
            pstmt.setInt(1, plan.getIdUser());
            pstmt.setString(2, plan.getPlanName());

            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(plan.getPlan());
            LOGGER.debug("JSON String obtained from plan.getPlan(): {}", jsonObject);
            pstmt.setObject(3, jsonObject);
            int rowsAffected = pstmt.executeUpdate();
            // If rowsAffected > 0, exercisePlan was saved successfully
            this.outputParam = rowsAffected > 0;
            LOGGER.info("ExercisePlan successfully saved.");
        }
    }
}

