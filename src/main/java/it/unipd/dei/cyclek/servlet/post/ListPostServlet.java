package it.unipd.dei.cyclek.servlet.post;

import it.unipd.dei.cyclek.dao.post.ListPostDAO;
import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.Post;
import it.unipd.dei.cyclek.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListPostServlet extends AbstractDatabaseServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        // request parameters

        int userId = 1;




        // model
        Post p = null;
        Message m = null;

        List<Post> posts = null;
        try {
            // retrieves the request parameters
            userId = Integer.parseInt(req.getParameter("userId"));

            // creates a new object for accessing the database and retrieves the posts
            new ListPostDAO(getConnection()).access();

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
