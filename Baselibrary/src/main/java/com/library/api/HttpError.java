package com.library.api;

public class HttpError extends Exception implements IHttpError {

    private String errorBody;

    private String errorCode;

    private HttpResponse response;

    public HttpError() {

    }

    public HttpError(String message) {
        super(message);
    }

    public HttpError(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpError(Throwable throwable) {
        super(throwable);
    }

    public HttpError(Throwable throwable, String errorCode) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public HttpError(String message, Throwable throwable) {
        super(message, throwable);
    }

    public HttpError(HttpResponse response) {
        this.response = response;
    }

    public HttpError(HttpResponse response, String message) {
        super(message);
        this.response = response;
    }

    public HttpError(HttpResponse response, Throwable throwable) {
        super(throwable);
        this.response = response;
    }

    public HttpError(HttpResponse response, String message, Throwable throwable) {
        super(message, throwable);
        this.response = response;
    }

    public String getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(String errorBody) {
        this.errorBody = errorBody;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }
}
