package it.unipd.dei.cyclek.rest.post;

import it.unipd.dei.cyclek.dao.post.CreatePostDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.resources.entity.Post;
import it.unipd.dei.cyclek.rest.AbstractRR;
import it.unipd.dei.cyclek.utils.TokenJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static it.unipd.dei.cyclek.utils.AuthUtils.extractUserId;

/**
 * A REST resource for creating {@link Post}s.
 *
 * @author Martina Boscolo Bacheto
 * @version 1.00
 * @since 1.00
 */
public class CreatePostRR extends AbstractRR {

    /**
     * Creates a new REST resource for creating {@code Post}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public CreatePostRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.CREATE_POST, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        Post p = null;
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
            path = path.substring(path.lastIndexOf("post") + 4);

            final Post post = Post.fromJSON(req.getInputStream());

            LogContext.setResource(Integer.toString(post.getPostId()));

            // creates a new DAO for accessing the database and stores the post
            p = new CreatePostDAO(con, post).access().getOutputParam();

            if (p != null) {
                LOGGER.info("Post successfully created.");

                res.setStatus(HttpServletResponse.SC_CREATED);
                p.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Fatal error while creating post.");
                m = ErrorCode.CREATE_POST_INTERNAL_SERVER_ERROR.getMessage();
                res.setStatus(ErrorCode.CREATE_POST_INTERNAL_SERVER_ERROR.getHttpCode());

                m.toJSON(res.getOutputStream());
            }
        } catch (EOFException ex) {
            LOGGER.warn("Cannot create post: no Post JSON object found in the request.", ex);

            m = ErrorCode.CREATE_POST_JSON_ERROR.getMessage();
            res.setStatus(ErrorCode.CREATE_POST_JSON_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot create post: it already exists.");
                m = ErrorCode.CREATE_POST_ALREADY_EXISTS.getMessage();
                res.setStatus(ErrorCode.CREATE_POST_ALREADY_EXISTS.getHttpCode());
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot create post: unexpected database error.", ex);

                m = ErrorCode.CREATE_POST_DB_ERROR.getMessage();
                res.setStatus(ErrorCode.CREATE_POST_DB_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        }
    }


}
