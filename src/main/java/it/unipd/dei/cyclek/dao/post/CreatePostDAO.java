package it.unipd.dei.cyclek.dao.post;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Post;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * This class handles the creation of a new post in the database.
 *
 * @author Martina Boscolo Bacheto
 */
public class CreatePostDAO extends AbstractDAO<Post> {

    private static final String STATEMENT = "INSERT INTO posts ( id_user, text_content, photo, photoMediaType, post_date) VALUES ( ?, ?, ?,?, ?) RETURNING * ";
    private final Post post;

    /**
     * Creates a new object for creating a new social network post in the database.
     *
     * @param con  the connection to the database.
     * @param post the social network post to be created in the database.
     * @throws IllegalArgumentException if post is null.
     */
    public CreatePostDAO(Connection con, Post post) {
        super(con);
        if (post == null) {
            throw new IllegalArgumentException("Post must not be null.");
        }

        this.post = post;
    }

    /**
     * Stores the social network post in the database.
     *
     * @throws SQLException if any error occurs while storing the social network post in the database.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Post post = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, this.post.getUserId());
            pstmt.setString(2, this.post.getTextContent());
            if (this.post.getPhoto() != null && this.post.getPhotoMediaType() != null) {
                pstmt.setBytes(3, this.post.getPhoto());
                pstmt.setString(4, this.post.getPhotoMediaType());
            } else {
                pstmt.setNull(3, Types.BINARY);
                pstmt.setNull(4, Types.VARCHAR);
            }
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                post = new Post(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getString("text_content"),
                        rs.getBytes("photo"),
                        rs.getString("photoMediaType"),
                        rs.getTimestamp("post_date"),
                        "username",
                        rs.getInt("likes_count"),
                        rs.getInt("comments_count")
                );

                LOGGER.info("Post %s successfully stored in the database.", post.getPostId());
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