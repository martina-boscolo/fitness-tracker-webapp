package it.unipd.dei.cyclek.servlet.socialNetwork;

import it.unipd.dei.cyclek.dao.socialNetworkPost.CreateSocialNetworkPostDAO;
import it.unipd.dei.cyclek.dao.socialNetworkPost.ListSocialNetworkPostDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.SocialNetworkPost;
import it.unipd.dei.cyclek.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListSocialNetworkPostServlet extends AbstractDatabaseServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        // request parameters

        int userId = 1;




        // model
        SocialNetworkPost p = null;
        Message m = null;

        List<SocialNetworkPost> posts = null;
        try {
            // retrieves the request parameters
            userId = Integer.parseInt(req.getParameter("userId"));

            // creates a new object for accessing the database and retrieves the posts
            new ListSocialNetworkPostDAO(getConnection()).access();

            m = new Message(String.format("Post list for user %d successfully created.", userId));

            LOGGER.info("Post list for user %d successfully created in the database.", userId);

            // stores the posts and the message as a request attribute
            req.setAttribute("posts", posts);
            req.setAttribute("message", m);

            // forwards the control to the list-posts-result JSP
            req.getRequestDispatcher("/jsp/list-posts-result.jsp").forward(req, res);
        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot create the post list. Invalid input parameters: userId must be integer.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot create the post list. Invalid input parameters: userId must be integer.",
                    ex);
        } catch (SQLException ex) {
            m = new Message("Cannot create the post list: unexpected error while accessing the database.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot create the post list: unexpected error while accessing the database.", ex);
        }



    }
}
