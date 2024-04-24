package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.rest.user.*;
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

        if (path.isEmpty() || path.equals("/") && method.equals("GET"))
                new ListUserRR(req, res, con).serve();

        else if (path.equals("/signup") || path.equals("/signup/") && method.equals("POST"))
            new RegisterUserRR(req, res, con).serve();

        else if (path.equals("/login") || path.equals("/login/") && method.equals("POST"))
            new LoginUserRR(req, res, con).serve();

        else if (path.contains("id") && method.equals("GET"))
            new GetUserByIdRR(req, res, con).serve();

        else if (path.contains("id") && method.equals("PUT"))
            new UpdateUserRR(req, res, con).serve();

        else {
            LOGGER.warn("Unsopported operation for URI /%s: %s.", TABLE_NAME, method);
            throw new UnsupportedOperationException();
        }
        return true;
    }
}


/*  GET      http://localhost:8080/cycleK-1.0.0/rest/user   --> take all the users
    GET      http://localhost:8080/cycleK-1.0.0/rest/user/id/(anyNumber)  --> take user with specific id
    POST     http://localhost:8080/cycleK-1.0.0/rest/user/login  --> login user
    POST     http://localhost:8080/cycleK-1.0.0/rest/user/signup --> signup user
    DELETE   http://localhost:8080/cycleK-1.0.0/rest/user/id/(anyNumber) --> delete a use
    PUT      http://localhost:8080/cycleK-1.0.0/rest/user/id/(anyNumber) --> modify user's information
*/



