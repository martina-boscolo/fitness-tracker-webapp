package it.unipd.dei.cyclek.rest.post;

import it.unipd.dei.cyclek.dao.socialNetworkPost.DeleteSocialNetworkPostDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.SocialNetworkPost;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A REST resource for deleting {@link SocialNetworkPost}s.
 *
 * @author Martina Boscolo Bacheto
 * @version 1.00
 * @since 1.00
 */
public class DeleteSocialNetworkPostRR extends AbstractRR {

    /**
     * Creates a new REST resource for deleting {@code SocialNetworkPost}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public DeleteSocialNetworkPostRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.DELETE_POST, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        SocialNetworkPost e = null;
        Message m = null;

        try {
            // parse the URI path to extract the badge
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("post") + 4);

            final int postId = Integer.parseInt(path.substring(1));

            LogContext.setResource(Integer.toString(postId));

            // creates a new DAO for accessing the database and deletes the employee
            e = new DeleteSocialNetworkPostDAO(con, postId).access().getOutputParam();

            if (e != null) {
                LOGGER.info("Post successfully deleted.");

                res.setStatus(HttpServletResponse.SC_OK);
                e.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Post not found. Cannot delete it.");

                m = new Message(String.format("Post %d not found. Cannot delete it.", postId), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot delete the post: wrong format for URI /post/{postId}.", ex);

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
