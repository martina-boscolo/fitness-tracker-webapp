package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.resources.ErrorCode;
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
                    break;
                case "POST":
                    new CreatePostRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post: %s.", method);
                    m = new Message("Unsupported operation for URI /post.", "-902", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.matches("/like/\\d+$")) {
            if (method.equals("DELETE")) {
                new DeleteLikeRR(req, res, con).serve();
            } else {
                LOGGER.warn("Unsupported operation for URI /post/{postId}/like/{likeId}: %s.", method);

                m = new Message("Unsupported operation for URI /post/{postId}/like/{likeId}.", "-902", String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                m.toJSON(res.getOutputStream());
            }
        } else if (path.matches("/\\d+/like/count$")) {

            if (method.equals("GET")) {
                new CountLikeRR(req, res, con).serve();
            } else {
                LOGGER.warn("Unsupported operation for URI /post/{postId}/like/count: %s.", method);

                m = new Message("Unsupported operation for URI /post/{postId}/like/count.", "-902", String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                m.toJSON(res.getOutputStream());
            }
        } else if (path.matches("/\\d+/like$")) {

            switch (method) {
                case "GET":
                    new ListLikeRR(req, res, con).serve();
                    break;
                case "POST":
                    new CreateLikeRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}/like: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}/like.", "-902", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.matches("/like$")) {

            if (method.equals("POST")) {
                new CreateLikeRR(req, res, con).serve();
            } else {
                LOGGER.warn("Unsupported operation for URI /post/like: %s.", method);

                m = new Message("Unsupported operation for URI /post/{postId}/like.", "-902", String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                m.toJSON(res.getOutputStream());
            }
        } else if (path.matches("/\\d+/comment/count$")) {
            ;

            if (method.equals("GET")) {
                new CountCommentRR(req, res, con).serve();
            } else {
                LOGGER.warn("Unsupported operation for URI /post/{postId}/comment/count: %s %s.", method);

                m = new Message("Unsupported operation for URI /post/{postId}/comment/count.", "-902", String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                m.toJSON(res.getOutputStream());
            }

        } else if (path.matches("/comment/\\d+$")) {

            if (method.equals("DELETE")) {
                new DeleteCommentRR(req, res, con).serve();
            } else {
                LOGGER.warn("Unsupported operation for URI /post/{postId}/comment/{commentId}: %s %s.", method);

                m = new Message("Unsupported operation for URI /post/{postId}/comment/{commentId}.", "-902", String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                m.toJSON(res.getOutputStream());
            }
        } else if (path.matches("/\\d+/comment$")) {

            if (method.equals("GET")) {
                new ListCommentRR(req, res, con).serve();
            } else {
                LOGGER.warn("Unsupported operation for URI /post/{postId}/comment: %s.", method);

                m = new Message("Unsupported operation for URI /post/{postId}/comment.", "-902", String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                m.toJSON(res.getOutputStream());
            }
        } else if (path.matches("/comment$")) {

            if (method.equals("POST")) {
                new CreateCommentRR(req, res, con).serve();
            } else {
                LOGGER.warn("Unsupported operation for URI /post/{postId}/comment: %s.", method);

                m = new Message("Unsupported operation for URI /post/{postId}/comment.", "-902", String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                m.toJSON(res.getOutputStream());
            }
        } else if (path.matches("/\\d+$")) {
            switch (method) {
                case "GET":
                    new GetPostRR(req, res, con).serve();
                    break;
                case "PUT":
                    new UpdatePostRR(req, res, con).serve();
                    break;
                case "DELETE":
                    new DeletePostRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /post/{postId}: %s.", method);

                    m = new Message("Unsupported operation for URI /post/{postId}.", "-902", String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.matches("/user/\\d+$")) {

            if (method.equals("GET")) {
                new ListPostByUserRR(req, res, con).serve();
            } else {
                LOGGER.warn("Unsupported operation for URI /post/user/{userId}: %s ", method);

                m = new Message("Unsupported operation for URI /post/user/{userId}.", "-902", String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                m.toJSON(res.getOutputStream());
            }
        }else{
           // String method = req.getMethod();
            String msg = String.format("Unsupported operation for URI /%s: %s.",path,method);
            LOGGER.warn(msg);
            m = ErrorCode.UNSUPPORTED_OPERATION.getMessage();
            res.setStatus(ErrorCode.UNSUPPORTED_OPERATION.getHttpCode());
            m.toJSON(res.getOutputStream());
        }

        return true;

    }
}


