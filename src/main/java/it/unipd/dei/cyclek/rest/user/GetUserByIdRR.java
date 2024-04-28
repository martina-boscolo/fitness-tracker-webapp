package it.unipd.dei.cyclek.rest.user;

import it.unipd.dei.cyclek.dao.user.GetUserDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.entity.User;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetUserByIdRR extends AbstractRR {
    public GetUserByIdRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.GET_USER_BY_ID, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List <User> userList;
        Message m;

        try {
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("id") + 2);
            final int id = Integer.parseInt(path.substring(1));

            User user = new User(id);
            userList = new GetUserDAO(con, user).access().getOutputParam();
            if (userList != null && userList.size() == 1) {
                LOGGER.info("User(s) successfully find.");
                res.setStatus(HttpServletResponse.SC_OK);
                userList.get(0).toJSON(res.getOutputStream());
            }
            else {
                LOGGER.error("Fatal error while finding user(s).");
                m = new Message("Cannot find user(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());

            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot find user(s): unexpected database error.", ex);
            m = new Message("Cannot find user(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
