package it.unipd.dei.cyclek.dao.exercisePlan;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.ExercisePlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListExercisePlanDao extends AbstractDAO<List<ExercisePlan>> {

    public static final String STATEMENT = "SELECT * FROM exercise_plan WHERE 1=1";
    private final ExercisePlan plan;
    public ListExercisePlanDao(Connection con, ExercisePlan plan){

        super(con);
        this.plan = plan;
    }
    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        final List<ExercisePlan> exerciseplans = new ArrayList<>();

        try {
            StringBuilder sb = new StringBuilder(STATEMENT);

            if (plan.getId() != null)
                sb.append(" and id = ").append(plan.getId());
            if (plan.getIdUser() != null)
                sb.append(" and idUser = ").append(plan.getIdUser());
            if (!plan.getPlanName().isEmpty())
                sb.append(" and planName = ").append(plan.getPlanName());
            if (plan.getPlan() != null)
                sb.append(" and plan = ").append(plan.getPlan());
            if (!plan.getPlanDate().isEmpty())
                sb.append(" and date = ").append(plan.getPlanDate());

            pstmt = con.prepareStatement(sb.toString());


            rs = pstmt.executeQuery();

            while (rs.next())
            {

                exerciseplans.add(new ExercisePlan(
                        rs.getInt("id"),
                        rs.getInt("idUser"),
                        rs.getString("planName"),
                        rs.getString("plan"),
                        rs.getString("planDate")
                ));
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