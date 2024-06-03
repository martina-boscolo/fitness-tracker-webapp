package it.unipd.dei.cyclek.dao.comment;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for creating a new comment in the database.
 *
 * @author Martina Boscolo Bacheto
 */
public class CreateCommentDAO extends AbstractDAO<Comment> {

    /**
     * SQL statement to insert a new comment into the database.
     */
    private static final String STATEMENT = "INSERT INTO comments (id_user, id_post, text_content) VALUES (?, ?, ?) RETURNING * ";
    private static final String STATEMENT_UPDATE = "UPDATE Posts SET comments_count = comments_count + 1 WHERE id = ?";

    /**
     * The comment to be inserted into the database.
     */
    private final Comment comment;

    /**
     * Creates a new object for creating a new comment in the database.
     *
     * @param con     the connection to the database
     * @param comment the comment to be inserted into the database
     * @throws IllegalArgumentException if comment is null
     */
    public CreateCommentDAO(Connection con, Comment comment) {
        super(con);
        if (comment == null) {
            throw new IllegalArgumentException("comment must not be null.");
        }

        this.comment = comment;
    }

    /**
     * Creates a new comment in the database.
     *
     * @throws SQLException if an error occurs while executing the query
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
            pstmt.setInt(1, comment.getUserId());
            pstmt.setInt(2, comment.getPostId());
            pstmt.setString(3, comment.getCommentText());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                c = new Comment(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_post"),
                        rs.getString("text_content"));
                LOGGER.info("Comment %s successfully stored in the database.", c.getCommentId());
            }

            // Execute the second statement
            pstmt2 = con.prepareStatement(STATEMENT_UPDATE);
            pstmt2.setInt(1, comment.getPostId());
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