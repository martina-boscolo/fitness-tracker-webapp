package it.unipd.dei.cyclek.rest.like;

import it.unipd.dei.cyclek.dao.like.DeleteLikeDAO;
import it.unipd.dei.cyclek.dao.post.DeletePostDAO;
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
public class DeleteLikeRR extends AbstractRR {

    /**
     * Creates a new REST resource for deleting {@code Post}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public DeleteLikeRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.DELETE_LIKE, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        Like e = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            String id = path.substring(path.lastIndexOf('/') + 1);

            int likeId = Integer.parseInt(id);



            LogContext.setResource(Integer.toString(likeId));

            // creates a new DAO for accessing the database and deletes the employee
            e = new DeleteLikeDAO(con, likeId).access().getOutputParam();

            if (e != null) {
                LOGGER.info("Like successfully deleted.");

                res.setStatus(HttpServletResponse.SC_OK);
                e.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Like not found. Cannot delete it.");

                m = new Message(String.format("Like %d not found. Cannot delete it.", likeId), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot delete the like: wrong format for URI /post/{postId}.", ex);

            m = new Message("Cannot delete the post: wrong format for URI /post/{postId}.", "E4A7",
                    ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23503".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot delete the post: other resources depend on it.");

                m = new Message("Cannot delete the post: other resources depend on it.", "E5A4", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot delete the post: unexpected database error.", ex);

                m = new Message("Cannot delete the post: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
    }
}