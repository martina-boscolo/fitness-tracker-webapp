package it.unipd.dei.cyclek.rest.diet;

import it.unipd.dei.cyclek.dao.diets.GetDietDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Diet;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetDietByIdRR extends AbstractRR {

    public GetDietByIdRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.GET_DIET_ID, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<Diet> dl;
        Message m;

        try {

            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("id") + 2);
            final int id = Integer.parseInt(path.substring(1));

            dl = new GetDietDAO(con, new Diet(id)).access().getOutputParam();

            if (dl != null && dl.size() == 1) {
                LOGGER.info("Diet(s) successfully listed.");
                res.setStatus(HttpServletResponse.SC_OK);
                dl.get(0).toJSON(res.getOutputStream());
            } else {
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

