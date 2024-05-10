package it.unipd.dei.cyclek.dao.userStats;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Exercise;
import it.unipd.dei.cyclek.resources.entity.ExercisePlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class GetExercisesAndPlansByUserIdDAO extends AbstractDAO<List<ExercisePlan>> {

    private static final String QUERY = "SELECT * FROM exercise_plan WHERE iduser = ? order by plandate desc";

    private final Integer idUser;

    public GetExercisesAndPlansByUserIdDAO(Connection con, Integer idUser) {
        super(con);
        this.idUser = idUser;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt_plans = null;
        PreparedStatement pstmt_exercises = null;
        ResultSet rs = null;
        // the results of the search
        final List<ExercisePlan> ml = new ArrayList<>();

        try {

            pstmt_plans = con.prepareStatement(QUERY);
            pstmt_plans.setInt(1, this.idUser);
            rs = pstmt_plans.executeQuery();

            while (rs.next())
            {
                ml.add(new ExercisePlan(
                        rs.getInt("id"),
                        rs.getInt("iduser"),
                        rs.getString("planname"),
                        rs.getString("plan"),
                        rs.getString("plandate")
                ));
            }

            LOGGER.info("Body Stats successfully fetched.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt_plans != null) {
                pstmt_plans.close();
            }

            if (pstmt_exercises != null) {
                pstmt_exercises.close();
            }

        }

        this.outputParam = ml;
    }
}
