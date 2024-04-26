package it.unipd.dei.cyclek.dao.exercise;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Exercise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ListAllExercisesDao extends AbstractDAO<List<Exercise>> {
    private final static String STATEMENT = "SELECT * FROM exercises;";

    public ListAllExercisesDao(final Connection con) {
        super(con);
    }


    @Override
    protected void doAccess() throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        final List<Exercise> exercises = new ArrayList<Exercise>();

        try {
            pstmt = con.prepareStatement(STATEMENT);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                exercises.add(
                        new Exercise(
                                rs.getInt("id"),
                                rs.getInt("id_category"),
                                rs.getString("exercise_name"),
                                rs.getString("description"),
                                rs.getString("exercise_equipment")

                        )
                );
            }

            LOGGER.info("Exercises successfully listed.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        outputParam = exercises;

    }
}
