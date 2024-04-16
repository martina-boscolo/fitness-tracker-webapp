package it.unipd.dei.cyclek.servlet;

import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.post.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

import static it.unipd.dei.cyclek.service.UserService.processUser;


public class RestDispatcherServlet extends AbstractDatabaseServlet{
    private static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";

    protected void service(final HttpServletRequest req, final HttpServletResponse res) throws IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        final OutputStream out = res.getOutputStream();

        try {

            // if the requested resource was an Employee, delegate its processing and return
            if (processUser(req, res, getConnection())) {
                return;
            }


            // if the requested resource was a post, delegate its processing and return
            if (processSocialNetworkPost(req, res)) {
                return;
            }

            // if none of the above process methods succeeds, it means an unknown resource has been requested
            LOGGER.warn("Unknown resource requested: %s.", req.getRequestURI());

            final Message m = new Message("Unknown resource requested.", "E4A6",
                    String.format("Requested resource is %s.", req.getRequestURI()));
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.setContentType(JSON_UTF_8_MEDIA_TYPE);
            m.toJSON(out);
        } catch (Throwable t) {
            LOGGER.error("Unexpected error while processing the REST resource.", t);

            final Message m = new Message("Unexpected error.", "E5A1", t.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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

    private boolean processSocialNetworkPost(final HttpServletRequest req, final HttpServletResponse res) throws Exception {

        final String method = req.getMethod();

        String path = req.getRequestURI();
        Message m = null;

        // the requested resource was not an employee
        if (path.lastIndexOf("rest/post") <= 0) {
            return false;
        }

        // strip everything until after the /employee
        path = path.substring(path.lastIndexOf("post") + 4);

        // the request URI is: /post
        // if method GET, list post
        // if method POST, create post
        if (path.length() == 0 || path.equals("/")) {

            switch (method) {
                case "GET":
                    new ListSocialNetworkPostRR(req, res, getConnection()).serve();
                    break;
                case "POST":
                    new CreateSocialNetworkPostRR(req, res, getConnection()).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post: %s.", method);

                    m = new Message("Unsupported operation for URI /post.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }else  {
            switch (method) {
                case "GET":
                    new ReadSocialNetworkPostRR(req, res, getConnection()).serve();
                    break;
                case "PUT":
                    new UpdateSocialNetworkPostRR(req, res, getConnection()).serve();
                    break;
                case "DELETE":
                    new DeleteSocialNetworkPostRR(req, res, getConnection()).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }


        return true;

    }
}
