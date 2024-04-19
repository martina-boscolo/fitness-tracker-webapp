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

    public static final String CREATE_POST = "CREATE_POST";

    public static final String DELETE_POST = "DELETE_POST";

    public static final String GET_POST_BY_ID = "GET_POST_BY_ID";

    public static final String UPDATE_POST = "UPDATE_POST";

    public static final String LIST_POST = "LIST_POST";

    public static final String CREATE_LIKE = "LIST_POST";

    public static final String DELETE_LIKE = "LIST_POST";

    public static final String LIST_LIKE_BY_POST_ID = "LIST_POST";

    public static final String CREATE_COMMENT = "LIST_POST";

    public static final String DELETE_COMMENT = "LIST_POST";

    public static final String LIST_COMMENT_BY_ID = "LIST_POST";

    /**
     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}

