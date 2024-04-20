package it.unipd.dei.cyclek.rest.meal;

import it.unipd.dei.cyclek.dao.meal.GetMealDao;
import it.unipd.dei.cyclek.resources.Meal;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.ResourceList;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import it.unipd.dei.cyclek.resources.Actions;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MealWithCaloriesRR extends AbstractRR {
    /**
     * Creates a new REST resource.
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    protected MealWithCaloriesRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_CALORIES_MEAL, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<Meal> fl = null;
        Message m = null;
        ArrayList list = new ArrayList();
        try {

            String path = req.getRequestURI();

            Meal meal = new Meal(null, null, null, null, null);

            // creates a new DAO for accessing the database and lists the employee(s)
            fl = new GetMealDao(con, meal).access().getOutputParam();

            for(Meal itr: fl){

            }

            if (fl != null) {
                LOGGER.info("Meal(s) successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(fl).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing meal(s).");

                m = new Message("Cannot list meal(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list meal(s): unexpected database error.", ex);

            m = new Message("Cannot list meal(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
