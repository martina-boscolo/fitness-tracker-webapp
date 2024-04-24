package it.unipd.dei.cyclek.dao.exercisePlan;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.ExercisePlan;
import org.postgresql.util.PGobject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateExercisePlanDao extends AbstractDAO<Boolean> {
    public static final String STATEMENT = "UPDATE exercise_plan SET planName = ?, plan = ? WHERE id = ? AND planDate > NOW() - INTERVAL '24 hours'";

    private final ExercisePlan plan;

    public UpdateExercisePlanDao(Connection con, ExercisePlan plan) {
        super(con);
        this.plan = plan;
    }

    @Override
    protected final void doAccess() throws SQLException {

        try (PreparedStatement stmt = con.prepareStatement(STATEMENT)) {
            stmt.setString(1, plan.getPlanName());

            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(plan.getPlan());
            stmt.setObject(2, jsonObject);
            stmt.setInt(3, plan.getId());


            int rowsAffected = stmt.executeUpdate();

            this.outputParam = rowsAffected > 0;
        }
    }
}

