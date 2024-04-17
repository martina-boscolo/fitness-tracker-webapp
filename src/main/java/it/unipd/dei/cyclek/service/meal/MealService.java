package it.unipd.dei.cyclek.service.meal;

import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.ListUserRR;
import it.unipd.dei.cyclek.service.AbstractService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

public class MealService extends AbstractService {
    private static final String TABLE_NAME = "meal";
    public static boolean processMeal(HttpServletRequest req, HttpServletResponse res, Connection con) throws IOException {

        final String method = req.getMethod();
        String path = req.getRequestURI();
        Message m = null;

        // the requested resource was not a user
        if (path.lastIndexOf("rest/".concat(TABLE_NAME)) <= 0)
            return false;

        path = path.substring(path.lastIndexOf(TABLE_NAME) + TABLE_NAME.length());

        if (!path.isEmpty() && path.equals("/mealService")) {
            switch (method) {
                case "GET":
                    new ListUserRR(req, res, con).serve();
                    break;
                case "POST":
                    throw new UnsupportedOperationException();
                default:
                    LOGGER.warn("Unsopported operation for URI /%s: %s.", TABLE_NAME, method);
            }
        }

        return true;
    }
}
