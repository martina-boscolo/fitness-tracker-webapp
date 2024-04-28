package it.unipd.dei.cyclek.dao.post;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for deleting a post from the database.
 *
 * @author Martina Boscolo Bacheto
 */
public class DeletePostDAO extends AbstractDAO<Post> {

    private static final String STATEMENT = "DELETE FROM posts WHERE id = ? RETURNING * ";

    private final int postId;

    /**
     * Creates a new object for deleting a social network post from the database.
     *
     * @param con    the connection to the database.
     * @param postId the ID of the post to be deleted.
     * @throws IllegalArgumentException if postId is less than or equal to 0.
     */
    public DeletePostDAO(Connection con, int postId) {
        super(con);
        if (postId <= 0) {
            throw new IllegalArgumentException("postId must be greater than 0.");
        }

        this.postId = postId;
    }

    /**
     * Deletes the social network post from the database.
     *
     * @throws SQLException if any error occurs while deleting the social network post from the database.
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
                post = new Post(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getString("text_content"),
                        rs.getBytes("photo"),
                        rs.getString("photoMediaType"),
                        rs.getTimestamp("post_date"));

                LOGGER.info("Post %s successfully deleted from the database.", post.getPostId());
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
