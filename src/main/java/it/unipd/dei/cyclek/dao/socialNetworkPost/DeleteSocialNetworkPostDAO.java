package it.unipd.dei.cyclek.dao.socialNetworkPost;

import it.unipd.dei.cyclek.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class is responsible for deleting a social network post from the database.
 * It extends the AbstractDAO class and overrides the doAccess method.
 *
 * @author Martina Boscolo Bacheto
 *
 */
public class DeleteSocialNetworkPostDAO extends AbstractDAO {

    /**
     * SQL statement to delete a social network post from the database.
     */
    private static final String STATEMENT = "DELETE FROM cyclek.public.social_network_posts WHERE id = ? ";

    /**
     * The post ID to be deleted.
     */
    private final int postId;

    /**
     * Constructs a new DeleteSocialNetworkPostDAO object with the given connection and post ID.
     *
     * @param con the connection to the database.
     * @param postId the post ID to be deleted.
     * @throws IllegalArgumentException if the post ID is less than or equal to 0.
     */
    public DeleteSocialNetworkPostDAO(Connection con,  int postId) {
        super(con);
        if ( postId <= 0) {
            throw new IllegalArgumentException("postId must be greater than 0.");
        }

        this.postId = postId;
    }

    /**
     * Deletes the social network post from the database.
     *
     * @throws SQLException if any SQL error occurs while deleting the post.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, postId);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }
}
