package it.unipd.dei.cyclek.resources;

/**
 * Represents a like or dislike action on a social network post with various attributes such as like/dislike ID, user ID, post ID, and whether it's a like or dislike.
 */
public class Like {
    private final int likeId;
    private final int userId;
    private final int postId;
    private final boolean isLike;

    /**
     * Constructs a new LikeOrDislike with the given attributes.
     *
     * @param likeDislikeId the ID of the like/dislike
     * @param userId        the ID of the user who made the like/dislike
     * @param postId        the ID of the post that was liked/disliked
     * @param isLike        whether the action was a like (true) or dislike (false)
     */
    public Like(int likeDislikeId, int userId, int postId, boolean isLike) {
        this.likeId = likeDislikeId;
        this.userId = userId;
        this.postId = postId;
        this.isLike = isLike;
    }

    /**
     * Returns the ID of the like/dislike.
     *
     * @return the ID of the like/dislike
     */
    public int getLikeId() {
        return likeId;
    }


    /**
     * Returns the ID of the user who made the like/dislike.
     *
     * @return the ID of the user
     */
    public int getUserId() {
        return userId;
    }


    /**
     * Returns the ID of the post that was liked/disliked.
     *
     * @return the ID of the post
     */
    public int getPostId() {
        return postId;
    }


    /**
     * Returns whether the action was a like or dislike.
     *
     * @return true if the action was a like, false if it was a dislike
     */
    public boolean isLike() {
        return isLike;
    }


}