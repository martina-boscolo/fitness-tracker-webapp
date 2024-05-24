package it.unipd.dei.cyclek.rest.userGoals;

import it.unipd.dei.cyclek.dao.userGoals.GetUserGoalsByUserIdDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.resources.entity.UserGoals;
import it.unipd.dei.cyclek.rest.AbstractRR;
import it.unipd.dei.cyclek.utils.TokenJWT;
import jakarta.servlet.http.Cookie;
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

            // creates a new DAO for accessing the database and lists the employee(s)
            bsl = new GetUserGoalsByUserIdDAO(con, idUser).access().getOutputParam();

            if (bsl == null) {
                LOGGER.error("No goals found.");

                m = ErrorCode.GET_USER_GOAL_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.GET_USER_GOAL_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }

            LOGGER.info("User Goals successfully listed.");
            res.setStatus(HttpServletResponse.SC_OK);
            new ResourceList<>(bsl).toJSON(res.getOutputStream());

        } catch (SQLException ex) {
            LOGGER.error("Cannot list User Goals: unexpected database error.", ex);

            m = ErrorCode.GET_USER_GOAL_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_USER_GOAL_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}