package it.unipd.dei.cyclek.dao.comment;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Comment;

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

        ResultSet rs = null;

        Comment c = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, commentId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                c = new Comment(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_post"),
                        rs.getString("text_content")
                );

                LOGGER.info("Comment {} successfully deleted from the database.", c.getCommentId());
            }
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        outputParam = c;

    }
}
