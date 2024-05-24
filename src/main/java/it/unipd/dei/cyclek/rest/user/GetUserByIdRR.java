package it.unipd.dei.cyclek.rest.user;

import it.unipd.dei.cyclek.dao.user.GetUserDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.entity.User;
import it.unipd.dei.cyclek.rest.AbstractRR;
import it.unipd.dei.cyclek.utils.TokenJWT;
import jakarta.servlet.http.Cookie;
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

            User user = new User(idUser);
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
