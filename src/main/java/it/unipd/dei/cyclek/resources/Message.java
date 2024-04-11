package it.unipd.dei.cyclek.resources;

public class Message {

    private final String message;
    private final String errorCode;
    private final String errorDetails;
    private final boolean isError;

    public Message(String message, String errorCode, String errorDetails) {
        this.message = message;
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
        this.isError = true;
    }

    public Message(String message) {
        this.message = message;
        this.errorCode = null;
        this.errorDetails = null;
        this.isError = false;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public boolean isError() {
        return isError;
    }
}
