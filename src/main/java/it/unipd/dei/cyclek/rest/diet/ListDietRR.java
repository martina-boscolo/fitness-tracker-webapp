package it.unipd.dei.cyclek.rest.diet;

import it.unipd.dei.cyclek.dao.diets.GetDietDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListDietRR extends AbstractRR {

    public ListDietRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_DIET, req, res, con);
    }
    @Override
    protected void doServe() throws IOException {
        List<Diet> dl = null;
        Message m = null;

        try {

            String path = req.getRequestURI();

            Diet diet = new Diet(null,null,"",null);

            // creates a new DAO for accessing the database and lists the employee(s)
            dl = new GetDietDAO(con, diet).access().getOutputParam();

            if (dl != null) {
                LOGGER.info("Diet(s) successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(dl).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing diet(s).");

                m = new Message("Cannot list diet(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list diet(s): unexpected database error.", ex);

            m = new Message("Cannot list diet(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }


}

