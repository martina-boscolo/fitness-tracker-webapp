package it.unipd.dei.cyclek.rest.like;

import it.unipd.dei.cyclek.dao.like.CreateLikeDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A REST resource for creating {@link Like}s.
 *
 * @author Martina Boscolo Bacheto
 * @version 1.00
 * @since 1.00
 */
public class CreateLikeRR extends AbstractRR {

    /**
     * Creates a new REST resource for creating {@code Like}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public CreateLikeRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.CREATE_LIKE, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        Like p = null;

        Message m = null;

        try {

            final Like like = Like.fromJSON(req.getInputStream());

            LogContext.setResource(Integer.toString(like.getLikeId()));

            p = new CreateLikeDAO(con, like).access().getOutputParam();

            if (p != null) {
                LOGGER.info("Like successfully created.");
                res.setStatus(HttpServletResponse.SC_CREATED);
                p.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while creating like.");
                m = ErrorCode.CREATE_LIKE_INTERNAL_SERVER_ERROR.getMessage();
                res.setStatus(ErrorCode.CREATE_LIKE_INTERNAL_SERVER_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (EOFException ex) {
            LOGGER.warn("Cannot create like: no Like JSON object found in the request.", ex);
            m = new Message("Cannot create the Like: no Like JSON object found in the request.", "E4A8",
                    ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot create like: it already exists.");
                m = ErrorCode.CREATE_LIKE_JSON_ERROR.getMessage();
                res.setStatus(ErrorCode.CREATE_LIKE_JSON_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot create like: unexpected database error.", ex);
                m = ErrorCode.CREATE_LIKE_DB_ERROR.getMessage();
                res.setStatus(ErrorCode.CREATE_LIKE_DB_ERROR.getHttpCode());

                m.toJSON(res.getOutputStream());
            }
        }
    }


}
