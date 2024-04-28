package it.unipd.dei.cyclek.dao.exercisePlan;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.ExercisePlan;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetExercisePlanByUserIdDao extends AbstractDAO<List<ExercisePlan>> {
    public static final String STATEMENT = "SELECT * FROM exercise_plan WHERE iduser = ?";

    private final Integer idUser;

    public GetExercisePlanByUserIdDao(final Connection con, Integer idUser) {
        super(con);
        this.idUser = idUser;
    }

    @Override
    protected final void doAccess() throws SQLException {


        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search

        List<ExercisePlan> exerciseplans = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, idUser);

            rs = pstmt.executeQuery();

            while (rs.next()) {
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