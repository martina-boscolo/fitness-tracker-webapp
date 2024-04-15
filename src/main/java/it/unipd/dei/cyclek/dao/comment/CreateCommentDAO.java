package it.unipd.dei.cyclek.dao.comment;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class is responsible for creating a new comment in the database.
 * It extends the AbstractDAO class and overrides the doAccess method.
 *
 * @author Martina Boscolo Bacheto
 *
 */
public class CreateCommentDAO extends AbstractDAO {

    /**
     * SQL statement to insert a new comment into the database.
     */
    private static final String STATEMENT = "INSERT INTO cyclek.public.likes (id_user, id_post, text_content) VALUES (?, ?, ?)";
    /**
     * The comment to be inserted into the database.
     */
    private final Comment comment;

    /**
     * Constructs a new CreateCommentDAO object with the given connection and comment.
     *
     * @param con the connection to the database.
     * @param comment the comment to be inserted into the database.
     * @throws IllegalArgumentException if the comment is null.
     */
    public CreateCommentDAO(Connection con, Comment comment) {
        super(con);
        if (comment == null) {
            throw new IllegalArgumentException("comment must not be null.");
        }

        this.comment = comment;
    }

    /**
     * Inserts the comment into the database.
     *
     * @throws SQLException if any SQL error occurs while inserting the comment.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setInt(1, comment.getUserId());
            pstmt.setInt(2, comment.getPostId());
            pstmt.setString(3, comment.getCommentText());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }
}