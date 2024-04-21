package it.unipd.dei.cyclek.rest.like;

import it.unipd.dei.cyclek.dao.socialNetworkPost.ListSocialNetworkPostDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.ResourceList;
import it.unipd.dei.cyclek.resources.SocialNetworkPost;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A REST resource for listing {@link SocialNetworkPost}s.
 *
 * @author Martina Boscolo Bacheto
 * @version 1.00
 * @since 1.00
 */

public class ListLikeRR extends AbstractRR {
    /**
     * Creates a new REST resource for listing {@code SocialNetworkPost}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */

    public ListLikeRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LIST_POST, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        List<SocialNetworkPost> el = null;
        Message m = null;

        try {

            // creates a new DAO for accessing the database and lists the employee(s)
            el = new ListSocialNetworkPostDAO(con).access().getOutputParam();

            if (el != null) {
                LOGGER.info("Post(s) successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing post(s).");

                m = new Message("Cannot list post(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list post(s): unexpected database error.", ex);

            m = new Message("Cannot list post(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
