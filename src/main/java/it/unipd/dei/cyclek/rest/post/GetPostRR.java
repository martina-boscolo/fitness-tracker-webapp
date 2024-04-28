package it.unipd.dei.cyclek.rest.post;

import it.unipd.dei.cyclek.dao.post.GetPostDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A REST resource for reading {@link Post}s.
 *
 * @author Martina Boscolo Bacheto
 * @version 1.00
 * @since 1.00
 */
public class GetPostRR extends AbstractRR {


    /**
     * Creates a new REST resource for reading {@code Post}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public GetPostRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.GET_POST_BY_ID, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        Post e = null;
        Message m = null;

        try {
            // parse the URI path to extract the postId
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("post") + 4);

            final int postId = Integer.parseInt(path.substring(1));

            LogContext.setResource(Integer.toString(postId));

            // creates a new DAO for accessing the database and reads the post
            e = new GetPostDAO(con, postId).access().getOutputParam();

            if (e != null) {
                LOGGER.info("Post successfully read.");

                res.setStatus(HttpServletResponse.SC_OK);
                e.toJSON(res.getOutputStream());
            } else {
                LOGGER.warn("Post not found. Cannot read it.");

                m = ErrorCode.GET_POST_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.GET_POST_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.warn("Cannot read post: wrong format for URI /post/{postId}.", ex);

            m = ErrorCode.GET_POST_BAD_REQUEST.getMessage();
            res.setStatus(ErrorCode.GET_POST_BAD_REQUEST.getHttpCode());
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            LOGGER.error("Cannot read post: unexpected database error.", ex);

            m = ErrorCode.GET_POST_DB_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_POST_DB_ERROR.getHttpCode());

            m.toJSON(res.getOutputStream());
        }
    }


}