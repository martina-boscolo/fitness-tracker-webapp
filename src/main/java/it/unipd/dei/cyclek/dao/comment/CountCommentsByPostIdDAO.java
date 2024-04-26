package it.unipd.dei.cyclek.dao.comment;

import it.unipd.dei.cyclek.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Count the number of comments for a given post.
 *
 * @author Martina Boscolo Bacheto
 */
public class CountCommentsByPostIdDAO extends AbstractDAO<Integer> {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT COUNT(*) FROM comments WHERE id_post = ?";

    /**
     * The post id
     */
    private final int postId;

    /**
     * Creates a new object for counting the number of comments for a given post.
     *
     * @param con    the connection to the database
     * @param postId the post id
     * @throws IllegalArgumentException if postId is less than or equal to 0
     */
    public CountCommentsByPostIdDAO(Connection con, int postId) {
        super(con);
        if (postId <= 0) {
            throw new IllegalArgumentException("postId must be greater than 0.");
        }

        this.postId = postId;
    }

    /**
     * Counts the number of comments for a given post.
     *
     * @return the number of comments for a given post
     * @throws SQLException if an error occurs while executing the query
     */
    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int commentCount = -1;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                commentCount = rs.getInt(1);
                LOGGER.info("Comments successfully counted for post {}.", postId);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        outputParam = commentCount;
    }
}
