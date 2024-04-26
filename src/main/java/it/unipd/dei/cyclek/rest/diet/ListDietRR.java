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
        List<Diet> dl;
        Message m;

        try {

            Diet diet = new Diet(null,"",null);

            dl = new GetDietDAO(con, diet).access().getOutputParam();

            if (dl != null) {
                LOGGER.info("Diet(s) successfully listed.");
                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(dl).toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Fatal error while listing diet(s).");
                m = ErrorCode.LIST_ALL_DIET_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.LIST_ALL_DIET_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list diet(s): unexpected database error.", ex);
            m = ErrorCode.LIST_ALL_DIET_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.LIST_ALL_DIET_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }


}

