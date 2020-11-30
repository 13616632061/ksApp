package com.library.api;

public interface IHttpErrorHandle {
    HttpError handleException(Throwable e);
}
