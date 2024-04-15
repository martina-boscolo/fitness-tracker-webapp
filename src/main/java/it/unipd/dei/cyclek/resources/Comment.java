package it.unipd.dei.cyclek.resources;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Represents a comment on a social network post with various attributes such as comment ID, post ID, user ID, comment text, and comment date.
 *
 * @author Martina Boscolo Bacheto
 *
 */
public class Comment {
    private final int commentId;
    private final int postId;
    private final int userId;
    private final String commentText;

    /**
     * Constructs a new Comment with the given attributes.
     *
     * @param commentId   the ID of the comment
     * @param postId      the ID of the post that the comment is on
     * @param userId      the ID of the user who made the comment
     * @param commentText the text of the comment
     */
    public Comment(int commentId, int postId, int userId, String commentText, String commentDate) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.commentText = commentText;
    }

    /**
     * Returns the ID of the comment.
     *
     * @return the ID of the comment
     */
    public int getCommentId() {
        return commentId;
    }


    /**
     * Returns the ID of the post that the comment is on.
     *
     * @return the ID of the post
     */
    public int getPostId() {
        return postId;
    }


    /**
     * Returns the ID of the user who made the comment.
     *
     * @return the ID of the user
     */
    public int getUserId() {
        return userId;
    }


    /**
     * Returns the text of the comment.
     *
     * @return the text of the comment
     */
    public String getCommentText() {
        return commentText;
    }


}