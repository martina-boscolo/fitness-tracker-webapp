package it.unipd.dei.cyclek.rest.post;

import it.unipd.dei.cyclek.dao.post.ListPostDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.Post;
import it.unipd.dei.cyclek.resources.ResourceList;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A REST resource for listing {@link Post}s.
 *
 * @author Martina Boscolo Bacheto
 * @version 1.00
 * @since 1.00
 */

public class ListPostRR extends AbstractRR {
    /**
     * Creates a new REST resource for listing {@code Post}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */

    public ListPostRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LIST_POST, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        List<Post> el = null;
        Message m = null;

        try {

            // creates a new DAO for accessing the database and lists the employee(s)
            el = new ListPostDAO(con).access().getOutputParam();

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
