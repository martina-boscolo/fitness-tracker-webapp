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
    public static final String LIST_FOOD ="LIST_FOOD";
    public static final String REGISTER_FOOD = "REGISTER_FOOD";
    public static final String LIST_MEAL = "LIST_MEAL";
    public static final String LIST_CALORIES_MEAL = "LIST_CALORIES_MEAL";
    public static final String FOOD_BY_ID = "FOOD_BY_ID";
    public static final String SAVE_MEAL = "SAVE_MEAL";


    /**
     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}

