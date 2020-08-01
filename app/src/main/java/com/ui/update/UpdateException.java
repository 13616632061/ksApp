package com.ui.update;

public class UpdateException extends Exception {
    /**
     * Unknown Error
     */
    public static final int UNKNOWN = 0;

    /**
     * The context is NULL.
     */
    public static final int CONTEXT_NOT_VALID = 1;

    /**
     * The UpdateOptions is NULL.
     */
    public static final int UPDATE_OPTIONS_NOT_VALID = 2;

    /**
     * Parse error
     */
    public static final int PARSE_ERROR = 3;

    private int code = UNKNOWN;

    public UpdateException() {
        super(generateMessageFromCode(UNKNOWN));
        this.code = UNKNOWN;
    }

    /**
     * Generage the error message with the code
     *
     * @param code code
     */
    private static String generateMessageFromCode(int code) {
        String message;

        switch (code) {
            case CONTEXT_NOT_VALID:
                message = "The Context is null or not valid!";
                break;
            case UPDATE_OPTIONS_NOT_VALID:
                message = "The Update Options is null or not valid!";
                break;
            case PARSE_ERROR:
                message = "Failed to parse the UpdateInfo from the xml or json string.";
                break;
            case UNKNOWN:
            default:
                message = "Unknown Error!";
                break;
        }
        return message;
    }

    public UpdateException(int code) {
        super(generateMessageFromCode(code));
        this.code = code;
    }

    public UpdateException(String message) {
        super(message);
        this.code = UNKNOWN;
    }

    public UpdateException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}