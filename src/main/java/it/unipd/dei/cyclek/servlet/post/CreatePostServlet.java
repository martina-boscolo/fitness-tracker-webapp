package it.unipd.dei.cyclek.servlet.post;

import it.unipd.dei.cyclek.dao.post.CreatePostDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.entity.Post;
import it.unipd.dei.cyclek.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.awt.datatransfer.MimeTypeParseException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CreatePostServlet extends AbstractDatabaseServlet {


    /**
     * Creates a new post into the database.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     *
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException      if any error occurs in the client/server communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.CREATE_POST);


        // model
        Post p = null;
        Message m = null;

        try {

            p = parseRequest(req);

            LogContext.setResource(Integer.toString(p.getPostId()));


            CreatePostDAO dao =  new CreatePostDAO(getConnection(), p);
            dao.access();
             p = dao.getOutputParam();

            m = new Message(String.format("Post %d successfully created.", p.getPostId()));

            LOGGER.info("Post %s successfully created in the database.", p.getPostId());

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot create the post. Invalid input parameters: postId, userId, likeCount,commentCount must be integer.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot create the post. Invalid input parameters: postId, userId, likeCount,commentCount must be integer.",
                    ex);
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                m = new Message(String.format("Cannot create the post: post %d already exists.", p.getPostId()), "E300",
                        ex.getMessage());

                LOGGER.error(
                        new StringFormattedMessage("Cannot create the post: post %d already exists.", p.getPostId()),
                        ex);
            } else {
                m = new Message("Cannot create the post: unexpected error while accessing the database.", "E200",
                        ex.getMessage());

                LOGGER.error("Cannot create the post: unexpected error while accessing the database.", ex);
            }
        } catch (MimeTypeParseException e) {
            m = new Message(
                    String.format("Unsupported MIME media type for post photo. Expected: image/png or image/jpeg."),
                    "E400", e.getMessage());
        }
        try {
            // stores the post and the message as a request attribute
            req.setAttribute("post", p);
            req.setAttribute("message", m);

            // forwards the control to the create-post-result JSP
            req.getRequestDispatcher("/jsp/create-post-result.jsp").forward(req, res);
        } catch(Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating post %s.", p.getPostId()), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private Post parseRequest(HttpServletRequest req) throws ServletException, IOException, MimeTypeParseException {


        // request parameters
        int postId = -1;
        int userId = -1;
        String textContent = null;
        byte[] photo = null;
        String photoMediaType = null;
        Timestamp postDate = new Timestamp(System.currentTimeMillis());
        String username = null;
        int likeCount = 0;
        int commentCount = 0;

        // retrieves the request parameters
        for (Part p : req.getParts()) {

            switch (p.getName()) {
                case "postId":

                    try (InputStream is = p.getInputStream()) {
                        postId = Integer.parseInt(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                    }
                    break;
                case "userId":

                    try (InputStream is = p.getInputStream()) {
                        userId = Integer.parseInt(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                    }
                    break;

                case "textContent":
                    try (InputStream is = p.getInputStream()) {
                        textContent = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                    }
                    break;


                case "photo":
                    photoMediaType = p.getContentType();

                    switch (photoMediaType.toLowerCase().trim()) {

                        case "image/png":
                        case "image/jpeg":
                        case "image/jpg":
                            // nothing to do
                            break;

                        default:
                            LOGGER.error("Unsupported MIME media type %s for post photo.", photoMediaType);

                            throw new MimeTypeParseException(
                                    String.format("Unsupported MIME media type %s for post photo.",
                                            photoMediaType));
                    }

                    try (InputStream is = p.getInputStream()) {
                        photo = is.readAllBytes();
                    }

                    break;
                case "postDate":
                    try (InputStream is = p.getInputStream()) {
                        postDate = Timestamp.valueOf(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                    }
                    break;
                case "username":
                    try (InputStream is = p.getInputStream()) {
                        username = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                    }
                    break;
                case "likeCount":
                    try (InputStream is = p.getInputStream()) {
                        likeCount = Integer.parseInt(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                    }
                    break;
                case "commentCount":
                    try (InputStream is = p.getInputStream()) {
                        commentCount = Integer.parseInt(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                    }
                    break;

            }

        }

        // creates a new post from the request parameters
        return new Post( postId, userId, textContent, photo, photoMediaType, postDate, username, likeCount, commentCount);
    }



}