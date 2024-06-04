package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.rest.meal.GetMealByUserIdRR;
import it.unipd.dei.cyclek.rest.meal.ListMealRR;
import it.unipd.dei.cyclek.rest.meal.RegisterMealRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

public class MealService extends AbstractService {
    private static final String TABLE_NAME = "meal";
    public static boolean processMeal(HttpServletRequest req, HttpServletResponse res, Connection con) throws IOException {

        final String method = req.getMethod();
        String path = req.getRequestURI();

        // the requested resource was not a user
        if (path.lastIndexOf("rest/".concat(TABLE_NAME)) <= 0)
            return false;

        path = path.substring(path.lastIndexOf(TABLE_NAME) + TABLE_NAME.length());

        if (path.isEmpty() || path.equals("/")){
            switch (method){
                case "GET":
                    LOGGER.info("%%%%%%%%%%%%%Inside GET%%%%%%%%%%%%%");
                    new ListMealRR(req, res, con).serve();
                    break;
                case "POST":
                    LOGGER.info("%%%%%%%%%%%%%Inside POST%%%%%%%%%%%%%");
                    new RegisterMealRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsopported operation for URI /%s: %s.", TABLE_NAME, method);
                    throw new UnsupportedOperationException();
            }
        }else if(path.matches("/user(/)?") && method.equals("GET"))
            new GetMealByUserIdRR(req, res, con).serve();
        else{
            LOGGER.warn("Unsopported operation for URI /%s: %s.", TABLE_NAME, method);
            throw new UnsupportedOperationException();
        }
        return true;
    }
}

//http://localhost:8080/cycleK-1.0.0/rest/meal      GET
//get che mi restituisce il meal con le calorie

//http://localhost:8080/cycleK-1.0.0/rest/meal      POST


