package it.unipd.dei.cyclek.resources;

/**
 * Represents a like action on a social network post
 *
 * @author Martina Boscolo Bacheto
 * 
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
     * @param isLike        whether the action was a like
     */
    public Like(int likeDislikeId, int userId, int postId, boolean isLike) {
        this.likeId = likeDislikeId;
        this.userId = userId;
        this.postId = postId;
        this.isLike = isLike;
    }

    /**
     * Returns the ID of the like.
     *
     * @return the ID of the like
     */
    public int getLikeId() {
        return likeId;
    }


    /**
     * Returns the ID of the user who made the like.
     *
     * @return the ID of the user
     */
    public int getUserId() {
        return userId;
    }


    /**
     * Returns the ID of the post that was liked.
     *
     * @return the ID of the post
     */
    public int getPostId() {
        return postId;
    }


    /**
     * Returns whether the action was a like.
     *
     * @return true if the action was a like
     */
    public boolean isLike() {
        return isLike;
    }


}