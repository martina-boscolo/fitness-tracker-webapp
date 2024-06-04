package it.unipd.dei.cyclek.dao.exercisePlan;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.ExercisePlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteExercisePlanDao extends AbstractDAO<Boolean> {
    public static final String STATEMENT = "DELETE FROM exercise_plan WHERE id = ?";
    private final Integer id;

    public DeleteExercisePlanDao(final Connection con, Integer id) {
        super(con);
        this.id = id;
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Boolean result = false;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            result = affectedRows != 0;
            LOGGER.info("ExercisePlan successfully fetched.");
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

        this.outputParam = result;
    }
}
