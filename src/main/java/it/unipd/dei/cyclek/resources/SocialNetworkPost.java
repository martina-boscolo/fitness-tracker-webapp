package it.unipd.dei.cyclek.resources;

import jakarta.json.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Represents a social network post with various attributes such as post ID, user ID, text content, image path, like count, dislike count, comment count, and post date.
 */
public class SocialNetworkPost {

    private final int postId;
    private final int userId;
    private final String textContent;
    private final String imagePath;
    private final int likeCount;
    private final int commentCount;
    private final Timestamp postDate;

    /**
     * Constructs a new SocialNetworkPost with the given attributes.
     *
     * @param postId the ID of the post
     * @param userId the ID of the user who created the post
     * @param textContent the text content of the post
     * @param imagePath the path to the image of the post
     * @param likeCount the number of likes the post has received
     * @param commentCount the number of comments the post has received
     * @param postDate the date the post was created
     */
    public SocialNetworkPost(int postId, int userId, String textContent, String imagePath, int likeCount, int commentCount, Timestamp postDate) {
        this.postId = postId;
        this.userId = userId;
        this.textContent = textContent;
        this.imagePath = imagePath;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.postDate = postDate;
    }

    /**
     * Returns the ID of the post.
     *
     * @return the ID of the post
     */
    public int getPostId() {
        return postId;
    }



    /**
     * Returns the ID of the user who created the post.
     *
     * @return the ID of the user
     */
    public int getUserId() {
        return userId;
    }



    /**
     * Returns the text content of the post.
     *
     * @return the text content of the post
     */
    public String getTextContent() {
        return textContent;
    }



    /**
     * Returns the path to the image of the post.
     *
     * @return the path to the image of the post
     */
    public String getImagePath() {
        return imagePath;
    }



    /**
     * Returns the number of likes the post has received.
     *
     * @return the number of likes
     */
    public int getLikeCount() {
        return likeCount;
    }



    /**
     * Returns the number of comments the post has received.
     *
     * @return the number of comments
     */
    public int getCommentCount() {
        return commentCount;
    }



    /**
     * Returns the date the post was created.
     *
     * @return the date the post was created
     */
    public Timestamp getPostDate() {
        return postDate;
    }


}