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

public class GetDietByUserIdRR extends AbstractRR {

    public GetDietByUserIdRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.GET_DIET_USER_ID, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<Diet> dl;
        Message m;

        try {

            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("idUser") + 6);
            final int idUser = Integer.parseInt(path.substring(1));

            dl = new GetDietDAO(con, new Diet(idUser, "", null)).access().getOutputParam();

            if (dl != null && !dl.isEmpty()) {
                LOGGER.info("Diet(s) successfully listed.");
                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(dl).toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Fatal error while listing diet(s).");
                m = ErrorCode.ID_USER_DIET_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.ID_USER_DIET_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list diet(s): unexpected database error.", ex);
            m = ErrorCode.GET_ID_USER_DIET_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_ID_USER_DIET_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}