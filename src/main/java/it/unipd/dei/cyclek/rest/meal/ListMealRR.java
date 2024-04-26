package it.unipd.dei.cyclek.rest.meal;

import it.unipd.dei.cyclek.dao.meal.GetMealDao;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListMealRR extends AbstractRR {

    public ListMealRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_MEAL, req, res, con);
    }
    @Override
    protected void doServe() throws IOException {
        List<Meal> fl;
        Message m;

        try {
            Meal meal = new Meal(null, null, null, null, null);

            // creates a new DAO for accessing the database and lists the employee(s)
            fl = new GetMealDao(con, meal).access().getOutputParam();

            if (fl != null) {
                LOGGER.info("Meal(s) successfully listed.");
                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(fl).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing diet(s).");
                m = ErrorCode.LIST_ALL_MEALS_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.LIST_ALL_MEALS_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list meal(s): unexpected database error.", ex);
            m = ErrorCode.LIST_ALL_MEAL_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.LIST_ALL_MEAL_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}
