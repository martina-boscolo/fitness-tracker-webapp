package it.unipd.dei.cyclek.servlet.post;

import it.unipd.dei.cyclek.dao.post.CreatePostDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.Post;
import it.unipd.dei.cyclek.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CreatePostServlet extends AbstractDatabaseServlet {

    /**
     * Creates a new employee into the database.
     *
     * @param req
     *            the HTTP request from the client.
     * @param res
     *            the HTTP response from the server.
     *
     * @throws ServletException
     *             if any error occurs while executing the servlet.
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.CREATE_POST);

        // request parameters
        int postId = -1;
        int userId = -1;
        String textContent = "Ciao quest a è una prova!";
        String imagePath = "";
        Timestamp postDate = new Timestamp(System.currentTimeMillis());



        // model
        Post p = null;
        Message m = null;

        try {
            // retrieves the request parameters
            postId = Integer.parseInt(req.getParameter("postId"));
            userId = Integer.parseInt(req.getParameter("userId"));
            textContent = req.getParameter("textContent");
            imagePath = req.getParameter("imagePath");
            if (req.getParameter("postDate") == null || req.getParameter("postDate").isEmpty())
                postDate = new Timestamp(System.currentTimeMillis());
            else
                postDate = Timestamp.valueOf(req.getParameter("postDate"));





            // set the badge of the employee as the resource in the log context
            // at this point we know it is a valid integer
            LogContext.setResource(req.getParameter("postId"));

            // creates a new employee from the request parameters

            p = new Post(postId, userId, textContent, imagePath, postDate);

            // creates a new object for accessing the database and stores the employee
            new CreatePostDAO(getConnection(), p).access();

            m = new Message(String.format("Post %d successfully created.", postId));

            LOGGER.info("Post %d successfully created in the database.", postId);

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot create the post. Invalid input parameters: postId, userId, likeCount,commentCount must be integer.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot create the post. Invalid input parameters: postId, userId, likeCount,commentCount must be integer.",
                    ex);
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                m = new Message(String.format("Cannot create the post: post %d already exists.", postId), "E300",
                        ex.getMessage());

                LOGGER.error(
                        new StringFormattedMessage("Cannot create the post: post %d already exists.", postId),
                        ex);
            } else {
                m = new Message("Cannot create the post: unexpected error while accessing the database.", "E200",
                        ex.getMessage());

                LOGGER.error("Cannot create the post: unexpected error while accessing the database.", ex);
            }
        }

        try {
            // stores the employee and the message as a request attribute
            req.setAttribute("post", p);
            req.setAttribute("message", m);

            // forwards the control to the create-employee-result JSP
            req.getRequestDispatcher("/jsp/create-post-result.jsp").forward(req, res);
        } catch(Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating post %d.", postId), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }

    }



}