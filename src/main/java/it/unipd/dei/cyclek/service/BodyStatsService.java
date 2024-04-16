package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.bodyStats.CreateBodyStatsRR;
import it.unipd.dei.cyclek.rest.bodyStats.ListBodyStatsByUserIdRR;
import it.unipd.dei.cyclek.rest.bodyStats.ListBodyStatsRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

public class BodyStatsService extends AbstractService{
    private static final String TABLE_NAME = "bodyStats";

    public static void unsupportedOperation(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String method = req.getMethod();
        String msg = String.format("Unsupported operation for URI /%s: %s.",TABLE_NAME,method);
        LOGGER.warn(msg);
        Message m = new Message(msg,"E4A5",String.format("Method %s,",method));
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        m.toJSON(res.getOutputStream());
    }
    public static boolean processBodyStats(HttpServletRequest req, HttpServletResponse res, Connection con) throws IOException {

        final String method = req.getMethod();
        String path = req.getRequestURI();
        Message m = null;

        // the requested resource was not a user
        if (path.lastIndexOf("rest/".concat(TABLE_NAME)) <= 0)
            return false;

        path = path.substring(path.lastIndexOf(TABLE_NAME) + TABLE_NAME.length());

        if (path.isEmpty() || path.equals("/")) {  // request is /bodyStats
            switch (method) {
                case "GET":
                    new ListBodyStatsRR(req, res, con).serve();  // list all table bodyStats
                    break;
                case "POST":
                    new CreateBodyStatsRR(req, res, con).serve();  // create a new bodyStats
                    break;
                default:
                    unsupportedOperation(req,res);
                    break;
            }
            // request is /bodyStats/user/{idUser}
        } else if (path.contains("user")) {
            if (method.equals("GET")) {
                new ListBodyStatsByUserIdRR(req, res, con).serve(); // get bodyStats by idUser
            } else {
                unsupportedOperation(req, res);
            }
            // request is /bodyStats
        } else {
            unsupportedOperation(req, res);
        }

        return true;
    }
}
