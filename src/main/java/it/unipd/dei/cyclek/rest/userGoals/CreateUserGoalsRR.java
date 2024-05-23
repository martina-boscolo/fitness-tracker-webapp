package it.unipd.dei.cyclek.rest.userGoals;

import it.unipd.dei.cyclek.dao.userGoals.CreateUserGoalDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.entity.UserGoals;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import it.unipd.dei.cyclek.utils.TokenJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class CreateUserGoalsRR extends AbstractRR {
    public CreateUserGoalsRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.CREATE_BODY_OBJ, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        UserGoals bs;
        Message m;

        try {
            Integer idUser = null;
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("authToken".equals(cookie.getName())) {
                        String cookieValue = cookie.getValue();
                        // Assuming the cookie value directly contains the idUser
                        idUser = Integer.parseInt(TokenJWT.extractUserId(cookieValue));
                        break;
                    }
                }
            }
            if (idUser == null) {
                LOGGER.error("Unauthorized");
                m = ErrorCode.UNAUTHORIZED.getMessage();
                res.setStatus(ErrorCode.UNAUTHORIZED.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }

            UserGoals userGoals = UserGoals.fromJSON(req.getInputStream());
            userGoals.setIdUser(idUser);

            // creates a new DAO for accessing the database and lists the employee(s)
            bs = new CreateUserGoalDAO(con, userGoals).access().getOutputParam();

            if (bs == null) {
                LOGGER.error("Fatal error while creating User Goal.");
                m = ErrorCode.CREATE_GOAL_INTERNAL_SERVER_ERROR.getMessage();
                res.setStatus(ErrorCode.CREATE_GOAL_INTERNAL_SERVER_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }
            LOGGER.info("User Goal successfully created.");
            res.setStatus(HttpServletResponse.SC_CREATED);
            bs.toJSON(res.getOutputStream());

        } catch (EOFException ex) {
            LOGGER.warn("Cannot create the User Goal: no Body Obj JSON object found in the request.", ex);
            m = ErrorCode.CREATE_GOAL_JSON_ERROR.getMessage();
            res.setStatus(ErrorCode.CREATE_GOAL_JSON_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot create the User Goal: it already exists.");
                m = ErrorCode.CREATE_GOAL_DB_CONFLICT.getMessage();
                res.setStatus(ErrorCode.CREATE_GOAL_DB_CONFLICT.getHttpCode());
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot create the User Goal: unexpected database error.", ex);
                m = ErrorCode.CREATE_GOAL_INTERNAL_SERVER_ERROR.getMessage();
                res.setStatus(ErrorCode.CREATE_GOAL_INTERNAL_SERVER_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (NullPointerException ex) {
            LOGGER.error("Body must contains all parameters (idUser,weight,height,fatty,lean,goalDate).", ex);
            m = ErrorCode.CREATE_GOAL_NULL_POINTER.getMessage();
            res.setStatus(ErrorCode.CREATE_GOAL_NULL_POINTER.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}
