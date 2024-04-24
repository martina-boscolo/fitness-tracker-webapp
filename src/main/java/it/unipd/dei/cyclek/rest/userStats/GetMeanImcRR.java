package it.unipd.dei.cyclek.rest.userStats;

import it.unipd.dei.cyclek.dao.userStats.GetLatestUserStatsDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetMeanImcRR extends AbstractRR {

    public GetMeanImcRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.GET_MEAN_IMC, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<UserStats> bsl = null;
        Message m = null;

        try {

            // creates a new DAO for accessing the database and lists the employee(s)
            bsl = new GetLatestUserStatsDAO(con).access().getOutputParam();

            if (bsl != null) {
                LOGGER.info("Mean IMC successfully listed.");

                double meanImc = 0.0;
                for (UserStats bs : bsl)
                    meanImc += (bs.getWeight()/(bs.getHeight()*bs.getHeight()/10000));
                meanImc /= bsl.size();

                res.setStatus(HttpServletResponse.SC_OK);
                new Imc(meanImc).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing Body Stats.");
                m = ErrorCode.GET_MEAN_IMC_INTERNAL_SERVER_ERROR.getMessage();
                res.setStatus(ErrorCode.GET_MEAN_IMC_INTERNAL_SERVER_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list Body Stats: unexpected database error.", ex);
            m = ErrorCode.GET_MEAN_IMC_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_MEAN_IMC_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}