package it.unipd.dei.cyclek.dao.like;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for deleting a like from the database.
 *
 * @author Martina Boscolo Bacheto
 *
 */
public class DeleteLikeDAO extends AbstractDAO<Like> {


    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "DELETE FROM likes WHERE id = ?  RETURNING *  ";

    /**
     * The id of the like to be deleted.
     */
    private final int likeId;

    /**
     * Creates a new object for deleting a like from the database.
     *
     * @param con    the connection to the database.
     * @param likeId the id of the like to be deleted.
     * @throws IllegalArgumentException if likeId is less than or equal to 0.
     */
    public DeleteLikeDAO(Connection con, int likeId) {
        super(con);
        if (likeId <= 0 ) {
            throw new IllegalArgumentException("likeId must be greater than 0.");
        }

        this.likeId = likeId;
    }

    /**
     * Deletes a like from the database.
     *
     * @throws SQLException if any error occurs while deleting the like.
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

                LOGGER.info("Like {} successfully deleted from the database.", l.getLikeId());
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
