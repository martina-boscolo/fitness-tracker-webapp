package it.unipd.dei.cyclek.dao.userExercise;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.UserExercise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddUserExerciseDao extends AbstractDAO<UserExercise> {

    private static final String STATEMENT = "INSERT INTO user_exercises (id_user, id_exercise, weight, repetition) VALUES (?, ?, ?, ?) RETURNING *";

    private final UserExercise userExercise;
    /**
     * Creates a new DAO object.
     *
     * @param con the connection to be used for accessing the database.
     */
    public AddUserExerciseDao(Connection con, UserExercise userExercise) {
        super(con);
        if(userExercise == null){
            LOGGER.error("The exercise of User can not be null!");
            throw new IllegalArgumentException("The exercise of User can not be null!");
        }

        this.userExercise = userExercise;
    }

    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UserExercise e = null;
        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, userExercise.getId_user());
            pstmt.setInt(2, userExercise.getId_exercise());
            pstmt.setInt(3, userExercise.getWeight());
            pstmt.setInt(4, userExercise.getRepetition());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                e = new UserExercise(
                        rs.getInt("id_user"),
                        rs.getInt("id_exercise"),
                        rs.getInt("weight"),
                        rs.getInt("repetition")
                );
                LOGGER.info("Exercise %d successfully stored in the database.", e.getId_exercise());
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        outputParam = e;
    }
}