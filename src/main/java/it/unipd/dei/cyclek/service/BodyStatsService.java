package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.bodyStats.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

public class BodyStatsService extends AbstractService{
    private static final String TABLE_NAME = "bodystats";

    private static void unsupportedOperation(HttpServletRequest req, HttpServletResponse res) throws IOException {
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

        if (path.isEmpty() || path.equals("/")) {
            switch (method) {
                case "GET":
                    new ListBodyStatsRR(req, res, con).serve();         // GET  /rest/bodystats  (list all table bodystats)
                    break;
                case "POST":
                    new CreateBodyStatsRR(req, res, con).serve();       // POST /rest/bodystats  (insert a new bodystats)
                    break;
                default:
                    unsupportedOperation(req, res);
                    break;
            }
        } else if (path.contains("imc")) {
            if (path.contains("mean") && method.equals("GET"))          // GET  /rest/bodystats/imc/mean      (get global mean of IMC)
                new GetMeanImcRR(req, res, con).serve();
            else if (path.contains("user") && method.equals("GET"))     // GET  /rest/bodystats/imc/user/{id} (get actual imc and meanIMC among measures of a single user)
                new GetImcByUserIdRR(req, res, con).serve();
            else
                unsupportedOperation(req, res);
        } else if (path.contains("user") && method.equals("GET")) {     // GET  /rest/bodystats/user/{id}  (get all measures of a single user)
            new ListBodyStatsByUserIdRR(req, res, con).serve();
        } else {
            unsupportedOperation(req, res);
        }

        return true;
    }
}