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
    public static final String LIST_DIET = "LIST_DIET";
    public static final String SAVE_DIET = "SAVE_DIET";
    public static final String UPDATE_DIET = "UPDATE_DIET";
    public static final String GET_DIET_ID = "GET_DIET_ID";

    /**
     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}

