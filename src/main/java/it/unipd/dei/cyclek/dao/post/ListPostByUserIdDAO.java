package it.unipd.dei.cyclek.dao.post;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for listing all the posts of a user from the database.
 * @author Martina Boscolo Bacheto
 */
public class ListPostByUserIdDAO extends AbstractDAO<List<Post>> {

    private static final String STATEMENT = "SELECT users.username, posts.*  FROM posts INNER JOIN users ON posts.id_user = users.id WHERE id_user = ? ORDER BY post_date DESC LIMIT 10";
    private final int userId;
    /**
     * Creates a new object for listing all the social network posts of a user from the database.
     *
     * @param con    the connection to the database.
     * @param userId the ID of the user whose posts are to be listed.
     * @throws IllegalArgumentException if userId is less than or equal to 0.
     */
    public ListPostByUserIdDAO(final Connection con, int userId) {
        super(con);
        if ( userId <= 0) {
            throw new IllegalArgumentException("userId must be greater than 0.");
        }
        this.userId = userId;
    }

    /**
     * Lists all the social network posts of a user from the database.
     *
     * @throws SQLException if any error occurs while listing the social network posts of a user from the database.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final List<Post> posts = new ArrayList<Post>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, userId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                posts.add(
                        new Post(
                                rs.getInt("id"),
                                rs.getInt("id_user"),
                                rs.getString("text_content"),
                                rs.getBytes("photo"),
                                rs.getString("photoMediaType"),
                                rs.getTimestamp("post_date"),
                                rs.getString("username"),
                                rs.getInt("likes_count"),
                                rs.getInt("comments_count")
                        )
                );
            }
            LOGGER.info("Posts successfully listed.");

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        outputParam = posts;
    }
}