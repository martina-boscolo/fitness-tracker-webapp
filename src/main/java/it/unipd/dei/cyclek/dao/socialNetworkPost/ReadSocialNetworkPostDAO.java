package it.unipd.dei.cyclek.dao.socialNetworkPost;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.SocialNetworkPost;

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

public class ReadSocialNetworkPostDAO extends AbstractDAO<SocialNetworkPost> {

    /**
     * SQL statement to retrieve a social network post from the database.
     */
    private static final String STATEMENT = "SELECT * FROM posts WHERE id = ? ";
    /**
     * The post ID to be retrieved.
     */
    private final int postId;

    /**
     * Constructs a new ReadSocialNetworkPostDAO object with the given connection and post ID.
     *
     * @param con    the connection to the database.
     * @param postId the post ID to be retrieved.
     */
    public ReadSocialNetworkPostDAO(final Connection con, final int postId) {
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

        SocialNetworkPost resource = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, postId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                resource = new SocialNetworkPost(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getString("text_content"),
                        rs.getString("image_path"),
                        rs.getInt("like_count"),
                        rs.getInt("comment_count"),
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
