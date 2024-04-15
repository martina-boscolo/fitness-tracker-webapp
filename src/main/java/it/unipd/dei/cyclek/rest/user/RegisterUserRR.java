package it.unipd.dei.cyclek.rest.user;

import it.unipd.dei.cyclek.dao.user.RegisterUserDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.User;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class RegisterUserRR extends AbstractRR {

    public RegisterUserRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.ADD_USER, req, res, con);
    }
    @Override
    protected void doServe() throws IOException {
        User u ;
        Message m;
        try {
            final User user = User.fromJSON(req.getInputStream());
            u = new RegisterUserDAO(con, user).access().getOutputParam();
            if (u != null) {
                LOGGER.info("User(s) successfully added to database.");
                res.setStatus(HttpServletResponse.SC_OK);

            } else { // it should not happen
                LOGGER.error("Fatal error while adding user(s).");
                m = new Message("Cannot add user(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }

        } catch (PSQLException ex) {
            LOGGER.error("Cannot add the user(s): duplicate usernames.", ex);
            m = new Message("Cannot add user(s): duplicate usernames.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
        catch (SQLException ex) {
            LOGGER.error("Cannot add the user(s): unexpected database error.", ex);
            m = new Message("Cannot add user(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }

    }

}
