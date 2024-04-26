package it.unipd.dei.cyclek.rest.like;

import it.unipd.dei.cyclek.dao.like.ListLikesByPostIdDAO;
import it.unipd.dei.cyclek.resources.*;
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

public class ListLikeRR extends AbstractRR {
    /**
     * Creates a new REST resource for listing {@code Post}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */

    public ListLikeRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LIST_LIKE_BY_POST_ID, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {


        List<Like> el = null;
        Message m = null;

        try {

            String path = req.getRequestURI();
            String[] parts = path.split("/");

            final int postId = Integer.parseInt(parts[4]);


            LogContext.setResource(Integer.toString(postId));
            // creates a new DAO for accessing the database and lists the employee(s)
            el = new ListLikesByPostIdDAO(con, postId ).access().getOutputParam();

            if (el != null) {
                LOGGER.info("Like(s) successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing Like(s).");

                m = new Message("Cannot list Like(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list Like(s): unexpected database error.", ex);

            m = new Message("Cannot list Like(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
