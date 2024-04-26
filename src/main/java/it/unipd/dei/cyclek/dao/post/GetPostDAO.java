package it.unipd.dei.cyclek.dao.post;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for retrieving a post from the database.
 *
 * @author Martina Boscolo Bacheto
 */

public class GetPostDAO extends AbstractDAO<Post> {

    private static final String STATEMENT = "SELECT * FROM posts WHERE id = ? ";
    /**
     * The post ID to be retrieved.
     */
    private final int postId;

    /**
     * Creates a new object for retrieving a social network post from the database.
     *
     * @param con    the connection to the database.
     * @param postId the ID of the post to be retrieved.
     * @throws IllegalArgumentException if postId is less than or equal to 0.
     */
    public GetPostDAO(final Connection con, final int postId) {
        super(con);
        this.postId = postId;
    }

    /**
     * Retrieves the social network post from the database.
     *
     * @throws SQLException if any error occurs while retrieving the social network post from the database.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Post post = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, postId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                post = new Post(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getString("text_content"),
                        rs.getBytes("photo"),
                        rs.getString("photoMediaType"),
                        rs.getTimestamp("post_date")

                );
                LOGGER.info("Post {} successfully read from the database.", post.getPostId());

            }

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
        outputParam = post;
    }
}
