package it.unipd.dei.cyclek.rest.bodyObjective;

import it.unipd.dei.cyclek.dao.bodyObjective.GetBodyObjDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListBodyObjRR extends AbstractRR {

    public ListBodyObjRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_BODY_OBJ, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<BodyObj> bol = null;
        Message m = null;

        try {
            BodyObj bodyObj = new BodyObj(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "");

            // creates a new DAO for accessing the database and lists the employee(s)
            bol = new GetBodyObjDAO(con, bodyObj).access().getOutputParam();

            if (bol != null) {
                LOGGER.info("Body Objective successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(bol).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing Body Objective.");

                m = new Message("Cannot list Body Objective: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list Body Objective: unexpected database error.", ex);

            m = new Message("Cannot list Body Objective: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}