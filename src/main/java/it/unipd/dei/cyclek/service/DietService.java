package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.rest.diet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

public class DietService extends AbstractService {

    private static final String TABLE_NAME = "diet";
    public static boolean processDiet(HttpServletRequest req, HttpServletResponse res, Connection con) throws IOException {

        final String method = req.getMethod();
        String path = req.getRequestURI();

        if (path.lastIndexOf("rest/".concat(TABLE_NAME)) <= 0)
            return false;

        path = path.substring(path.lastIndexOf(TABLE_NAME) + TABLE_NAME.length());

        if (path.isEmpty() || path.equals("/")) {
            switch (method) {
                case "GET":
                    new ListDietRR(req, res, con).serve();
                    break;
                case "POST":
                    new SaveDietRR(req, res, con).serve();
                    break;
                case "PUT":
                    new UpdateDietRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsopported operation for URI /%s: %s.", TABLE_NAME, method);
            }
        }
        else if(path.contains("idUser") && method.equals("GET"))
            new GetDietByUserIdRR(req, res, con).serve();
        else if(path.contains("id") && method.equals("GET"))
            new GetDietByIdRR(req, res, con).serve();
        else{
            LOGGER.warn("Unsopported operation for URI /%s: %s.", TABLE_NAME, method);
            throw new UnsupportedOperationException();
        }
        return true;
    }
}
