package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.rest.user.ListUserRR;
import it.unipd.dei.cyclek.rest.user.RegisterUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;


public class UserService extends AbstractService{
    private static final String TABLE_NAME = "user";
    public static boolean processUser(HttpServletRequest req, HttpServletResponse res, Connection con) throws IOException {

        final String method = req.getMethod();
        String path = req.getRequestURI();

        // the requested resource was not a user
        if (path.lastIndexOf("rest/".concat(TABLE_NAME)) <= 0)
            return false;

        path = path.substring(path.lastIndexOf(TABLE_NAME) + TABLE_NAME.length());

        if (path.isEmpty() || path.equals("/")) {
            switch (method) {
                case "GET":
                    new ListUserRR(req, res, con).serve();
                    break;
                case "POST":
                    new RegisterUser(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsopported operation for URI /%s: %s.", TABLE_NAME, method);
            }
        }

        return true;
    }
}

