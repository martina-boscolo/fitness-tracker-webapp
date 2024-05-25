package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.rest.food.GetFoodByIdRR;
import it.unipd.dei.cyclek.rest.food.ListFoodRR;
import it.unipd.dei.cyclek.rest.food.RegisterFoodRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

public class FoodService extends AbstractService {
    private static final String TABLE_NAME = "foods";

    public static boolean processFood(HttpServletRequest req, HttpServletResponse res, Connection con) throws IOException {
        final String method = req.getMethod();
        String path = req.getRequestURI();

        // Verifica se il percorso della richiesta contiene la stringa "rest/foods"
        if (path.lastIndexOf("rest/".concat(TABLE_NAME)) <= 0)
            return false;

        path = path.substring(path.lastIndexOf(TABLE_NAME) + TABLE_NAME.length());

        if (path.isEmpty() || path.equals("/")){
            switch (method){
                case "GET":
                    new ListFoodRR(req, res, con).serve();
                    break;
                case "POST":
                    new RegisterFoodRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsopported operation for URI /%s: %s.", TABLE_NAME, method);
                    throw new UnsupportedOperationException();
            }
        } else if(path.contains("id") && method.equals("GET"))
            new GetFoodByIdRR(req, res, con).serve();
        else{
            LOGGER.warn("Unsopported operation for URI /%s: %s.", TABLE_NAME, method);
            throw new UnsupportedOperationException();
        }
        return true;
    }
}

// http://localhost:8080/cycleK-1.0.0/rest/foods         GET
// http://localhost:8080/cycleK-1.0.0/rest/foods/id/1    GET
