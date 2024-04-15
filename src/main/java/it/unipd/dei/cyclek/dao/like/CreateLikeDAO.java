package it.unipd.dei.cyclek.dao.like;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class is responsible for creating a new like in the database.
 * It extends the AbstractDAO class and overrides the doAccess method.
 *
 * @author Martina Boscolo Bacheto
 *
 */
public class CreateLikeDAO extends AbstractDAO {

    /**
     * SQL statement to insert a new like into the database.
     */
    private static final String STATEMENT = "INSERT INTO cyclek.public.likes (id_user, id_post, is_like) VALUES (?, ?, ?)";

    /**
     * The like to be inserted into the database.
     */
    private final Like like;

    /**
     * Constructs a new CreateLikeDAO object with the given connection and like.
     *
     * @param con  the connection to the database.
     * @param like the like to be inserted into the database.
     * @throws IllegalArgumentException if the like is null.
     */
    public CreateLikeDAO(Connection con, Like like) {
        super(con);
        if (like == null) {
            throw new IllegalArgumentException("like must not be null.");
        }

        this.like = like;
    }

    /**
     * Inserts the like into the database.
     *
     * @throws SQLException if any SQL error occurs while inserting the like.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, like.getUserId());
            pstmt.setInt(2, like.getPostId());
            pstmt.setBoolean(3, like.isLike());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }
}
