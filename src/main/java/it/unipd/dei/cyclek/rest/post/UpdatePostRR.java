package it.unipd.dei.cyclek.rest.post;

import it.unipd.dei.cyclek.dao.post.UpdatePostDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.resources.entity.Post;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A REST resource for updating {@link Post}s.
 *
 * @author Martina Boscolo Bacheto
 * @version 1.00
 * @since 1.00
 */
public class UpdatePostRR extends AbstractRR {

    /**
     * Creates a new REST resource for updating {@code Post}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public UpdatePostRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.UPDATE_POST, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        Post e = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("post") + 4);

            final int postId = Integer.parseInt(path.substring(1));

            LogContext.setResource(Integer.toString(postId));

            final Post post = Post.fromJSON(req.getInputStream());

            if (postId != post.getPostId()) {
                LOGGER.warn("Cannot update the post: URI request (%d) and post resource (%d) postId differ.",
                        postId, post.getPostId());

                m = ErrorCode.UPDATE_POST_BAD_REQUEST.getMessage();
                res.setStatus(ErrorCode.UPDATE_POST_BAD_REQUEST.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }



            e = new UpdatePostDAO(con, post).access().getOutputParam();

            if (e != null) {
                LOGGER.info("Post successfully updated.");

                res.setStatus(HttpServletResponse.SC_OK);
                e.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Post not found. Cannot update it.");

                m = ErrorCode.UPDATE_POST_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.UPDATE_POST_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot delete post: wrong format for URI /post/{postId}.", ex);

            m = ErrorCode.UPDATE_POST_BAD_FORMAT.getMessage();
            res.setStatus(ErrorCode.UPDATE_POST_BAD_FORMAT.getHttpCode());
            m.toJSON(res.getOutputStream());
        } catch (EOFException ex) {
            LOGGER.warn("Cannot updated post: no Post JSON object found in the request.", ex);

            m = ErrorCode.UPDATE_POST_JSON_ERROR.getMessage();
            res.setStatus(ErrorCode.UPDATE_POST_JSON_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23503".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot delete post: other resources depend on it.");

                m = ErrorCode.DELETE_POST_CONFLICT.getMessage();
                res.setStatus(ErrorCode.DELETE_POST_CONFLICT.getHttpCode());
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot delete post: unexpected database error.", ex);

                m = ErrorCode.UPDATE_POST_DB_ERROR.getMessage();
                res.setStatus(ErrorCode.UPDATE_POST_DB_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        }
    }
}
