package it.unipd.dei.cyclek.resources;

/**
 * Contains constants for the actions performed by the application.
 *
 */
public final class Actions {

    /**
     * The search of users by their id
     */
    public static final String SEARCH_USER_BY_ID = "SEARCH_USER_BY_ID";
    public static final String LIST_USER = "LIST_USER";

    //POST ACTIONS
    public static final String CREATE_POST = "CREATE_POST";
    public static final String DELETE_POST = "DELETE_POST";
    public static final String GET_POST_BY_ID = "GET_POST_BY_ID";
    public static final String UPDATE_POST = "UPDATE_POST";
    public static final String LIST_POST = "LIST_POST";
    public static final String LIST_POST_BY_USER_ID = "LIST_POST_BY_USER_ID";

    //LIKE ACTIONS
    public static final String CREATE_LIKE = "LIST_POST";
    public static final String DELETE_LIKE = "DELETE_LIKE";
    public static final String LIST_LIKE_BY_POST_ID = "LIST_LIKE_BY_POST_ID";
    public static final String COUNT_LIKE_BY_POST_ID = "COUNT_LIKE_BY_POST_ID";

    //COMMENT ACTIONS
    public static final String CREATE_COMMENT = "CREATE_COMMENT";
    public static final String DELETE_COMMENT = "DELETE_COMMENT";
    public static final String LIST_COMMENT_BY_POST_ID = "LIST_COMMENT_BY_POST_ID";
    public static final String COUNT_COMMENT_BY_POST_ID = "COUNT_COMMENT_BY_POST_ID";

    /**
     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}

