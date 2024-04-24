package it.unipd.dei.cyclek.dao.post;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Post;

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
public class CreatePostDAO extends AbstractDAO<Post> {

    private static final String STATEMENT = "INSERT INTO posts ( id_user, text_content, image_path, like_count, comment_count, post_date) VALUES ( ?, ?, ?, ?, ?, ?) RETURNING * ";
    private final Post post;

    /**
     * Constructs a new CreateSocialNetworkPost with the given database connection and social network post.
     *
     * @param con the database connection
     * @param post the social network post to be created
     */
    public CreatePostDAO(Connection con, Post post) {
        super(con);
        if (post == null) {
            throw new IllegalArgumentException("Social network post must not be null.");
        }

        this.post = post;
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

        Post e = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, post.getUserId());
            pstmt.setString(2, post.getTextContent());
            pstmt.setString(3, post.getImagePath());
            pstmt.setInt(4, post.getLikeCount());
            pstmt.setInt(5, post.getCommentCount());
            pstmt.setTimestamp(6, java.sql.Timestamp.valueOf(LocalDateTime.now()));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                e = new Post(rs.getInt("id"), rs.getInt("id_user"), rs.getString("text_content"),
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