package it.unipd.dei.cyclek.dao.post;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for retrieving a social network post from the database.
 * It extends the AbstractDAO class and overrides the doAccess method.
 *
 * @author Martina Boscolo Bacheto
 */

public class GetPostDAO extends AbstractDAO<Post> {

    /**
     * SQL statement to retrieve a social network post from the database.
     */
    private static final String STATEMENT = "SELECT * FROM posts WHERE id = ? ";
    /**
     * The post ID to be retrieved.
     */
    private final int postId;

    /**
     * Constructs a new GetPostDAO object with the given connection and post ID.
     *
     * @param con    the connection to the database.
     * @param postId the post ID to be retrieved.
     */
    public GetPostDAO(final Connection con, final int postId) {
        super(con);
        this.postId = postId;
    }

    /**
     * Retrieves the social network post from the database.
     *
     * @throws SQLException if any SQL error occurs while retrieving the post.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Post resource = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, postId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                resource = new Post(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getString("text_content"),
                        rs.getString("image_path"),
                        rs.getTimestamp("post_date")

                );
                LOGGER.info("Post %d successfully read from the database.", resource.getPostId());

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
