package it.unipd.dei.cyclek.servlet.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unipd.dei.cyclek.dao.user.GetUserDAO;
import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.User;
import it.unipd.dei.cyclek.servlet.AbstractDatabaseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Searches user by their id.
 */
public class GetUserServlet extends AbstractDatabaseServlet {

    /**
     * Searches user by their id.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.SEARCH_USER_BY_ID);

        // request parameter
        int id = -1;

        // model
        User user = null;

        try {

            // retrieves the request parameter
            id = Integer.parseInt(req.getParameter("id"));

            // creates a new object for accessing the database and searching the employees
            user = new GetUserDAO(getConnection(), id).access().getOutputParam();

            LOGGER.info("User successfully searched by id {}.", id);

        } catch (NumberFormatException ex) {
            LOGGER.error("Cannot search for users. Id must be an integer", ex);
        } catch (SQLException ex) {
            LOGGER.error("Cannot search for employees: unexpected error while accessing the database.", ex);
        }

        try {
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();

            ObjectMapper objectMapper = new ObjectMapper();
            String json = "";
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("result", user != null);
            rootNode.set("user", objectMapper.valueToTree(user));

            json = objectMapper.writeValueAsString(rootNode);
            out.write(json);

        } catch (IOException ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when searching user id %d.", id), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }
}
