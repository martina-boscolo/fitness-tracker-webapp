package it.unipd.dei.cyclek.rest.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unipd.dei.cyclek.dao.user.GetUserDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.User;
import it.unipd.dei.cyclek.rest.AbstractRR;
import it.unipd.dei.cyclek.utils.TokenJWT;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
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
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String requestBody = sb.toString();
            String username = null;
            String password = null;
            final JsonNode node = new ObjectMapper().readTree(requestBody);
            if (node.has("username"))
                username = node.get("username").textValue();
            if (node.has("password"))
                password = node.get("password").textValue();
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
                TokenJWT token = new TokenJWT(ul.get(0).getId());
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
