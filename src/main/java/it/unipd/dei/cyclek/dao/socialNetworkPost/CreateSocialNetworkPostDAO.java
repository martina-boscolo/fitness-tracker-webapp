package it.unipd.dei.cyclek.dao.socialNetworkPost;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.SocialNetworkPost;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * This class handles the creation of a new social network post in the database.
 *
 * @author Martina Boscolo Bacheto
 *
 */
public class CreateSocialNetworkPostDAO extends AbstractDAO<SocialNetworkPost> {

    private static final String STATEMENT = "INSERT INTO posts ( id_user, text_content, image_path, like_count, comment_count, post_date) VALUES ( ?, ?, ?, ?, ?, ?) RETURNING * ";
    private final SocialNetworkPost socialNetworkPost;

    /**
     * Constructs a new CreateSocialNetworkPost with the given database connection and social network post.
     *
     * @param con the database connection
     * @param socialNetworkPost the social network post to be created
     */
    public CreateSocialNetworkPostDAO(Connection con, SocialNetworkPost socialNetworkPost) {
        super(con);
        if (socialNetworkPost == null) {
            throw new IllegalArgumentException("Social network post must not be null.");
        }

        this.socialNetworkPost = socialNetworkPost;
    }

    /**
     * Executes the SQL statement to create a new social network post in the database.
     *
     * @throws SQLException if any error occurs while accessing the database
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        SocialNetworkPost e = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, socialNetworkPost.getUserId());
            pstmt.setString(2, socialNetworkPost.getTextContent());
            pstmt.setString(3, socialNetworkPost.getImagePath());
            pstmt.setInt(4, socialNetworkPost.getLikeCount());
            pstmt.setInt(5, socialNetworkPost.getCommentCount());
            pstmt.setTimestamp(6, java.sql.Timestamp.valueOf(LocalDateTime.now()));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                e = new SocialNetworkPost(rs.getInt("id"), rs.getInt("id_user"), rs.getString("text_content"),
                        rs.getString("image_path"), rs.getInt("like_count"), rs.getInt("comment_count"),
                        rs.getTimestamp("post_date"));
                LOGGER.info("Post %d successfully stored in the database.", e.getPostId());
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