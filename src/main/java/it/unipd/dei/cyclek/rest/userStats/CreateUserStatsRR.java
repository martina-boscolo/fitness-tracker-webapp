package it.unipd.dei.cyclek.rest.userStats;

import it.unipd.dei.cyclek.dao.userStats.CreateUserStatsDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.resources.entity.UserStats;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class CreateUserStatsRR extends AbstractRR {
    public CreateUserStatsRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.CREATE_BODY_STATS, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        UserStats bs = null;
        Message m = null;

        try {
            final UserStats userStats = UserStats.fromJSON(req.getInputStream());

            // creates a new DAO for accessing the database and lists the employee(s)
            bs = new CreateUserStatsDAO(con, userStats).access().getOutputParam();

            if (bs == null) {
                LOGGER.error("Fatal error while creating Body Stats.");
                m = ErrorCode.CREATE_STATS_INTERNAL_SERVER_ERROR.getMessage();
                res.setStatus(ErrorCode.CREATE_STATS_INTERNAL_SERVER_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }

            LOGGER.info("Body Stats successfully created.");
            res.setStatus(HttpServletResponse.SC_CREATED);
            bs.toJSON(res.getOutputStream());

        } catch (EOFException ex) {
            LOGGER.warn("Cannot create the Body Stats: no Body Stats JSON object found in the request.", ex);
            m = ErrorCode.CREATE_STATS_JSON_ERROR.getMessage();
            res.setStatus(ErrorCode.CREATE_STATS_JSON_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());

        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot create the Body Stats: it already exists.");
                m = ErrorCode.CREATE_STATS_DB_CONFLICT.getMessage();
                res.setStatus(ErrorCode.CREATE_STATS_DB_CONFLICT.getHttpCode());
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot create the Body Stats: unexpected database error.", ex);
                m = ErrorCode.CREATE_STATS_INTERNAL_SERVER_ERROR.getMessage();
                res.setStatus(ErrorCode.CREATE_STATS_INTERNAL_SERVER_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }

        } catch (NullPointerException ex) {
            LOGGER.error("Body must contains all parameters (idUser,weight,height,fatty,lean,statsDate).", ex);
            m = ErrorCode.CREATE_STATS_NULL_POINTER.getMessage();
            res.setStatus(ErrorCode.CREATE_STATS_NULL_POINTER.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}
