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


    // Diet Code        -400 -> -499
    ID_DIET_NOT_FOUND("-400", HttpServletResponse.SC_NOT_FOUND, "Not Found", "Id not found for searched diet"),
    GET_DIET_INTERNAL_SERVER_ERROR("-401", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while retrieving diet"),
    ID_USER_DIET_NOT_FOUND("-402", HttpServletResponse.SC_NOT_FOUND, "Not Found", "idUser not found for searched diet"),
    GET_ID_USER_DIET_INTERNAL_SERVER_ERROR("-403", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while retrieving diet"),
    LIST_ALL_DIET_NOT_FOUND("-404", HttpServletResponse.SC_NOT_FOUND, "Not Found", "There are no diets to list"),
    LIST_ALL_DIET_INTERNAL_SERVER_ERROR("-405", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while listing diets"),
    SAVE_DIET_INTERNAL_SERVER_ERROR("-406", HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal server error", "Error while saving diet"),
    SAVE_DIET_BAD_REQUEST("-407", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "No idUser JSON object found in the request"),
    UPDATE_DIET_CONSTRAINT_VIOLATION("-408", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Cannot modify a diet, 24 hours has passed"),
    UPDATE_DIET_INTERNAL_SERVER_ERROR("-409", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error while updating a diet"),

    // Meal Code        -500 -> -599


    // Exercise Code    -600 -> -699



    // General Code     -900 -> -999
    INTERNAL_ERROR("-900", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error","Unexpected Error"),
    REST_NOT_FOUND("-901", HttpServletResponse.SC_NOT_FOUND, "Unknown resource requested.", "Unknown resource requested."),
    UNSUPPORTED_OPERATION("-902", HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unsupported operation", "Unsupported operation for requested URI"),
    MEDIA_TYPE_NOT_SPECIFIED("-903", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Output media type not specified. Accept request header missing."),
    UNSUPPORTED_MEDIA_TYPE("-904", HttpServletResponse.SC_NOT_ACCEPTABLE, "Not Acceptable", "Unsupported output media type. Resources are represented only in application/json."),
    CONTENT_TYPE_MISSING("-905", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Input media type not specified. Content-Type request header missing."),
    UNABLE_TO_SERVE_REQUEST("-906", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unable to serve the REST request. "),
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
