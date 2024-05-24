package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.userGoals.CreateUserGoalsRR;
import it.unipd.dei.cyclek.rest.userGoals.ListUserGoalsByUserIdRR;
import it.unipd.dei.cyclek.rest.userGoals.ListUserGoalsRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

public class UserGoalsService extends AbstractService{
    private static final String TABLE_NAME = "goals";

    private static void unsupportedOperation(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String method = req.getMethod();
        String msg = String.format("Unsupported operation for URI /%s: %s.",TABLE_NAME,method);
        LOGGER.warn(msg);
        Message m = ErrorCode.UNSUPPORTED_OPERATION.getMessage();
        res.setStatus(ErrorCode.UNSUPPORTED_OPERATION.getHttpCode());
        m.toJSON(res.getOutputStream());
    }
    public static boolean processUserGoals(HttpServletRequest req, HttpServletResponse res, Connection con) throws IOException {

        final String method = req.getMethod();
        String path = req.getRequestURI();

        // the requested resource was not a user
        if (path.lastIndexOf("rest/".concat(TABLE_NAME)) <= 0)
            return false;

        path = path.substring(path.lastIndexOf(TABLE_NAME) + TABLE_NAME.length());

        if ((path.isEmpty() || path.equals("/")) && (method.equals("GET") || method.equals("POST")))
            if (method.equals("GET"))
                new ListUserGoalsRR(req, res, con).serve();           // GET  /rest/goals  (list user goals of all users)
            else
                new CreateUserGoalsRR(req, res, con).serve();         // POST /rest/goals  (insert a new user goal)
        else if (path.matches("/user(/)?") && method.equals("GET"))
            new ListUserGoalsByUserIdRR(req, res, con).serve();       // GET /rest/goals/user/{id}  (list user goals of a user)
        else
            unsupportedOperation(req, res);

        return true;
    }
}
