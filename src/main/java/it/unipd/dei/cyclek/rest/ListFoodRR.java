package it.unipd.dei.cyclek.rest;

import it.unipd.dei.cyclek.dao.foodRetriever.GetFoodDao;
import it.unipd.dei.cyclek.resources.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListFoodRR extends AbstractRR{


    /**
     * Creates a new REST resource.
     *
     * @param action the action performed by this REST resource.
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    protected ListFoodRR(String action, HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_FOOD, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<Food> fl = null;
        Message m = null;

        try {

            String path = req.getRequestURI();

            Food food = new Food(null,"",0,0,00, 00);

            // creates a new DAO for accessing the database and lists the employee(s)
            fl = new GetFoodDao(con, food).access().getOutputParam();

            if (fl != null) {
                LOGGER.info("Food(s) successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(fl).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing food(s).");

                m = new Message("Cannot list food(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list food(s): unexpected database error.", ex);

            m = new Message("Cannot list food(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
