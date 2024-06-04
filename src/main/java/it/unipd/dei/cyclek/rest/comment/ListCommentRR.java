package it.unipd.dei.cyclek.rest.comment;

import it.unipd.dei.cyclek.dao.comment.ListCommentsByPostIdDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.resources.entity.Comment;
import it.unipd.dei.cyclek.resources.entity.Post;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static it.unipd.dei.cyclek.utils.AuthUtils.extractUserId;

/**
 * A REST resource for listing {@link Post}s.
 *
 * @author Martina Boscolo Bacheto
 * @version 1.00
 * @since 1.00
 */

public class ListCommentRR extends AbstractRR {
    /**
     * Creates a new REST resource for listing {@code Post}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */

    public ListCommentRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LIST_COMMENT_BY_POST_ID, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        List<Comment> el = null;
        Message m = null;

        try {
            Integer idUser = extractUserId(req);
            if (idUser == null) {
                LOGGER.error("Unauthorized");
                m = ErrorCode.UNAUTHORIZED.getMessage();
                res.setStatus(ErrorCode.UNAUTHORIZED.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }

            String path = req.getRequestURI();
            String[] parts = path.split("/");
            final int postId = Integer.parseInt(parts[4]);

            el = new ListCommentsByPostIdDAO(con, postId).access().getOutputParam();

            if (el != null) {
                LOGGER.info("Comment(s) successfully listed.");
                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Fatal error while listing comment(s).");
                m = new Message("Cannot list comment(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list comment(s): unexpected database error.", ex);
            m = ErrorCode.LIST_COMMENT_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.LIST_COMMENT_INTERNAL_SERVER_ERROR.getHttpCode());

            m.toJSON(res.getOutputStream());
        }
    }
}
