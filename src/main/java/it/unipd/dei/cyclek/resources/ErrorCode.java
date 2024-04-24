package it.unipd.dei.cyclek.resources;

import jakarta.servlet.http.HttpServletResponse;

public enum ErrorCode {

    // User Code        -100 -> -199
    REGISTER_USER_BAD_REQUEST("-100", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Missing some user's fields"),
    REGISTER_USER_INTERNAL_SERVER_ERROR("-101", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Error creating user"),
    REGISTER_USER_CONSTRAINT_VIOLATION("-102", HttpServletResponse.SC_BAD_REQUEST, "Bad Request", "Username already used"),
    REGISTER_USER_DB_ERROR("-103", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", "Database Error"),
    // Stats Code       -200 -> -299


    // Social Code      -300 -> -399


    // Diet Code        -400 -> -499


    // Meal Code        -500 -> -599


    // Exercise Code    -600 -> -699


    // General Code     -900 -> -999
    INTERNAL_ERROR("-999",HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error","Internal Server Error");

    private final int httpCode;

    private final Message message;

    ErrorCode(String ErrorCode, int HttpCode, String msg, String msgDet) {
        this.httpCode = HttpCode;
        this.message = new Message(msg, ErrorCode, msgDet);
    }

    public int getHttpCode() {
        return httpCode;
    }

    public Message getMessage() {
        return message;
    }
}