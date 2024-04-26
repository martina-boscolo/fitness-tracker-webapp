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
    public static final String LIST_DIET = "LIST_DIET";
    public static final String SAVE_DIET = "SAVE_DIET";
    public static final String UPDATE_DIET = "UPDATE_DIET";
    public static final String GET_DIET_ID = "GET_DIET_ID";
    public static final String GET_DIET_USER_ID = "GET_DIET_USER_ID";


    //POST ACTIONS
    public static final String CREATE_POST = "CREATE_POST";
    public static final String DELETE_POST = "DELETE_POST";
    public static final String GET_POST_BY_ID = "GET_POST_BY_ID";
    public static final String UPDATE_POST = "UPDATE_POST";
    public static final String LIST_POST = "LIST_POST";
    public static final String LIST_POST_BY_USER_ID = "LIST_POST_BY_USER_ID";
    public static final String LOAD_POST_PHOTO = "LOAD_POST_PHOTO";



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

