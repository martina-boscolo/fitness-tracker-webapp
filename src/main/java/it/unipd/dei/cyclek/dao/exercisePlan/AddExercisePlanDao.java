package it.unipd.dei.cyclek.dao.exercisePlan;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.ExercisePlan;
import it.unipd.dei.cyclek.resources.entity.UserStats;
import org.postgresql.util.PGobject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddExercisePlanDao extends AbstractDAO<ExercisePlan> {

    public static final String STATEMENT = "INSERT INTO exercise_plan (idUser, planName, plan) VALUES (?, ?, ?) RETURNING *";
    private final ExercisePlan plan;

    public AddExercisePlanDao(Connection con, ExercisePlan plan) {
        super(con);
        this.plan = plan;
    }

    @Override
    protected final void doAccess() throws SQLException {
        ResultSet rs;
        ExercisePlan exerciseplan = null;
        try (PreparedStatement pstmt = con.prepareStatement(STATEMENT)) {
            pstmt.setInt(1, plan.getIdUser());
            pstmt.setString(2, plan.getPlanName());

            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(plan.getPlan());
            LOGGER.debug("JSON String obtained from plan.getPlan(): {}", jsonObject);
            pstmt.setObject(3, jsonObject);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                exerciseplan = new ExercisePlan(
                        rs.getInt("id"),
                        rs.getInt("idUser"),
                        rs.getString("planName"),
                        rs.getString("plan"),
                        rs.getString("planDate")
                );
                LOGGER.info("exercise plan added with id {}.", exerciseplan.getId());
            }
            this.outputParam = exerciseplan;
            LOGGER.info("ExercisePlan successfully saved.");
        }
    }
}

