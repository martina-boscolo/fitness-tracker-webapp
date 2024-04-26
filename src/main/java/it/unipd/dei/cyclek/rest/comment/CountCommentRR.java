package it.unipd.dei.cyclek.rest.comment;

import it.unipd.dei.cyclek.dao.comment.CountCommentsByPostIdDAO;
import it.unipd.dei.cyclek.dao.comment.ListCommentsByPostIdDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CountCommentRR extends AbstractRR {

    public CountCommentRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.COUNT_COMMENT_BY_POST_ID, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {

        int commentCount = -1;
        Message m = null;

        try {

            String path = req.getRequestURI();
            String[] parts = path.split("/");
            final int postId = Integer.parseInt(parts[4]);

            commentCount = new CountCommentsByPostIdDAO(con, postId).access().getOutputParam();

            if (commentCount > -1) {
                LOGGER.info("Comment(s) successfully counted.");
                res.setStatus(HttpServletResponse.SC_OK);
                res.getOutputStream().write(Integer.toString(commentCount).getBytes());
            } else {
                LOGGER.error("Fatal error while counting comment(s).");

                m = ErrorCode.COUNT_COMMENT_INTERNAL_SERVER_ERROR.getMessage();
                res.setStatus(ErrorCode.COUNT_COMMENT_INTERNAL_SERVER_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot count comment(s): unexpected database error.", ex);
            m = ErrorCode.COUNT_COMMENT_DB_ERROR.getMessage();
            res.setStatus(ErrorCode.COUNT_COMMENT_DB_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }

}
