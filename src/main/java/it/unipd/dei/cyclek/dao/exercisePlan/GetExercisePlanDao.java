package it.unipd.dei.cyclek.dao.exercisePlan;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.ExercisePlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetExercisePlanDao extends AbstractDAO<ExercisePlan> {
    public static final String STATEMENT = "SELECT * FROM exercise_plan WHERE id = ?";

private final Integer id;

    public GetExercisePlanDao(final Connection con, Integer id) {
        super(con);
        this.id = id;
    }

    @Override
    protected final void doAccess() throws SQLException {


        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
//        final List<Exercise> exercises = new ArrayList<>();
        ExercisePlan exerciseplans = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            while (rs.next())
            {

                exerciseplans = new ExercisePlan(
                        rs.getInt("id"),
                        rs.getInt("idUser"),
                        rs.getString("planName"),
                        rs.getString("plan"),
                        rs.getString("planDate")
                );
            }

            LOGGER.info("ExercisePlan successfully fetched.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = exerciseplans;
    }
}