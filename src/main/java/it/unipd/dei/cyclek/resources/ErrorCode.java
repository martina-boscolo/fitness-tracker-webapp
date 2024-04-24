package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import jakarta.servlet.http.HttpServletResponse;

public enum ErrorCode {

    // User Code        -100 -> -199


    // Stats Code       -200 -> -299
    CREATE_GOAL_INTERNAL_SERVER_ERROR("-200", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while creating a user goal."),
    CREATE_GOAL_DB_CONFLICT("-201", HttpServletResponse.SC_CONFLICT, "Conflict", "Cannot create a user goal, already exist."),
    CREATE_GOAL_NULL_POINTER("-202", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Some parameters are null, body must contains all parameters."),
    CREATE_GOAL_JSON_ERROR("-203", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "No User Goal JSON object found in the request."),
    GET_GOAL_INTERNAL_SERVER_ERROR("-204", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving a goals."),
    GET_USER_GOAL_INTERNAL_SERVER_ERROR("-205", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving a user goal."),
    CREATE_STATS_INTERNAL_SERVER_ERROR("-206", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while creating a user stats."),
    CREATE_STATS_DB_CONFLICT("-207", HttpServletResponse.SC_CONFLICT, "Conflict", "Cannot create a user stats, already exist."),
    CREATE_STATS_NULL_POINTER("-208", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Some parameters are null, body must contains all parameters."),
    CREATE_STATS_JSON_ERROR("-209", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "No User Stats JSON object found in the request."),
    GET_STATS_INTERNAL_SERVER_ERROR("-210", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving stats."),
    GET_USER_STATS_INTERNAL_SERVER_ERROR("-211", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving user stats."),
    GET_IMC_INTERNAL_SERVER_ERROR("-212", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving imc."),
    GET_MEAN_IMC_INTERNAL_SERVER_ERROR("-213", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving mean imc."),
    GET_MEALS_FOOD_INTERNAL_SERVER_ERROR("-214", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error", "Unexpected Error while retrieving meal stats."),
    // Social Code      -300 -> -399


    // Diet Code        -400 -> -499


    // Meal Code        -500 -> -599


    // Exercise Code    -600 -> -699



    // General Code     -900 -> -999
    INTERNAL_ERROR("-900", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error","Unexpected Error"),
    REST_NOT_FOUND("-901", HttpServletResponse.SC_NOT_FOUND, "Unknown resource requested.", "Unknown resource requested."),
    UNSUPPORTED_OPERATION("-902", HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unsupported operation", "Unsupported operation for requested uri"),
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
