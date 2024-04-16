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
    public static final String LIST_BODY_STATS = "LIST_BODY_STATS";
    public static final String LIST_BODY_STATS_BY_IDUSER = "LIST_BODY_STATS_BY_IDUSER";
    public static final String CREATE_BODY_STATS = "CREATE_BODY_STATS";

    /**
     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}

