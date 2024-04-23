package it.unipd.dei.cyclek.rest.userStats;

import it.unipd.dei.cyclek.dao.userStats.GetUserStatsByUserIdDAO;
import it.unipd.dei.cyclek.dao.userStats.GetUserStatsDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.UserStats;
import it.unipd.dei.cyclek.resources.Imc;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetImcByUserIdRR extends AbstractRR {

    public GetImcByUserIdRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.GET_IMC_BY_IDUSER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<UserStats> bsl = null;
        Message m = null;

        try {

            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("user") + 4);
            final int idUser = Integer.parseInt(path.substring(1));

            // creates a new DAO for accessing the database and lists the employee(s)
            bsl = new GetUserStatsByUserIdDAO(con, idUser).access().getOutputParam();

            if (bsl != null) {
                LOGGER.info("Body Stats successfully listed.");

                Integer userId = bsl.get(0).getIdUser();
                double imc = bsl.get(0).getWeight() / (bsl.get(0).getHeight() * bsl.get(0).getHeight() / 10000);
                double userMeanImc = 0.0;
                for (UserStats bs : bsl)
                    userMeanImc += (bs.getWeight()/(bs.getHeight()*bs.getHeight()/10000));
                userMeanImc /= bsl.size();

                res.setStatus(HttpServletResponse.SC_OK);
                new Imc(userId, userMeanImc, imc).toJSON(res.getOutputStream());
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