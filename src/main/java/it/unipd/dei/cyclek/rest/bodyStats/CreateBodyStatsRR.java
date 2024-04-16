package it.unipd.dei.cyclek.rest.bodyStats;

import it.unipd.dei.cyclek.dao.bodyStats.CreateBodyStatsDAO;
import it.unipd.dei.cyclek.dao.bodyStats.GetBodyStatsDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CreateBodyStatsRR extends AbstractRR {
    public CreateBodyStatsRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.CREATE_BODY_STATS, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        BodyStats bs = null;
        Message m = null;

        try {
            final BodyStats bodyStats = BodyStats.fromJSON(req.getInputStream());

            // creates a new DAO for accessing the database and lists the employee(s)
            bs = new CreateBodyStatsDAO(con, bodyStats).access().getOutputParam();

            if (bs != null) {
                LOGGER.info("Body Stats successfully created.");

                res.setStatus(HttpServletResponse.SC_CREATED);
                bs.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while creating Body Stats.");

                m = new Message("Cannot create Body Stats: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (EOFException ex) {
            LOGGER.warn("Cannot create the Body Stats: no Body Stats JSON object found in the request.", ex);

            m = new Message("Cannot create the Body Stats: no Body Stats JSON object found in the request.", "E4A8",
                    ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot create the Body Stats: it already exists.");

                m = new Message("Cannot create the Body Stats: it already exists.", "E5A2", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot create the Body Stats: unexpected database error.", ex);

                m = new Message("Cannot create the Body Stats: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (NullPointerException ex) {
            LOGGER.error("Body must contains all parameters (idUser,weight,height,fatty,lean,statsDate).", ex);

            m = new Message("Body must contains all parameters (idUser,weight,height,fatty,lean,statsDate).", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        }
    }
}
