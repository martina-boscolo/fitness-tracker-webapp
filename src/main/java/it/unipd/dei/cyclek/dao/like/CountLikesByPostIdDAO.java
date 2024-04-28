package it.unipd.dei.cyclek.dao.like;

import it.unipd.dei.cyclek.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Count the number of likes for a post.
 *
 * @author Martina Boscolo Bacheto
 */
public class CountLikesByPostIdDAO extends AbstractDAO<Integer> {

    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "SELECT COUNT(*) FROM likes WHERE id_post = ? ";

    /**
     * The id of the post.
     */
    private final int postId;

    /**
     * Creates a new object for counting the number of likes for a post.
     *
     * @param con    the connection to the database.
     * @param postId the id of the post.
     * @throws IllegalArgumentException if postId is less than or equal to 0.
     */
    public CountLikesByPostIdDAO(Connection con, int postId) {
        super(con);
        if (postId <= 0) {
            throw new IllegalArgumentException("postId must be greater than 0.");
        }

        this.postId = postId;
    }

    /**
     * Counts the number of likes for a post.
     *
     * @throws SQLException if any error occurs while counting the number of likes.
     */
    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        int likeCount = -1;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                likeCount = rs.getInt(1);
                LOGGER.info("Likes successfully counted for post %s.", postId);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        outputParam = likeCount;
    }
}
