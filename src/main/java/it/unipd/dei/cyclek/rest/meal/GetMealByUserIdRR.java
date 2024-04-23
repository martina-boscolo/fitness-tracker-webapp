package it.unipd.dei.cyclek.rest.meal;

import it.unipd.dei.cyclek.dao.meal.GetMealDao;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Meal;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.ResourceList;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetMealByUserIdRR extends AbstractRR {

    public GetMealByUserIdRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.GET_MEAL_USER_ID, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<Meal> ml;
        Message m;

        try {
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("idUser") + 6);
            final int idUser = Integer.parseInt(path.substring(1));
            ml = new GetMealDao(con, new Meal(idUser)).access().getOutputParam();

            if (ml != null) {
                LOGGER.info("meal(s) successfully listed.");
                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(ml).toJSON(res.getOutputStream());
            } else {
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