package it.unipd.dei.cyclek.rest.like;

import it.unipd.dei.cyclek.dao.like.DeleteLikeDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.resources.entity.Like;
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
public class DeleteLikeRR extends AbstractRR {

    /**
     * Creates a new REST resource for deleting {@code Post}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public DeleteLikeRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.DELETE_LIKE, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        Like e = null;
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
            final int userId = Integer.parseInt(parts[5]);
            final int postId = Integer.parseInt(parts[6]);

            //String path = req.getRequestURI();
            //String id = path.substring(path.lastIndexOf('/') + 1);

            //int user = Integer.parseInt(userId);

            LogContext.setResource(Integer.toString(userId));
            LogContext.setResource(Integer.toString(postId));

            e = new DeleteLikeDAO(con, userId, postId).access().getOutputParam();

            if (e != null) {
                LOGGER.info("Like successfully deleted.");
                res.setStatus(HttpServletResponse.SC_OK);
                e.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Like not found. Cannot delete it.");
                m = ErrorCode.DELETE_LIKE_INTERNAL_SERVER_ERROR.getMessage();
                res.setStatus(ErrorCode.DELETE_LIKE_INTERNAL_SERVER_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot delete the like: wrong format for URI /post/{postId}.", ex);
            m = ErrorCode.DELETE_LIKE_WRONG_FORMAT.getMessage();
            res.setStatus(ErrorCode.DELETE_LIKE_WRONG_FORMAT.getHttpCode());
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23503".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot delete the post: other resources depend on it.");
                m = ErrorCode.DELETE_LIKE_CONFLICT.getMessage();
                res.setStatus(ErrorCode.DELETE_LIKE_CONFLICT.getHttpCode());
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot delete the post: unexpected database error.", ex);
                m = ErrorCode.DELETE_LIKE_DB_ERROR.getMessage();
                res.setStatus(ErrorCode.DELETE_LIKE_DB_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        }
    }
}
