package it.unipd.dei.cyclek.rest.comment;

import it.unipd.dei.cyclek.dao.socialNetworkPost.CreateSocialNetworkPostDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.SocialNetworkPost;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A REST resource for creating {@link SocialNetworkPost}s.
 *
 * @author Martina Boscolo Bacheto
 * @version 1.00
 * @since 1.00
 */
public class CreateCommentRR extends AbstractRR {

    /**
     * Creates a new REST resource for creating {@code SocialNetworkPost}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public CreateCommentRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.CREATE_POST, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        SocialNetworkPost p = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("post") + 4);

            final SocialNetworkPost socialNetworkPost = SocialNetworkPost.fromJSON(req.getInputStream());

            LogContext.setResource(Integer.toString(socialNetworkPost.getPostId()));

            // creates a new DAO for accessing the database and stores the employee
            p = new CreateSocialNetworkPostDAO(con, socialNetworkPost).access().getOutputParam();

            if (p != null) {
                LOGGER.info("Post successfully created.");

                res.setStatus(HttpServletResponse.SC_CREATED);
                p.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while creating post.");

                m = new Message("Cannot create post: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (EOFException ex) {
            LOGGER.warn("Cannot create post: no Post JSON object found in the request.", ex);

            m = new Message("Cannot create the Post: no Post JSON object found in the request.", "E4A8",
                    ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot create post: it already exists.");

                m = new Message("Cannot create post: it already exists.", "E5A2", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot create post: unexpected database error.", ex);

                m = new Message("Cannot create post: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
    }


}
