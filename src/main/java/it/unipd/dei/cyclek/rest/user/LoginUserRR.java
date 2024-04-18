package it.unipd.dei.cyclek.rest.user;

import it.unipd.dei.cyclek.dao.user.GetUserDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.User;
import it.unipd.dei.cyclek.rest.AbstractRR;
import it.unipd.dei.cyclek.utils.TokenJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LoginUserRR extends AbstractRR {

    public LoginUserRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.ADD_USER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<User> ul;
        Message m;
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            LOGGER.info("student {} is trying to login", username);
            User user = new User(username, password);
            ul = new GetUserDAO(con, user).access().getOutputParam();
            if (ul == null) {
                m = new Message("The User does not exist", "E200", "Missing User");
                LOGGER.error("problems with student: {}", m.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            } else if (ul.size() > 1) {
                m = new Message("The Db is broken", "E666", "Missing User");
                LOGGER.error("problems with student: {}", m.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            } else {
               TokenJWT token = new TokenJWT(user.getId());
               token.toJSON(res.getOutputStream());
            }


        } catch (SQLException ex) {
            LOGGER.error("Cannot log the User: unexpected database error.", ex);
            m = new Message("Cannot log the User: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
