package it.unipd.dei.cyclek.rest.comment;

import it.unipd.dei.cyclek.dao.comment.DeleteCommentDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A REST resource for deleting {@link Post}s.
 *
 * @author Martina Boscolo Bacheto
 * @version 1.00
 * @since 1.00
 */
public class DeleteCommentRR extends AbstractRR {

    /**
     * Creates a new REST resource for deleting {@code Post}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public DeleteCommentRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.DELETE_COMMENT, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        Comment e = null;
        Message m = null;

        try {
            // parse the URI path to extract the badge
            String path = req.getRequestURI();
            String[] parts = path.split("/");
            final int commentId = Integer.parseInt(parts[1]);

            LogContext.setResource(Integer.toString(commentId));


            e = new DeleteCommentDAO(con, commentId).access().getOutputParam();

            if (e != null) {
                LOGGER.info("Comment successfully deleted.");

                res.setStatus(HttpServletResponse.SC_OK);
                e.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Comment not found. Cannot delete it.");

                m = new Message(String.format("Comment %d not found. Cannot delete it.", commentId), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot delete the comment: wrong format for URI /post/{postId}/comment/{commentId}.", ex);

            m = new Message("Cannot delete the comment: wrong format for URI /post/{postId}/comment/{commentId}.", "E4A7",
                    ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23503".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot delete the comment: other resources depend on it.");

                m = new Message("Cannot delete the comment: other resources depend on it.", "E5A4", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot delete the comment: unexpected database error.", ex);

                m = new Message("Cannot delete the comment: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
    }
}
