package it.unipd.dei.cyclek.rest.userGoals;

import it.unipd.dei.cyclek.dao.userGoals.GetUserGoalsDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.UserGoals;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.ResourceList;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListUserGoalsByUserIdRR extends AbstractRR {

    public ListUserGoalsByUserIdRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_BODY_OBJ_BY_IDUSER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<UserGoals> bsl = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("user") + 4);
            final int idUser = Integer.parseInt(path.substring(1));

            UserGoals userGoals = new UserGoals(
                    null,
                    idUser,
                    null,
                    null,
                    null,
                    null,
                    "");

            // creates a new DAO for accessing the database and lists the employee(s)
            bsl = new GetUserGoalsDAO(con, userGoals).access().getOutputParam();

            if (bsl != null) {
                LOGGER.info("Body Obj successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(bsl).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing Body Obj.");

                m = new Message("Cannot list Body Obj: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list Body Obj: unexpected database error.", ex);

            m = new Message("Cannot list Body Obj: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}