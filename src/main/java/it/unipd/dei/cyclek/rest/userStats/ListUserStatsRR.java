package it.unipd.dei.cyclek.rest.userStats;

import it.unipd.dei.cyclek.dao.userStats.GetUserStatsDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.resources.entity.UserStats;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListUserStatsRR extends AbstractRR {

    public ListUserStatsRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_BODY_STATS, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<UserStats> bsl = null;
        Message m = null;

        try {

            // creates a new DAO for accessing the database and lists the employee(s)
            bsl = new GetUserStatsDAO(con).access().getOutputParam();

            if (bsl == null) {
                LOGGER.error("No stats found.");
                m = ErrorCode.GET_STATS_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.GET_STATS_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }

            LOGGER.info("Stats successfully listed.");
            res.setStatus(HttpServletResponse.SC_OK);
            new ResourceList<>(bsl).toJSON(res.getOutputStream());

        } catch (SQLException ex) {
            LOGGER.error("Cannot list Stats: unexpected database error.", ex);
            m = ErrorCode.GET_STATS_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_STATS_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}