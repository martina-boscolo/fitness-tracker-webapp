package it.unipd.dei.cyclek.rest.food;

import it.unipd.dei.cyclek.dao.food.GetFoodDao;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListFoodRR extends AbstractRR {
    public ListFoodRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_FOOD, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<Food> fl;
        Message m;

        try {

            String path = req.getRequestURI();

            Food food = new Food(null,null,null,null, null);
            fl = new GetFoodDao(con, food).access().getOutputParam();
            if (fl != null) {
                LOGGER.info("Food(s) successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(fl).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing food(s).");
                m = ErrorCode.FOOD_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.FOOD_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list food(s): unexpected database error.", ex);
            m = ErrorCode.FOOD_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.FOOD_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}