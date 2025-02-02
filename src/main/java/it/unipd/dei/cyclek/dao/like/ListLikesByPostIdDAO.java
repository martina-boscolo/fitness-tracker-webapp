package it.unipd.dei.cyclek.dao.like;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * List all the likes for a post.
 *
 * @author Martina Boscolo Bacheto
 */
public class ListLikesByPostIdDAO extends AbstractDAO<List<Like>> {

    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "SELECT users.username , likes.* FROM likes INNER JOIN users ON likes.id_user = users.id  WHERE likes.id_post = ? ; ";

    /**
     * The id of the post.
     */
    private final int postId;

    /**
     * Creates a new object for listing all the likes for a post.
     *
     * @param con    the connection to the database.
     * @param postId the id of the post.
     * @throws IllegalArgumentException if postId is less than or equal to 0.
     */
    public ListLikesByPostIdDAO(Connection con, int postId) {
        super(con);
        if (postId <= 0) {
            throw new IllegalArgumentException("postId must be greater than 0.");
        }

        this.postId = postId;
    }

    /**
     * Lists all the likes for a post.
     *
     * @throws SQLException if any error occurs while listing the likes.
     */
    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final List<Like> likes = new ArrayList<>();


        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();


            while (rs.next()) {
                likes.add(new Like(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_post"),
                        rs.getString("username"))
                );

            }
            LOGGER.info("Likes successfully listed.");

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        outputParam = likes;
    }
}
