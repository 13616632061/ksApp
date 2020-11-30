package com.library.api;

public interface IHttpCallback<T> {

    void onStart();

    void onSuccess(T t);

    void onFailure(String code, String message);

    void onThrowable(HttpError httpError);

}