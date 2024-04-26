package it.unipd.dei.cyclek.resources;

/**
 * Contains constants for the actions performed by the application.
 *
 */
public final class Actions {

    /**
     * The search of users by their id
     */
    public static final String ADD_USER = "ADD_USER";
    public static final String LIST_USER = "LIST_USER";
    public static final String LIST_BODY_STATS = "LIST_BODY_STATS";
    public static final String LIST_BODY_STATS_BY_IDUSER = "LIST_BODY_STATS_BY_IDUSER";
    public static final String GET_MEAN_IMC = "GET_MEAN_IMC";
    public static final String GET_IMC_BY_IDUSER = "GET_IMC_BY_IDUSER";
    public static final String GET_MEALS_BY_IDUSER = "GET_MEALS_BY_IDUSER";
    public static final String CREATE_BODY_STATS = "CREATE_BODY_STATS";
    public static final String LIST_BODY_OBJ = "LIST_BODY_OBJ";
    public static final String LIST_BODY_OBJ_BY_IDUSER = "LIST_BODY_OBJ_BY_IDUSER";
    public static final String CREATE_BODY_OBJ = "CREATE_BODY_OBJ";

    /**
     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}

