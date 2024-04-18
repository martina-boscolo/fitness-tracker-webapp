package it.unipd.dei.cyclek.rest.bodyStats;

import it.unipd.dei.cyclek.dao.bodyStats.GetBodyStatsDAO;
import it.unipd.dei.cyclek.dao.bodyStats.GetLatestBodyStatsDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetMeanImcRR extends AbstractRR {

    public GetMeanImcRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.GET_MEAN_IMC, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<BodyStats> bsl = null;
        Message m = null;

        try {

            // creates a new DAO for accessing the database and lists the employee(s)
            bsl = new GetLatestBodyStatsDAO(con).access().getOutputParam();

            if (bsl != null) {
                LOGGER.info("Body Stats successfully listed.");

                double meanImc = 0.0;
                for (BodyStats bs : bsl)
                    meanImc += (bs.getWeight()/(bs.getHeight()*bs.getHeight()/10000));
                meanImc /= bsl.size();

                res.setStatus(HttpServletResponse.SC_OK);
                new Imc(meanImc).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing Body Stats.");

                m = new Message("Cannot list Body Stats: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list Body Stats: unexpected database error.", ex);

            m = new Message("Cannot list Body Stats: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}