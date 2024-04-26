package it.unipd.dei.cyclek.dao.post;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for deleting a social network post from the database.
 * It extends the AbstractDAO class and overrides the doAccess method.
 *
 * @author Martina Boscolo Bacheto
 */
public class DeletePostDAO extends AbstractDAO<Post> {

    /**
     * SQL statement to delete a social network post from the database.
     */
    private static final String STATEMENT = "DELETE FROM posts WHERE id = ? RETURNING * ";

    /**
     * The post ID to be deleted.
     */
    private final int postId;

    /**
     * Constructs a new DeletePostDAO object with the given connection and post ID.
     *
     * @param con    the connection to the database.
     * @param postId the post ID to be deleted.
     * @throws IllegalArgumentException if the post ID is less than or equal to 0.
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
     * @throws SQLException if any SQL error occurs while deleting the post.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the deleted employee
        Post e = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                e = new Post(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getString("text_content"),
                        rs.getBytes("photo"),
                        rs.getString("photoMediaType"),
                        rs.getTimestamp("post_date"));

                LOGGER.info("Post %d successfully deleted from the database.", e.getPostId());
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
