package it.unipd.dei.cyclek.rest;

import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.Actions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;

public abstract class AbstractRR implements RestResource {

    /**
     * A LOGGER available for all the subclasses.
     */
    protected static final Logger LOGGER = LogManager.getLogger(AbstractRR.class,
            StringFormatterMessageFactory.INSTANCE);

    /**
     * The JSON MIME media type
     */
    protected static final String JSON_MEDIA_TYPE = "application/json";

    /**
     * The JSON UTF-8 MIME media type
     */
    protected static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";

    /**
     * The any MIME media type
     */
    protected static final String ALL_MEDIA_TYPE = "*/*";

    /**
     * The HTTP request
     */
    protected final HttpServletRequest req;

    /**
     * The HTTP response
     */
    protected final HttpServletResponse res;

    /**
     * The connection to the database
     */
    protected final Connection con;

    /**
     * The {@link Actions} performed by this REST resource.
     */
    private final String action;

    /**
     * Creates a new REST resource.
     *
     * @param action the action performed by this REST resource.
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    protected AbstractRR(final String action, final HttpServletRequest req, final HttpServletResponse res, final Connection con) {

        if (action == null || action.isEmpty()) {
            LOGGER.warn("Action is null or empty.");
        }
        this.action = action;
        LogContext.setAction(action);

        if (req == null) {
            LOGGER.error("The HTTP request cannot be null.");
            throw new NullPointerException("The HTTP request cannot be null.");
        }
        this.req = req;

        if (res == null) {
            LOGGER.error("The HTTP response cannot be null.");
            throw new NullPointerException("The HTTP response cannot be null.");
        }
        this.res = res;

        if (con == null) {
            LOGGER.error("The connection cannot be null.");
            throw new NullPointerException("The connection cannot be null.");
        }
        this.con = con;
    }

    @Override
    public void serve() throws IOException {

        try {
            // if the request method and/or the MIME media type are not allowed, return.
            // Appropriate error message sent by {@code checkMethodMediaType}
            if (!checkMethodMediaType(req, res)) {
                return;
            }

            doServe();
        } catch (Throwable t) {
            LOGGER.error("Unable to serve the REST request.", t);

            final Message m = ErrorCode.UNABLE_TO_SERVE_REQUEST.getMessageWithParam(action);
            res.setStatus(ErrorCode.UNABLE_TO_SERVE_REQUEST.getHttpCode());
            m.toJSON(res.getOutputStream());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    /**
     * Performs the actual logic needed for serving the REST request.
     *
     * Subclasses have to implement this method in order to define the actual strategy for serving the REST request.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    protected abstract void doServe() throws IOException;

    /**
     * Checks that the request method and MIME media type are allowed.
     *
     * Subclasses may override it to customize their behaviour, e.g. not limiting the MIME media types to JSON.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     *
     * @return {@code true} if the request method and the MIME type are allowed; {@code false} otherwise.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    protected boolean checkMethodMediaType(final HttpServletRequest req, final HttpServletResponse res) throws
            IOException {

        final String method = req.getMethod();
        final String contentType = req.getHeader("Content-Type");
        final String accept = req.getHeader("Accept");
        final OutputStream out = res.getOutputStream();

        Message m = null;

        if (accept == null) {
            LOGGER.error("Output media type not specified. Accept request header missing.");
            m = ErrorCode.MEDIA_TYPE_NOT_SPECIFIED.getMessage();
            res.setStatus(ErrorCode.MEDIA_TYPE_NOT_SPECIFIED.getHttpCode());
            m.toJSON(out);
            return false;
        }

        if (!accept.contains(JSON_MEDIA_TYPE) && !accept.equals(ALL_MEDIA_TYPE)) {
            LOGGER.error("Unsupported output media type. Resources are represented only in application/json.");
            m = ErrorCode.UNSUPPORTED_MEDIA_TYPE.getMessage();
            res.setStatus(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getHttpCode());
            m.toJSON(out);
            return false;
        }

        // if the method is supposed to send a body, check its MIME media type
        switch (method) {
            case "GET":
            case "DELETE":
                // nothing to do
                break;

            case "POST":
            case "PUT":
                if (contentType == null) {
                    LOGGER.error("Input media type not specified. Content-Type request header missing.");
                    m = ErrorCode.CONTENT_TYPE_MISSING.getMessage();
                    res.setStatus(ErrorCode.CONTENT_TYPE_MISSING.getHttpCode());
                    m.toJSON(out);
                    return false;
                }

                if (!contentType.contains(JSON_MEDIA_TYPE)) {
                    LOGGER.error("Unsupported input media type. Resources are represented only in application/json.");
                    m = ErrorCode.UNSUPPORTED_MEDIA_TYPE.getMessage();
                    res.setStatus(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getHttpCode());
                    m.toJSON(out);
                    return false;
                }

                break;
            default:
                LOGGER.error("Unsupported operation.");
                m = ErrorCode.UNSUPPORTED_OPERATION.getMessage();
                res.setStatus(ErrorCode.UNSUPPORTED_OPERATION.getHttpCode());
                m.toJSON(out);
                return false;
        }

        return true;
    }

}
