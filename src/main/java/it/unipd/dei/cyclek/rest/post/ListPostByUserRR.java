package it.unipd.dei.cyclek.rest.post;

import it.unipd.dei.cyclek.dao.like.DeleteLikeDAO;
import it.unipd.dei.cyclek.dao.post.ListPostByUserIdDAO;
import it.unipd.dei.cyclek.dao.post.ListPostDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListPostByUserRR extends AbstractRR {
    public ListPostByUserRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LIST_POST_BY_USER_ID, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {

        List<Post> el = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            String id = path.substring(path.lastIndexOf('/') + 1);

            int userId = Integer.parseInt(id);

            el = new ListPostByUserIdDAO(con, userId).access().getOutputParam();

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
