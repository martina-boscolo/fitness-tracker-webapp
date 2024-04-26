package it.unipd.dei.cyclek.dao.like;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Like;

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

        ResultSet rs = null;

        Like like = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, this.like.getUserId());
            pstmt.setInt(2, this.like.getPostId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                like = new Like(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_post"));

                LOGGER.info("Like {} successfully stored in the database.", like.getLikeId());
            }
        } finally {

            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        outputParam = like;

    }
}
