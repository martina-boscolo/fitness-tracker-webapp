package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.bodyObjective.CreateBodyObjRR;
import it.unipd.dei.cyclek.rest.bodyObjective.ListBodyObjByUserIdRR;
import it.unipd.dei.cyclek.rest.bodyObjective.ListBodyObjRR;
import it.unipd.dei.cyclek.rest.bodyStats.CreateBodyStatsRR;
import it.unipd.dei.cyclek.rest.bodyStats.ListBodyStatsByUserIdRR;
import it.unipd.dei.cyclek.rest.bodyStats.ListBodyStatsRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

public class BodyObjService extends AbstractService{
    private static final String TABLE_NAME = "bodyobj";

    private static void unsupportedOperation(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String method = req.getMethod();
        String msg = String.format("Unsupported operation for URI /%s: %s.",TABLE_NAME,method);
        LOGGER.warn(msg);
        Message m = new Message(msg,"E4A5",String.format("Method %s,",method));
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        m.toJSON(res.getOutputStream());
    }
    public static boolean processBodyObj(HttpServletRequest req, HttpServletResponse res, Connection con) throws IOException {

        final String method = req.getMethod();
        String path = req.getRequestURI();
        Message m = null;

        // the requested resource was not a user
        if (path.lastIndexOf("rest/".concat(TABLE_NAME)) <= 0)
            return false;

        path = path.substring(path.lastIndexOf(TABLE_NAME) + TABLE_NAME.length());

        if (path.isEmpty() || path.equals("/")) {
            switch (method) {
                case "GET":
                    new ListBodyObjRR(req, res, con).serve();           // GET  /rest/bodyobj  (list body objective of all users)
                    break;
                case "POST":
                    new CreateBodyObjRR(req, res, con).serve();         // POST /rest/bodyobj  (insert a new body objective)
                    break;
                default:
                    unsupportedOperation(req,res);
                    break;
            }
        } else if (path.contains("user") && method.equals("GET")) {
            new ListBodyObjByUserIdRR(req, res, con).serve();           // GET /rest/bodyobj/user/{id}  (list body objective of a user)
        } else {
            unsupportedOperation(req, res);
        }

        return true;
    }
}
