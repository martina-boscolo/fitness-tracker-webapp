package it.unipd.dei.cyclek.rest.like;

import it.unipd.dei.cyclek.dao.comment.CountCommentsByPostIdDAO;
import it.unipd.dei.cyclek.dao.like.CountLikesByPostIdDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class CountLikeRR extends AbstractRR {

    public CountLikeRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.COUNT_LIKE_BY_POST_ID, req, res, con);
    }
    @Override
    protected void doServe() throws IOException {

        int likeCount  = -1;
        Message m = null;

        try {

            String path = req.getRequestURI();
            String[] parts = path.split("/");
            final int postId = Integer.parseInt(parts[4]);

            likeCount = new CountLikesByPostIdDAO(con, postId).access().getOutputParam();

            if (likeCount > -1) {
                LOGGER.info("Like(s) successfully counted.");

                res.setStatus(HttpServletResponse.SC_OK);
                res.getOutputStream().write(Integer.toString(likeCount).getBytes());
            } else {
                LOGGER.error("Fatal error while counting like(s).");

                m = new Message("Cannot count like(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot count like(s): unexpected database error.", ex);

            m = new Message("Cannot count like(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }

}
