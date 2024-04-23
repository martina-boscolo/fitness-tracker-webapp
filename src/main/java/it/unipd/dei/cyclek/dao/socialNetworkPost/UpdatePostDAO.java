package it.unipd.dei.cyclek.dao.socialNetworkPost;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for updating a social network post in the database.
 * It extends the AbstractDAO class and overrides the doAccess method.
 *
 * @author Martina Boscolo Bacheto
 */
public class UpdatePostDAO extends AbstractDAO<Post> {

    /**
     * SQL statement to update a social network post in the database.
     */
    private static final String STATEMENT = "UPDATE posts SET text_content = ?, image_path = ? , like_count = ?, comment_count = ? WHERE id = ? RETURNING *";

    /**
     * The post to be updated.
     */
    private final Post post;

    /**
     * Constructs a new UpdatePostDAO object with the given connection and post.
     *
     * @param con  the connection to the database.
     * @param post the post to be updated.
     */
    public UpdatePostDAO(Connection con, final Post post) {
        super(con);
        if (post == null) {
            LOGGER.error("The post cannot be null.");
            throw new NullPointerException("The post cannot be null.");
        }

        this.post = post;

    }

    /**
     * Updates the social network post in the database.
     *
     * @throws SQLException if any SQL error occurs while updating the post.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Post resource = null;

        try {

            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, post.getTextContent());
            pstmt.setString(2, post.getImagePath());
            pstmt.setInt(3, post.getLikeCount());
            pstmt.setInt(4, post.getCommentCount());
            pstmt.setInt(5, post.getPostId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                resource = new Post(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getString("text_content"),
                        rs.getString("image_path"),
                        rs.getInt("like_count"),
                        rs.getInt("comment_count"),
                        rs.getTimestamp("post_date")

                );
                LOGGER.info("Post %d successfully updated in the database.", resource.getPostId());

            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        outputParam = resource;
    }
}
