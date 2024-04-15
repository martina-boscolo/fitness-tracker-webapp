package it.unipd.dei.cyclek.rest.user;

import it.unipd.dei.cyclek.dao.user.GetUserDAO;
import it.unipd.dei.cyclek.resources.Actions;

import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.ResourceList;
import it.unipd.dei.cyclek.resources.User;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListUserRR extends AbstractRR {
    public ListUserRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_USER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<User> ul;
        Message m;

        try {
            User user = new User(null,"","","","", "", "");
            // creates a new DAO for accessing the database and lists the employee(s)
            ul = new GetUserDAO(con, user).access().getOutputParam();

            if (ul != null) {
                LOGGER.info("Employee(s) successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(ul).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing user(s).");
                m = new Message("Cannot list user(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list user(s): unexpected database error.", ex);
            m = new Message("Cannot list user(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
