package it.unipd.dei.cyclek.rest.comment;

import it.unipd.dei.cyclek.dao.comment.DeleteCommentDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.resources.entity.Comment;
import it.unipd.dei.cyclek.resources.entity.Post;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static it.unipd.dei.cyclek.utils.AuthUtils.extractUserId;

/**
 * A REST resource for deleting {@link Post}s.
 *
 * @author Martina Boscolo Bacheto
 * @version 1.00
 * @since 1.00
 */
public class DeleteCommentRR extends AbstractRR {

    /**
     * Creates a new REST resource for deleting {@code Post}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public DeleteCommentRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.DELETE_COMMENT, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        Comment e = null;
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
            String id = path.substring(path.lastIndexOf('/') + 1);

            int commentId = Integer.parseInt(id);

            LogContext.setResource(Integer.toString(commentId));

            e = new DeleteCommentDAO(con, commentId).access().getOutputParam();

            if (e != null) {
                LOGGER.info("Comment successfully deleted.");
                res.setStatus(HttpServletResponse.SC_OK);
                e.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Comment not found. Cannot delete it.");
                m =ErrorCode.DELETE_COMMENT_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.DELETE_COMMENT_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot delete the comment: wrong format for URI /post/{postId}/comment/{commentId}.", ex);

            m = ErrorCode.DELETE_COMMENT_WRONG_FORMAT.getMessage();
            res.setStatus(ErrorCode.DELETE_COMMENT_WRONG_FORMAT.getHttpCode());
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23503".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot delete the comment: other resources depend on it.");
                m = ErrorCode.DELETE_COMMENT_CONFLICT.getMessage();
                res.setStatus(ErrorCode.DELETE_COMMENT_CONFLICT.getHttpCode());
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot delete the comment: unexpected database error.", ex);
                m = ErrorCode.DELETE_COMMENT_DB_ERROR.getMessage();
                res.setStatus(ErrorCode.DELETE_COMMENT_DB_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        }
    }
}
