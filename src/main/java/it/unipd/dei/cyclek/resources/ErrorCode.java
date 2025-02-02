package it.unipd.dei.cyclek.resources;

import jakarta.servlet.http.HttpServletResponse;

public enum ErrorCode {

    // User Code        -100 -> -199
    REGISTER_USER_BAD_REQUEST("-100", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Missing some user's fields"),
    REGISTER_USER_INTERNAL_SERVER_ERROR("-101", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Error creating user"),
    REGISTER_USER_CONSTRAINT_VIOLATION("-102", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Username already used"),
    REGISTER_USER_DB_ERROR("-103", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected Error while registering user"),
    LOGIN_USER_BAD_REQUEST("-104", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Missing username or password"),
    LOGIN_USER_NOT_FOUND("-105", HttpServletResponse.SC_NOT_FOUND, "Not Found", "User Not Found"),
    LOGIN_USER_DB_ERROR("-106", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected Error while login user"),
    UPDATE_USER_BAD_REQUEST("-107", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Cannot update the user, body must contains all parameters"),
    UPDATE_USER_DB_ERROR("-108", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected Error while updating user"),
    // Stats Code       -200 -> -299
    CREATE_GOAL_INTERNAL_SERVER_ERROR("-200", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while creating a user goal."),
    CREATE_GOAL_DB_CONFLICT("-201", HttpServletResponse.SC_CONFLICT, "Conflict", "Cannot create a user goal, already exist."),
    CREATE_GOAL_NULL_POINTER("-202", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Cannot create a user goal, body must contains all parameters."),
    CREATE_GOAL_JSON_ERROR("-203", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "No User Goal JSON object found in the request."),
    GET_GOAL_INTERNAL_SERVER_ERROR("-204", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving goals."),
    GET_GOAL_NOT_FOUND("-205", HttpServletResponse.SC_NOT_FOUND, "Not Found", "No goal found."),
    GET_USER_GOAL_INTERNAL_SERVER_ERROR("-206", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving a user goal."),
    GET_USER_GOAL_NOT_FOUND("-207", HttpServletResponse.SC_NOT_FOUND, "Not Found", "No goal found for searched user."),
    CREATE_STATS_INTERNAL_SERVER_ERROR("-208", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while creating a user stats."),
    CREATE_STATS_DB_CONFLICT("-209", HttpServletResponse.SC_CONFLICT, "Conflict", "Cannot create a user stats, already exist."),
    CREATE_STATS_NULL_POINTER("-210", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Cannot create a user stats, body must contains all parameters."),
    CREATE_STATS_JSON_ERROR("-211", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "No User Stats JSON object found in the request."),
    GET_STATS_INTERNAL_SERVER_ERROR("-212", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving stats."),
    GET_STATS_NOT_FOUND("-213", HttpServletResponse.SC_NOT_FOUND, "Not Found", "No stat found."),
    GET_USER_STATS_INTERNAL_SERVER_ERROR("-214", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving user stats."),
    GET_USER_STATS_NOT_FOUND("-215", HttpServletResponse.SC_NOT_FOUND, "Not Found", "No stat found for searched user."),
    GET_IMC_INTERNAL_SERVER_ERROR("-216", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving IMC."),
    GET_IMC_NOT_FOUND("-217", HttpServletResponse.SC_NOT_FOUND, "Not Found", "IMC cannot be computed, no stats found for searched user."),
    GET_MEAN_IMC_INTERNAL_SERVER_ERROR("-218", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving mean IMC."),
    GET_MEAN_IMC_NOT_FOUND("-219", HttpServletResponse.SC_NOT_FOUND, "Not Found", "Mean IMC cannot be computed, no stats found."),
    GET_MEALS_FOOD_INTERNAL_SERVER_ERROR("-220", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving meal stats."),
    GET_MEALS_NOT_FOUND("-221", HttpServletResponse.SC_NOT_FOUND, "Not Found", "Cannot compute meal stats, no meal registered for searched user."),

    // Social Code      -300 -> -399
    CREATE_POST_ALREADY_EXISTS("-300", HttpServletResponse.SC_CONFLICT, "Conflict", "Post already exists"),
    CREATE_POST_INTERNAL_SERVER_ERROR("-301", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while creating post"),
    CREATE_POST_JSON_ERROR("-302", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "No Post JSON object found in the request"),
    CREATE_POST_DB_ERROR("-303", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected db error while creating post"),
    DELETE_POST_NOT_FOUND("-304", HttpServletResponse.SC_NOT_FOUND, "Not Found", "Post not found"),
    DELETE_POST_BAD_REQUEST("-305", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Cannot delete the post, wrong format for URI /post/{postId}"),
    DELETE_POST_CONFLICT("-306", HttpServletResponse.SC_CONFLICT, "Conflict", "Cannot delete the post, other resources depend on it"),
    DELETE_POST_DB_ERROR("-307", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected db error while deleting post"),
    LIST_POST_INTERNAL_SERVER_ERROR("-308", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while listing posts"),
    LIST_POST_DB_ERROR("-309", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected db error while listing posts"),
    GET_POST_NOT_FOUND("-310", HttpServletResponse.SC_NOT_FOUND, "Not Found", "Post not found"),
    GET_POST_BAD_REQUEST("-311", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Cannot get the post, wrong format for URI /post/{postId}"),
    GET_POST_DB_ERROR("-312", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected db error while getting post"),
    UPDATE_POST_BAD_REQUEST("-313", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Cannot update the post, URI request and post resource postId differ"),
    UPDATE_POST_NOT_FOUND("-314", HttpServletResponse.SC_NOT_FOUND, "Not Found", "Post not found"),
    UPDATE_POST_BAD_FORMAT("-315", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Cannot update the post, wrong format for URI /post/{postId}"),
    UPDATE_POST_JSON_ERROR("-316", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Cannot update the post, no Post JSON object found in the request"),
    UPDATE_POST_DB_ERROR("-317", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected db error while updating post"),
    COUNT_COMMENT_INTERNAL_SERVER_ERROR("-318", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while counting comments"),
    COUNT_COMMENT_DB_ERROR("-319", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected db error while counting comments"),
    CREATE_COMMENT_INTERNAL_SERVER_ERROR("-320", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while creating comment"),
    CREATE_COMMENT_JSON_ERROR("-321", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "No Comment JSON object found in the request"),
    CREATE_COMMENT_ALREADY_EXISTS("-322", HttpServletResponse.SC_CONFLICT, "Conflict", "Comment already exists"),
    CREATE_COMMENT_DB_ERROR("-323", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while creating comment"),
    DELETE_COMMENT_NOT_FOUND("-324", HttpServletResponse.SC_NOT_FOUND, "Not Found", "Comment not found"),
    DELETE_COMMENT_WRONG_FORMAT("-325", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Cannot delete the comment, wrong format for URI /post/comment/{commentId}"),
    DELETE_COMMENT_CONFLICT("-326", HttpServletResponse.SC_CONFLICT, "Conflict", "Cannot delete the comment, other resources depend on it"),
    DELETE_COMMENT_DB_ERROR("-327", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while deleting comment"),
    LIST_COMMENT_INTERNAL_SERVER_ERROR("-328", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while listing comments"),
    COUNT_LIKE_INTERNAL_SERVER_ERROR("-329", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while counting likes"),
    COUNT_LIKE_DB_ERROR("-330", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected db error while counting likes"),
    CREATE_LIKE_INTERNAL_SERVER_ERROR("-331", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while creating like"),
    CREATE_LIKE_JSON_ERROR("-332", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "No Like JSON object found in the request"),
    CREATE_LIKE_DB_ERROR("-333", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected db error while creating like"),
    DELETE_LIKE_INTERNAL_SERVER_ERROR("-334", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while deleting like"),
    DELETE_LIKE_WRONG_FORMAT("-335", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Cannot delete the like, wrong format for URI /post/like/{likeId}"),
    DELETE_LIKE_CONFLICT("-336", HttpServletResponse.SC_CONFLICT, "Conflict", "Cannot delete the like, other resources depend on it"),
    DELETE_LIKE_DB_ERROR("-337", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected db error while deleting like"),
    LIST_LIKE_INTERNAL_SERVER_ERROR("-338", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while listing likes"),
    LIST_LIKE_DB_ERROR("-339", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected db error while listing likes"),

    CREATE_LIKE_ALREADY_EXISTS("-340", HttpServletResponse.SC_CONFLICT, "Conflict", "Like already exists"),

    // Diet Code        -400 -> -499
    ID_DIET_NOT_FOUND("-400", HttpServletResponse.SC_NOT_FOUND, "Not Found", "Id not found for searched diet"),
    GET_DIET_INTERNAL_SERVER_ERROR("-401", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected Error while retrieving diet"),
    ID_USER_DIET_NOT_FOUND("-402", HttpServletResponse.SC_NOT_FOUND, "Not Found", "idUser not found for searched diet"),
    GET_ID_USER_DIET_INTERNAL_SERVER_ERROR("-403", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected Error while retrieving diet"),
    LIST_ALL_DIET_NOT_FOUND("-404", HttpServletResponse.SC_NOT_FOUND, "Not Found", "There are no diets to list"),
    LIST_ALL_DIET_INTERNAL_SERVER_ERROR("-405", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected Error while listing diets"),
    SAVE_DIET_INTERNAL_SERVER_ERROR("-406", HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal server error", "Unexpected Error while saving diet"),
    SAVE_DIET_BAD_REQUEST("-407", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "No idUser JSON object found in the request"),
    UPDATE_DIET_CONSTRAINT_VIOLATION("-408", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Cannot modify a diet, 24 hours has passed"),
    UPDATE_DIET_INTERNAL_SERVER_ERROR("-409", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected Error while updating a diet"),

    // Food and Meal Code        -500 -> -599
    GET_FOOD_NOT_FOUND("-500", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "No food found for the id"),
    GET_FOOD_INTERNAL_SERVER_ERROR("-501",  HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected Error while retrieving food for this id"),
    FOOD_NOT_FOUND("-502", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Found", "Food not found in database"),
    FOOD_INTERNAL_SERVER_ERROR("-503",  HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected Error while retrieving food"),
    REGISTER_FOOD_BAD_REQUEST("-504", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "No JSON object found in the request"),
    REGISTER_FOOD_INTERNAL_SERVER_ERROR("-505", HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal server error", "Unexpected Error while saving food"),

    ID_USER_MEAL_NOT_FOUND("-506", HttpServletResponse.SC_NOT_FOUND, "Not Found", "idUser not found for searched meals"),
    GET_ID_USER_FOOD_INTERNAL_SERVER_ERROR("-507", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected Error while retrieving meals"),
    LIST_ALL_MEALS_NOT_FOUND("-508", HttpServletResponse.SC_NOT_FOUND, "Not Found", "There are no meals to list"),
    LIST_ALL_MEAL_INTERNAL_SERVER_ERROR("-509", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected Error while listing meals"),
    SAVE_MEAL_BAD_REQUEST("-510", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "No JSON object found in the request"),
    SAVE_MEAL_INTERNAL_SERVER_ERROR("-511", HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal server error", "Unexpected Error while saving meal"),


    // Exercise Code    -600 -> -699
    ID_EXERCISE_NOT_FOUND("-600", HttpServletResponse.SC_NOT_FOUND, "Not Found", "id exercise not found "),
    GET_EXERCISE_INTERNAL_SERVER_ERROR("-601",HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal Server Error", "Unexpected Error while retrieving exercise"),
    LIST_ALL_EXERCISES_NOT_FOUND("-602",HttpServletResponse.SC_NOT_FOUND, "Not Found", "There are no exercises to list"),
    LIST_ALL_EXERCISES_INTERNAL_SERVER_ERROR("-603",HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal Server Error", "Unexpected Error while listing exercises"),
    ADD_EXERCISE_PLAN_BAD_REQUEST("-604", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "No JSON object found in the request"),
    ADD_EXERCISE_PLAN_INTERNAL_SERVER_ERROR("-605",HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal Server Error", "Unexpected Error while adding exercise plan"),
    GET_EXERCISE_PLAN_INTERNAL_ERROR("-606",HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal Server Error", "Unexpected Error while getting exercise plan"),
    ID_EXERCISE_PLAN_NOT_FOUND("-607", HttpServletResponse.SC_NOT_FOUND, "Not Found", "id exercise plan not found"),
    LIST_ALL_EXERCISE_PLANS_NOT_FOUND("-608",HttpServletResponse.SC_NOT_FOUND,"Not Found", "There are no exercise plans to list"),
    LIST_ALL_EXERCISE_PLANS_INTERNAL_SERVER_ERROR("-609",HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal Server Error", "Unexpected Error while listing exercise plans"),
    UPDATE_EXERCISE_PLAN_INTERNAL_ERROR("-610",HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal Server Error", "Unexpected Error while updating exercise plan"),
    ID_USER_EXERCISE_PLAN_NOT_FOUND("-611", HttpServletResponse.SC_NOT_FOUND, "Not Found", "id user not found for searched exercise plan"),
    GET_ID_USER_EXERCISE_PLAN_INTERNAL_SERVER_ERROR("-612",HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal Server Error", "Unexpected Error while getting user's exercise plans"),
    DELETE_EXERCISE_PLAN_INTERNAL_SERVER_ERROR("-613",HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal Server Error","Unexpected Error while deleting exercise plan"),
    DELETE_EXERCISE_PLAN_BAD_REQUEST("-614",HttpServletResponse.SC_BAD_REQUEST,"Internal Server Error","we can't find this exercise plan to delete"),
    ADD_EXERCISE_PLAN_INSERT_FAIL("-615",HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal Server Error","failed to insert plan into db"),




    // General Code     -900 -> -999
    INTERNAL_ERROR("-900", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error","Unexpected Error"),
    REST_NOT_FOUND("-901", HttpServletResponse.SC_NOT_FOUND, "Unknown resource requested.", "Unknown resource requested."),
    UNSUPPORTED_OPERATION("-902", HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unsupported operation", "Unsupported operation for requested URI"),
    MEDIA_TYPE_NOT_SPECIFIED("-903", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Output media type not specified. Accept request header missing."),
    UNSUPPORTED_MEDIA_TYPE("-904", HttpServletResponse.SC_NOT_ACCEPTABLE, "Not Acceptable", "Unsupported output media type. Resources are represented only in application/json."),
    CONTENT_TYPE_MISSING("-905", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Input media type not specified. Content-Type request header missing."),
    UNABLE_TO_SERVE_REQUEST("-906", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unable to serve the REST request. "),
    UNAUTHORIZED("-907", HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Unauthorized"),
    ;

    private final String errorCode;
    private final int httpCode;
    private final String msg;
    private final String msgDet;

    ErrorCode(String errorCode, int httpCode, String msg, String msgDet) {
        this.errorCode = errorCode;
        this.httpCode = httpCode;
        this.msg = msg;
        this.msgDet = msgDet;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public Message getMessage() {
        return new Message(this.msg,this.errorCode,this.msgDet);
    }
    public Message getMessageWithParam(String param) {
        return new Message(this.msg,this.errorCode,this.msgDet.concat(param));
    }
}
