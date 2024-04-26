package it.unipd.dei.cyclek.servlet;

import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

import static it.unipd.dei.cyclek.service.DietService.processDiet;
import static it.unipd.dei.cyclek.service.PostService.processPost;
import static it.unipd.dei.cyclek.service.UserGoalsService.processUserGoals;
import static it.unipd.dei.cyclek.service.UserService.processUser;
import static it.unipd.dei.cyclek.service.UserStatsService.processUserStats;


public class RestDispatcherServlet extends AbstractDatabaseServlet{
    private static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse res) throws IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        final OutputStream out = res.getOutputStream();

        try {

            // if the requested resource was a user, delegate its processing and return
            if (processUser(req, res, getConnection())) {
                return;
            }
            if (processUserStats(req, res, getConnection())) {
                return;
            }
            if (processUserGoals(req, res, getConnection())) {
                return;
            }
            if (processDiet(req, res, getConnection())){
                return;
            }
            // if the requested resource was a post, delegate its processing and return
            if (processPost(req, res, getConnection())) {
                return;
            }

            // if none of the above process methods succeeds, it means an unknown resource has been requested
            LOGGER.warn("Unknown resource requested: %s.", req.getRequestURI());
            final Message m = ErrorCode.REST_NOT_FOUND.getMessage();
            res.setStatus(ErrorCode.REST_NOT_FOUND.getHttpCode());
            res.setContentType(JSON_UTF_8_MEDIA_TYPE);
            m.toJSON(out);
        } catch (Throwable t) {
            LOGGER.error("Unexpected error while processing the REST resource.", t);
            final Message m = ErrorCode.INTERNAL_ERROR.getMessage();
            res.setStatus(ErrorCode.INTERNAL_ERROR.getHttpCode());
            m.toJSON(out);
        } finally {

            // ensure to always flush and close the output stream
            if (out != null) {
                out.flush();
                out.close();
            }

            LogContext.removeIPAddress();
        }
    }
}
