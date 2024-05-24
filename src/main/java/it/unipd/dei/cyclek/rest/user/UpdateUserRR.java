package it.unipd.dei.cyclek.rest.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.cyclek.dao.user.UpdateUserDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.entity.User;
import it.unipd.dei.cyclek.rest.AbstractRR;
import it.unipd.dei.cyclek.utils.TokenJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UpdateUserRR extends AbstractRR {
    public UpdateUserRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.UPDATE_USER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Message m;
        try {
            // take the value of the id from the url
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

            // take the parameters values from the json request
            BufferedReader reader = req.getReader();
            StringBuilder jsonBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonBody.toString());
            User user = new User(idUser, rootNode.get("name").asText(),
                                         rootNode.get("surname").asText(),
                                         rootNode.get("birthday").asText(),
                                         rootNode.get("gender").asText(),
                                         rootNode.get("username").asText(),
                                         rootNode.get("password").asText());
            if (!user.isValid()) {
                LOGGER.error("Cannot update the user: missing User fields.");
                m = ErrorCode.REGISTER_USER_BAD_REQUEST.getMessage();
                res.setStatus(ErrorCode.REGISTER_USER_BAD_REQUEST.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }
            boolean saved = new UpdateUserDAO(con, user).access().getOutputParam();
            if (saved) {
                LOGGER.info("User successfully updated.");
                res.setStatus(HttpServletResponse.SC_OK);
                user.toJSON(res.getOutputStream());
            } else {
                m = ErrorCode.UPDATE_USER_DB_ERROR.getMessage();
                res.setStatus(ErrorCode.UPDATE_USER_DB_ERROR.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot update user(s): unexpected database error.", ex);
            m = ErrorCode.UPDATE_USER_DB_ERROR.getMessage();
            res.setStatus(ErrorCode.UPDATE_USER_DB_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}
