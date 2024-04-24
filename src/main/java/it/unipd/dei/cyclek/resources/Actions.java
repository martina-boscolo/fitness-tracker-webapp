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
    public static final String LIST_Exercise = "LIST_Exercise";
    public static final String GET_Exercise_Id = "GET_Exercise_Id";
    public static final String Add_User_Exercise = "Add_User_Exercise";
    public static final String Delete_User_Exercise = "Delete_User_Exercise";
    public static final String LIST_ExercisePlan = "LIST_ExercisePlan";
    public static final String ADD_Exercise_Plan = "ADD_Exercise_Plan";
    public static final String DELETE_Exercise_Plan = "DELETE_Exercise_Plan";
    public static final String Get_ExercisePlan_Id = "GET_ExercisePlan_Id";
    public static final String Update_Exercise_Plan = "Update_Exercise_Plan";
    public static final String Get_ExercisePlan_UserId = "GET_Exercise_Plan_Id";
    /**
     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}

