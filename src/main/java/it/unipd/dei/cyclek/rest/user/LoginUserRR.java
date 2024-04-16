package it.unipd.dei.cyclek.rest.user;

import it.unipd.dei.cyclek.dao.user.GetUserDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.User;
import it.unipd.dei.cyclek.rest.AbstractRR;
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
        List<User> ul ;
        Message m;
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            LOGGER.info("student {} is trying to login", username);
            User user = new User(username, password);
            ul = new GetUserDAO(con, user).access().getOutputParam();
            if (ul == null || ul.size() > 1) {
                m = new Message("The User does not exist", "E200", "Missing User");
                LOGGER.error("problems with student: {}", m.getMessage());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {

        }
    }
}

/*{
    username : IlPazzo
    password : Vicenza
        }*/