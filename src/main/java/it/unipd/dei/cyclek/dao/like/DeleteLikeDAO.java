package it.unipd.dei.cyclek.dao.like;

import it.unipd.dei.cyclek.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class is responsible for deleting a like from the database.
 * It extends the AbstractDAO class and overrides the doAccess method.
 *
 * @author Martina Boscolo Bacheto
 *
 */
public class DeleteLikeDAO extends AbstractDAO {

    /**
     * SQL statement to delete a like from the database.
     */
    private static final String STATEMENT = "DELETE FROM cyclek.public.likes WHERE id_user = ? AND id_post = ?";

    /**
     * The user ID of the like to be deleted.
     */
    private final int userId;

    /**
     * The post ID of the like to be deleted.
     */
    private final int postId;

    /**
     * Constructs a new DeleteLikeDAO object with the given connection, user ID and post ID.
     *
     * @param con the connection to the database.
     * @param userId the user ID of the like to be deleted.
     * @param postId the post ID of the like to be deleted.
     * @throws IllegalArgumentException if the user ID or post ID is less than or equal to 0.
     */
    public DeleteLikeDAO(Connection con, int userId, int postId) {
        super(con);
        if (userId <= 0 || postId <= 0) {
            throw new IllegalArgumentException("userId and postId must be greater than 0.");
        }

        this.userId = userId;
        this.postId = postId;
    }

    /**
     * Deletes the like from the database.
     *
     * @throws SQLException if any SQL error occurs while deleting the like.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, userId);
            pstmt.setInt(2, postId);

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }
}
