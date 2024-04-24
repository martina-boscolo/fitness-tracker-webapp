package it.unipd.dei.cyclek.rest.user;

import it.unipd.dei.cyclek.dao.user.RegisterUserDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.User;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
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
        User u;
        Message m;
        try {
            final User user = User.fromJSON(req.getInputStream());
            if (!user.isValid()) {
                m = ErrorCode.REGISTER_USER_BAD_REQUEST.getMessage();
                res.setStatus(ErrorCode.REGISTER_USER_BAD_REQUEST.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }
            u = new RegisterUserDAO(con, user).access().getOutputParam();
            if (u == null) {
                LOGGER.error("Cannot add the user(s): missing User fields.");
                m = ErrorCode.REGISTER_USER_INTERNAL_SERVER_ERROR.getMessage();
                res.setStatus(ErrorCode.REGISTER_USER_INTERNAL_SERVER_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }
            LOGGER.info("User(s) successfully added to database.");
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (PSQLException ex) {
            if (ex.getSQLState().equals("23505")) {
                LOGGER.error("Cannot add the user(s): constraint violated.", ex);
                m = ErrorCode.REGISTER_USER_CONSTRAINT_VIOLATION.getMessage();
                res.setStatus(ErrorCode.REGISTER_USER_CONSTRAINT_VIOLATION.getHttpCode());
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot add the user(s): unexpected database error.", ex);
                m = ErrorCode.REGISTER_USER_DB_ERROR.getMessage();
                res.setStatus(ErrorCode.REGISTER_USER_DB_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot add the user(s): unexpected database error.", ex);
            m = ErrorCode.REGISTER_USER_DB_ERROR.getMessage();
            res.setStatus(ErrorCode.REGISTER_USER_DB_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }

    }

}
