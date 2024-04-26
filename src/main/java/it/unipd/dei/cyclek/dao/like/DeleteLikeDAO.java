package it.unipd.dei.cyclek.dao.like;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for deleting a like from the database.
 * It extends the AbstractDAO class and overrides the doAccess method.
 *
 * @author Martina Boscolo Bacheto
 *
 */
public class DeleteLikeDAO extends AbstractDAO<Like> {

    /**
     * SQL statement to delete a like from the database.
     */
    private static final String STATEMENT = "DELETE FROM likes WHERE id = ?  RETURNING *  ";

    /**
     * The user ID of the like to be deleted.
     */
    private final int likeId;



    /**
     * Constructs a new DeleteLikeDAO object with the given connection, user ID and post ID.
     *
     * @param con the connection to the database.
     * @throws IllegalArgumentException if the user ID or post ID is less than or equal to 0.
     */
    public DeleteLikeDAO(Connection con, int likeId) {
        super(con);
        if (likeId <= 0 ) {
            throw new IllegalArgumentException("userId and postId must be greater than 0.");
        }

        this.likeId = likeId;
    }

    /**
     * Deletes the like from the database.
     *
     * @throws SQLException if any SQL error occurs while deleting the like.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Like l = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, likeId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                l = new Like(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_post"));

                LOGGER.info("Like %d successfully deleted from the database.", l.getPostId());
            }
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        outputParam = l;
    }
}
