package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.comment.CreateCommentRR;
import it.unipd.dei.cyclek.rest.comment.DeleteCommentRR;
import it.unipd.dei.cyclek.rest.comment.ListCommentRR;
import it.unipd.dei.cyclek.rest.like.CreateLikeRR;
import it.unipd.dei.cyclek.rest.like.DeleteLikeRR;
import it.unipd.dei.cyclek.rest.like.ListLikeRR;
import it.unipd.dei.cyclek.rest.socialNetworkPost.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

import static java.sql.DriverManager.getConnection;

public class PostService extends AbstractService{
    public static boolean processSocialNetworkPost(final HttpServletRequest req, final HttpServletResponse res, Connection con) throws IOException {

        final String method = req.getMethod();

        String path = req.getRequestURI();
        Message m = null;

        // the requested resource was not an employee
        if (path.lastIndexOf("rest/post") <= 0) {
            return false;
        }

        // strip everything until after the /employee
        path = path.substring(path.lastIndexOf("post") + 4);

        // the request URI is: /post
        // if method GET, list post
        // if method POST, create post
        if (path.length() == 0 || path.equals("/")) {

            switch (method) {
                case "GET":
                    new ListSocialNetworkPostRR(req, res, con).serve();
                    break;
                case "POST":
                    new CreateSocialNetworkPostRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post: %s.", method);

                    m = new Message("Unsupported operation for URI /post.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.matches("/\\d+/like/\\d+$")) {
            // Extract postId and likeId from the path
            String[] parts = path.split("/");
            int postId = Integer.parseInt(parts[1]);
            int likeId = Integer.parseInt(parts[3]);

            switch (method) {

                case "DELETE":
                    new DeleteLikeRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}/like/{likeId}: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}/like/{likeId}.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.matches("/\\d+/like$")) {
            // Extract postId and likeId from the path
            String[] parts = path.split("/");
            int postId = Integer.parseInt(parts[1]);

            switch (method) {
                case "GET":
                    new ListLikeRR(req, res, con).serve();
                    break;
                case "POST":
                    new CreateLikeRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}/like: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}/like.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }else if (path.matches("/\\d+/comment/\\d+")) {

            // Extract postId and likeId from the path
            String[] parts = path.split("/");
            int postId = Integer.parseInt(parts[1]);
            int commentId = Integer.parseInt(parts[3]);

            switch (method) {

                case "DELETE":
                    new DeleteCommentRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}/comment/{commentId}: %s %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}/comment/{commentId}.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;


            }

        }else if (path.matches("/\\d+/comment")) {
            // Extract postId and likeId from the path
            String[] parts = path.split("/");
            int postId = Integer.parseInt(parts[1]);

            switch (method) {
                case "GET":
                    new ListCommentRR(req, res, con).serve();
                    break;
                case "POST":
                    new CreateCommentRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}/comment: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}/comment.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        else  if (path.matches("/\\d+$")) {
            switch (method) {
                case "GET":
                    new ReadSocialNetworkPostRR(req, res, con).serve();
                    break;
                case "PUT":
                    new UpdateSocialNetworkPostRR(req, res, con).serve();
                    break;
                case "DELETE":
                    new DeleteSocialNetworkPostRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }


        return true;

    }
}


