package it.unipd.dei.cyclek.rest.food;

import it.unipd.dei.cyclek.dao.food.GetFoodDao;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.entity.Food;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetFoodByIdRR extends AbstractRR {
    public GetFoodByIdRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.FOOD_BY_ID, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List <Food> foodList;
        Message m;

        try {
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("id") + 2);
            final Integer id = Integer.valueOf(path.substring(1));

            Food food = new Food(id);
            // creates a new DAO for accessing the database and lists the food(s)
            foodList = new GetFoodDao(con, food).access().getOutputParam();
            if (foodList != null && foodList.size() == 1) {
                LOGGER.info("Food(s) successfully find.");
                res.setStatus(HttpServletResponse.SC_OK);
                foodList.get(0).toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Fatal error while finding Food(s).");
                m = new Message("Cannot find Food(s): unexpected error.", "E5A1", null);
                res.setStatus(ErrorCode.GET_FOOD_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
                m = ErrorCode.GET_FOOD_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.GET_FOOD_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot find Food(s): unexpected database error.", ex);
            m = ErrorCode.GET_FOOD_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_FOOD_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}
