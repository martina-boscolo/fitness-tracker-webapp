package it.unipd.dei.cyclek.dao.comment;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for deleting a comment from the database.
 *
 * @author Martina Boscolo Bacheto
 */
public class DeleteCommentDAO extends AbstractDAO<Comment> {

    /**
     * SQL statement to delete a comment from the database.
     */
    private static final String STATEMENT = "DELETE FROM comments WHERE id = ? RETURNING * ";
    private static final String STATEMENT_UPDATE = "UPDATE Posts SET comments_count = comments_count - 1 WHERE id = ?";

    /**
     * The comment ID to be deleted.
     */
    private final int commentId;

    /**
     * Constructs a new DeleteCommentDAO object with the given connection and comment ID.
     *
     * @param con       the connection to the database.
     * @param commentId the comment ID to be deleted.
     * @throws IllegalArgumentException if the comment ID is less than or equal to 0.
     */
    public DeleteCommentDAO(Connection con, int commentId) {
        super(con);
        if (commentId <= 0) {
            throw new IllegalArgumentException("commentId must be greater than 0.");
        }

        this.commentId = commentId;
    }

    /**
     * Deletes a comment from the database.
     *
     * @throws SQLException if an error occurs while executing the query.
     */
    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        Comment c = null;

        try {
            // Turn off auto-commit mode
            con.setAutoCommit(false);

            // Execute the first statement
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, commentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                c = new Comment(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_post"),
                        rs.getString("text_content"),
                        "username"
                );

                LOGGER.info("Comment %s successfully deleted from the database.", c.getCommentId());
            }

            // Execute the second statement
            pstmt2 = con.prepareStatement(STATEMENT_UPDATE);
            pstmt2.setInt(1, c.getPostId());
            pstmt2.executeUpdate();

            // If both statements are successful, commit the transaction
            con.commit();

        } catch (SQLException e) {
            // If any statement fails, rollback the transaction
            if (con != null) {
                try {
                    LOGGER.error("Transaction is being rolled back");
                    con.rollback();
                } catch (SQLException excep) {
                    LOGGER.error("Error occurred during transaction rollback");
                }
            }
        } finally {
            // Turn auto-commit mode back on
            if (con != null) {
                con.setAutoCommit(true);
            }

            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

            if (pstmt2 != null) {
                pstmt2.close();
            }
        }

        outputParam = c;
    }
}
