package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.userStats.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

public class UserStatsService extends AbstractService{
    private static final String TABLE_NAME = "stats";

    private static void unsupportedOperation(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String method = req.getMethod();
        String msg = String.format("Unsupported operation for URI /%s: %s.",TABLE_NAME,method);
        LOGGER.warn(msg);
        Message m = ErrorCode.UNSUPPORTED_OPERATION.getMessage();
        res.setStatus(ErrorCode.UNSUPPORTED_OPERATION.getHttpCode());
        m.toJSON(res.getOutputStream());
    }
    public static boolean processUserStats(HttpServletRequest req, HttpServletResponse res, Connection con) throws IOException {

        final String method = req.getMethod();
        String path = req.getRequestURI();

        // the requested resource was not a user
        if (path.lastIndexOf("rest/".concat(TABLE_NAME)) <= 0)
            return false;

        path = path.substring(path.lastIndexOf(TABLE_NAME) + TABLE_NAME.length());

        if (path.matches("/body") && (method.equals("GET") || method.equals("POST")))
            if (method.equals("GET"))                                                 // GET  /rest/stats/body  (list all table bodystats)
                new ListUserStatsRR(req, res, con).serve();
            else                                                                      // POST /rest/stats/body  (insert a new bodystats)
                new CreateUserStatsRR(req, res, con).serve();
        else if (path.matches("/body/user/\\d+$") && method.equals("GET"))      // GET  /rest/stats/body/user/{id}  (get all measures of a single user)
            new ListUserStatsByUserIdRR(req, res, con).serve();
        else if (path.matches("/imc/mean") && method.equals("GET"))             // GET  /rest/stats/imc/mean      (get global mean of IMC)
                new GetMeanImcRR(req, res, con).serve();
        else if (path.matches("/imc/user/\\d+$") && method.equals("GET"))       // GET  /rest/stats/imc/user/{id} (get actual imc and meanIMC among measures of a single user)
                new GetImcByUserIdRR(req, res, con).serve();
        else if (path.matches("/meals/user/\\d+$") && method.equals("GET"))     // GET  /rest/stats/meals/user/{id} (get stats about meals for a single user)
            new GetMealsByUserIdRR(req, res, con).serve();
        else
            unsupportedOperation(req, res);

        return true;
    }
}