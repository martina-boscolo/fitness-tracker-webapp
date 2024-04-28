package it.unipd.dei.cyclek.rest.comment;

import it.unipd.dei.cyclek.dao.comment.CreateCommentDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.resources.entity.Comment;
import it.unipd.dei.cyclek.resources.entity.Post;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A REST resource for creating {@link Post}s.
 *
 * @author Martina Boscolo Bacheto
 * @version 1.00
 * @since 1.00
 */
public class CreateCommentRR extends AbstractRR {

    /**
     * Creates a new REST resource for creating {@code Post}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public CreateCommentRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.CREATE_COMMENT, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        Comment c = null;
        Message m = null;

        try {

            final Comment comment = Comment.fromJSON(req.getInputStream());

            c = new CreateCommentDAO(con, comment).access().getOutputParam();

            if (c != null) {
                LOGGER.info("Comment successfully created.");
                res.setStatus(HttpServletResponse.SC_CREATED);
                c.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while creating comment.");
                m = ErrorCode.CREATE_COMMENT_INTERNAL_SERVER_ERROR.getMessage();
                res.setStatus(ErrorCode.CREATE_COMMENT_INTERNAL_SERVER_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (EOFException ex) {
            LOGGER.warn("Cannot create comment: no Post Comment object found in the request.", ex);

            m = ErrorCode.CREATE_COMMENT_JSON_ERROR.getMessage();
            res.setStatus(ErrorCode.CREATE_COMMENT_JSON_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot create comment: it already exists.");
                m = ErrorCode.CREATE_COMMENT_ALREADY_EXISTS.getMessage();
                res.setStatus(ErrorCode.CREATE_COMMENT_ALREADY_EXISTS.getHttpCode());
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot create comment: unexpected database error.", ex);

                m = ErrorCode.CREATE_COMMENT_DB_ERROR.getMessage();
                res.setStatus(ErrorCode.CREATE_COMMENT_DB_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        }
    }


}
