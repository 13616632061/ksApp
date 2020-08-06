package com.bean;

/**
 * Created by lyf on 2020/7/8.
 */

public class ResultResponse<T> {


    /**
     * response : {"status":"200","message":"ok","data":"成功"}
     */

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public class ResponseBean{
        /**
         * status : 200
         * message : ok
         * data : 成功
         */

        private String status;
        private String message;
        private T data;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}