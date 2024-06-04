package it.unipd.dei.cyclek.dao.comment;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * List all comments for a given post.
 *
 * @author Martina Boscolo Bacheto
 */
public class ListCommentsByPostIdDAO extends AbstractDAO<List<Comment>> {
    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT users.username , comments.* FROM comments INNER JOIN users ON comments.id_user = users.id  WHERE comments.id_post = ?";

    /**
     * The post id
     */
    private final int postId;

    /**
     * Creates a new object for listing all comments for a given post.
     *
     * @param con    the connection to the database
     * @param postId the post id
     * @throws IllegalArgumentException if postId is less than or equal to 0
     */
    public ListCommentsByPostIdDAO(Connection con, int postId) {
        super(con);
        if (postId <= 0) {
            throw new IllegalArgumentException("postId must be greater than 0.");
        }

        this.postId = postId;
    }

    /**
     * Lists all comments for a given post.
     *
     * @throws SQLException if an error occurs while executing the query
     */
    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final List<Comment> comments = new ArrayList<>();


        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();


            while (rs.next()) {
                comments.add(new Comment(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_post"),
                        rs.getString("text_content"),
                        rs.getString("username"))
                );

            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        outputParam = comments;
    }
}
