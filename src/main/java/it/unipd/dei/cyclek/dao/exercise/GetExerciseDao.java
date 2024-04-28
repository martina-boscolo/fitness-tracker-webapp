package it.unipd.dei.cyclek.dao.exercise;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Exercise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetExerciseDao extends AbstractDAO<Exercise> {
    private static final String STATEMENT = "SELECT * FROM exercises WHERE id = ?";

    private final Integer id;

    public GetExerciseDao(final Connection con, Integer id) {
        super(con);
        this.id = id;
    }

    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
//        final List<Exercise> exercises = new ArrayList<>();
        Exercise exercise = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                exercise =
                        new Exercise(
                                rs.getInt("id"),
                                rs.getInt("id_category"),
                                rs.getString("exercise_name"),
                                rs.getString("description"),
                                rs.getString("exercise_equipment")
                        );
            }

            LOGGER.info("Exercise successfully fetched.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        outputParam = exercise;
    }

}
