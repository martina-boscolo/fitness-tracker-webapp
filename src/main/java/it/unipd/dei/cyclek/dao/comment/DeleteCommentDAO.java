package it.unipd.dei.cyclek.dao.comment;

import it.unipd.dei.cyclek.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class is responsible for deleting a comment from the database.
 * It extends the AbstractDAO class and overrides the doAccess method.
 *
 * @author Martina Boscolo Bacheto
 *
 */
public class DeleteCommentDAO extends AbstractDAO {

    /**
     * SQL statement to delete a comment from the database.
     */
    private static final String STATEMENT = "DELETE FROM cyclek.public.comments WHERE id = ? ";

    /**
     * The comment ID to be deleted.
     */
    private final int commentId;

    /**
     * Constructs a new DeleteCommentDAO object with the given connection and comment ID.
     *
     * @param con the connection to the database.
     * @param commentId the comment ID to be deleted.
     * @throws IllegalArgumentException if the comment ID is less than or equal to 0.
     */
    public DeleteCommentDAO(Connection con, int commentId) {
        super(con);
        if (commentId <= 0 ) {
            throw new IllegalArgumentException("commentId must be greater than 0.");
        }

        this.commentId = commentId;
    }

    /**
     * Deletes the comment from the database.
     *
     * @throws SQLException if any SQL error occurs while deleting the comment.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, commentId);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }
}
