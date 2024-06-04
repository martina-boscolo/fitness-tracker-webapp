package it.unipd.dei.cyclek.dao.like;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for creating a new like in the database.
 *
 * @author Martina Boscolo Bacheto
 *
 */
public class CreateLikeDAO extends AbstractDAO<Like> {

    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "INSERT INTO likes (id_user, id_post) VALUES (?, ?) RETURNING * ";
    private static final String STATEMENT_UPDATE = "UPDATE Posts SET likes_count = likes_count + 1 WHERE id = ?";

    /**
     * The like to be created.
     */
    private final Like like;


    /**
     * Creates a new object for creating a like in the database.
     *
     * @param con  the connection to the database.
     * @param like the like to be created.
     * @throws IllegalArgumentException if like is null.
     */
    public CreateLikeDAO(Connection con, Like like ) {
        super(con);
        if (like == null) {
            throw new IllegalArgumentException("like must not be null.");
        }

        this.like = like;
    }

    /**
     * Creates a new like in the database.
     *
     * @throws SQLException if any error occurs while creating the like.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;

        Like like = null;

        try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, this.like.getUserId());
            pstmt.setInt(2, this.like.getPostId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                like = new Like(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_post"));

                LOGGER.info("Like %s successfully stored in the database.", like.getLikeId());
            }
            // Execute the second statement
            pstmt2 = con.prepareStatement(STATEMENT_UPDATE);
            pstmt2.setInt(1, like.getPostId());
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

        outputParam = like;

    }
}
