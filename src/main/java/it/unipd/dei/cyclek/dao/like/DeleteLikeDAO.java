package it.unipd.dei.cyclek.dao.like;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Like;

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
    private static final String STATEMENT = "DELETE FROM likes WHERE id_user = ?  AND id_post = ? RETURNING *  ";
    private static final String STATEMENT_UPDATE = "UPDATE Posts SET likes_count = likes_count - 1 WHERE id = ?";


    /**
     * The id of the like to be deleted.
     */
    private final int userId;
    private final int postId;

    /**
     * Creates a new object for deleting a like from the database.
     *
     * @param con    the connection to the database.
     *
     * @throws IllegalArgumentException if likeId is less than or equal to 0.
     */
    public DeleteLikeDAO(Connection con, int userId, int postId ) {
        super(con);
        if (userId <= 0 || postId <= 0) {
            throw new IllegalArgumentException("likeId must be greater than 0.");
        }

        this.userId = userId;
        this.postId = postId;
    }

    /**
     * Deletes a like from the database.
     *
     * @throws SQLException if any error occurs while deleting the like.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        PreparedStatement pstmt2 = null;
        ResultSet rs = null;

        Like l = null;

        try {
            // Turn off auto-commit mode
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, userId);
            pstmt.setInt(2, postId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                l = new Like(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_post"));

                LOGGER.info("Like %s successfully deleted from the database.", l.getLikeId());
            }
            pstmt2 = con.prepareStatement(STATEMENT_UPDATE);
            pstmt2.setInt(1, postId);
            pstmt2.executeUpdate();

            // If both statements are successful, commit the transaction
            con.commit();

        } catch (SQLException e) {
            // If any statement fails, rollback the transaction
            if (con != null) {
                try {
                    LOGGER.error("Transaction is being rolled back");
                    con.rollback();
                } catch (SQLException excep) {
                    LOGGER.error("Error occurred during transaction rollback");
                }
            }
        } finally {
            // Turn auto-commit mode back on
            if (con != null) {
                con.setAutoCommit(true);
            }

            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

            if (pstmt2 != null) {
                pstmt2.close();
            }
        }

        outputParam = l;
    }
}
