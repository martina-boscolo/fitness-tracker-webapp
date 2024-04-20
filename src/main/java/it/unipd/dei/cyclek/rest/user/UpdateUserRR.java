package it.unipd.dei.cyclek.rest.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.cyclek.dao.user.GetUserDAO;
import it.unipd.dei.cyclek.dao.user.UpdateUserDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.User;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UpdateUserRR extends AbstractRR {
    public UpdateUserRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_USER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Message m;
        try {
            // take the value of the id from the url
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("id") + 2);
            final int id = Integer.parseInt(path.substring(1));
            // take the parameters values from the json request
            BufferedReader reader = req.getReader();
            StringBuilder jsonBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonBody.toString());
            String name = rootNode.get("name").asText();
            String surname = rootNode.get("surname").asText();
            String birthday = rootNode.get("birthday").asText();
            String gender = rootNode.get("gender").asText();
            String username = rootNode.get("username").asText();
            String password = rootNode.get("password").asText();
            String[] fields = {name, surname, birthday, gender, username, password};
            for(String field : fields)
                if(StringUtils.isBlank(field)) {
                    LOGGER.error("some fields are null or empty.");
                    m = new Message("Fields cant be null or empty", "E5A1", null);
                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    m.toJSON(res.getOutputStream());
                }
            User user = new User(id, name, surname, birthday, gender, username, password);
            boolean saved = new UpdateUserDAO(con, user).access().getOutputParam();
            if (saved) {
                LOGGER.info("User(s) successfully updated.");
                res.setStatus(HttpServletResponse.SC_OK);
                m = new Message("User(s) successfully updated.", "S1A1", null);
                m.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while updating user(s).");
                m = new Message("Cannot find user(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot update user(s): unexpected database error.", ex);
            m = new Message("Cannot update user(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
