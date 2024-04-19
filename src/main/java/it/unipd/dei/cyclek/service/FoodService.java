package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.dao.food.GetFoodDao;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.food.ListFoodRR;
import it.unipd.dei.cyclek.rest.food.RegisterFoodRR;
import it.unipd.dei.cyclek.rest.meal.ListMealRR;
import it.unipd.dei.cyclek.service.AbstractService;
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

        // Gestione delle azioni in base al percorso e al metodo HTTP
        if (path.isEmpty() || path.equals("/")){
            switch (method){
                case "GET":
                    new ListFoodRR(req, res, con).serve();
                    break;
                case "POST":
                    new RegisterFoodRR(req, res, con).serve();
                    break;
                default:
                    unsupportedOperation(req, res);
                    break;
            }
        }
        return true;
    }

    private static void unsupportedOperation(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String method = req.getMethod();
        String msg = String.format("Unsupported operation for URI /%s: %s.",TABLE_NAME,method);
        LOGGER.warn(msg);
        Message m = new Message(msg,"E4A5",String.format("Method %s,",method));
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        m.toJSON(res.getOutputStream());
    }
}
