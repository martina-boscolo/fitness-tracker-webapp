package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.dao.like.CountLikesByPostIdDAO;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.comment.CountCommentRR;
import it.unipd.dei.cyclek.rest.comment.CreateCommentRR;
import it.unipd.dei.cyclek.rest.comment.DeleteCommentRR;
import it.unipd.dei.cyclek.rest.comment.ListCommentRR;
import it.unipd.dei.cyclek.rest.like.CountLikeRR;
import it.unipd.dei.cyclek.rest.like.CreateLikeRR;
import it.unipd.dei.cyclek.rest.like.DeleteLikeRR;
import it.unipd.dei.cyclek.rest.like.ListLikeRR;
import it.unipd.dei.cyclek.rest.post.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

import static java.sql.DriverManager.getConnection;

public class PostService extends AbstractService {
    public static boolean processPost(final HttpServletRequest req, final HttpServletResponse res, Connection con) throws IOException {

        final String method = req.getMethod();

        String path = req.getRequestURI();
        Message m = null;

        // the requested resource was not a post
        if (path.lastIndexOf("rest/post") <= 0) {
            return false;
        }

        // strip everything until after the /post
        path = path.substring(path.lastIndexOf("post") + 4);
        if (path.length() == 0 || path.equals("/")) {

            switch (method) {
                case "GET":
                    new ListPostRR(req, res, con).serve();
                    System.out.println("GET post");
                    break;
                case "POST":
                    new CreatePostRR(req, res, con).serve();
                    System.out.println("POST post");
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post: %s.", method);

                    m = new Message("Unsupported operation for URI /post.", "E4A5", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.matches("/like/\\d+")) {
            switch (method) {

                case "DELETE":
                    new DeleteLikeRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}/like/{likeId}: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}/like/{likeId}.", "E4A5", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.matches("/\\d+/like/count")) {

            switch (method) {
                case "GET":
                    new CountLikeRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}/like/count: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}/like/count.", "E4A5", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.matches("/\\d+/like")) {

            switch (method) {
                case "GET":
                    new ListLikeRR(req, res, con).serve();
                    System.out.println("GET like");
                    break;
                case "POST":
                    new CreateLikeRR(req, res, con).serve();
                    System.out.println("POST like");
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}/like: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}/like.", "E4A5", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.matches("/like")) {

            switch (method) {

                case "POST":
                    new CreateLikeRR(req, res, con).serve();
                    System.out.println("POST like");
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/like: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}/like.", "E4A5", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.matches("/\\d+/comment/count")) {;

            switch (method) {

                case "GET":
                    new CountCommentRR(req, res, con).serve();
                    System.out.println("GET comment count");
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}/comment/count: %s %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}/comment/count.", "E4A5", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }

        } else if (path.matches("/comment/\\d+")) {

            switch (method) {

                case "DELETE":
                    new DeleteCommentRR(req, res, con).serve();
                    System.out.println("DELETE comment");
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}/comment/{commentId}: %s %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}/comment/{commentId}.", "E4A5", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.matches("/\\d+/comment")) {

            switch (method) {
                case "GET":
                    new ListCommentRR(req, res, con).serve();
                    System.out.println("GET comment");
                    break;

                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}/comment: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}/comment.", "E4A5", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.matches("/comment")) {

            switch (method) {

                case "POST":
                    new CreateCommentRR(req, res, con).serve();
                    System.out.println("POST comment");
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}/comment: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}/comment.", "E4A5", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.matches("/\\d+")) {
            switch (method) {
                case "GET":
                    new ReadPostRR(req, res, con).serve();
                    System.out.println("GET post");
                    break;
                case "PUT":
                    new UpdatePostRR(req, res, con).serve();
                    System.out.println("PUT post");
                    break;
                case "DELETE":
                    new DeletePostRR(req, res, con).serve();
                    System.out.println("DELETE post");
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}.", "E4A5", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }else if (path.matches("/user/\\d+")) {

            switch (method) {

                case "GET":
                    new ListPostByUserRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/user/{userId}: %s ", method);

                    m = new Message("Unsupported operation for URI /post/user/{userId}.", "E4A5", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        return true;

    }
}


