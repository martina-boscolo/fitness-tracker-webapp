package it.unipd.dei.cyclek.service.food;

import it.unipd.dei.cyclek.dao.food.GetFoodDao;
import it.unipd.dei.cyclek.rest.food.ListFoodRR;
import it.unipd.dei.cyclek.rest.food.RegisterFoodRR;
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
        if (path.isEmpty() || path.equals("/") && method.equals("GET")) {
            // Ottieni la lista dei cibi
            new ListFoodRR(req, res, con).serve();
        } else if (path.equals("/register") && method.equals("POST")) {
            // Registra un nuovo cibo
            new RegisterFoodRR(req, res, con).serve();
        } else {
            // Metodo HTTP non supportato
            LOGGER.warn("Unsupported operation for URI /%s: %s.", TABLE_NAME, method);
            throw new UnsupportedOperationException();
        }

        return true;
    }
}
