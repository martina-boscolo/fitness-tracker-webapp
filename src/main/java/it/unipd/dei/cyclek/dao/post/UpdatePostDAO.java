package it.unipd.dei.cyclek.dao.post;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for updating a social network post in the database.
 * @author Martina Boscolo Bacheto
 */
public class UpdatePostDAO extends AbstractDAO<Post> {

    private static final String STATEMENT = "UPDATE posts SET text_content = ?  WHERE id = ? RETURNING *";

    private final Post post;

    /**
     * Creates a new object for updating a social network post in the database.
     *
     * @param con  the connection to the database.
     * @param post the post to be updated.
     * @throws NullPointerException if the post is null.
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
     * @throws SQLException if any error occurs while updating the social network post in the database.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Post resource = null;

        try {

            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, post.getTextContent());
            pstmt.setInt(2, post.getPostId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                resource = new Post(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getString("text_content"),
                        rs.getBytes("photo"),
                        rs.getString("photoMediaType"),
                        rs.getTimestamp("post_date")

                );
                LOGGER.info("Post {} successfully updated in the database.", resource.getPostId());

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
