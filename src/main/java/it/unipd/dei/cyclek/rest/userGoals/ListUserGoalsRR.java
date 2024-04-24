package it.unipd.dei.cyclek.rest.userGoals;

import it.unipd.dei.cyclek.dao.userGoals.GetUserGoalsDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListUserGoalsRR extends AbstractRR {

    public ListUserGoalsRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_BODY_OBJ, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<UserGoals> bol = null;
        Message m = null;

        try {
            // creates a new DAO for accessing the database and lists the employee(s)
            bol = new GetUserGoalsDAO(con).access().getOutputParam();

            if (bol == null) {
                LOGGER.error("Fatal error while listing Goals.");

                m = ErrorCode.GET_GOAL_INTERNAL_SERVER_ERROR.getMessage();
                res.setStatus(ErrorCode.GET_GOAL_INTERNAL_SERVER_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }

            LOGGER.info("Goals successfully listed.");
            res.setStatus(HttpServletResponse.SC_OK);
            new ResourceList<>(bol).toJSON(res.getOutputStream());

        } catch (SQLException ex) {
            LOGGER.error("Cannot list Goals: unexpected database error.", ex);

            m = ErrorCode.GET_GOAL_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_GOAL_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}