package com.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lyf on 2020/7/11.
 */

public class InventoryRecordRespone{


    /**
     * response : {"status":"200","message":"ok","data":{"list":[{"batch":"3","addtime":"1594304805"},{"batch":"2","addtime":"1594304725"},{"batch":"1","addtime":"1593940183"}],"total":"5"}}
     */

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "InventoryRecordRespone{" +
                "response=" + response +
                '}';
    }

    public static class ResponseBean {
        /**
         * status : 200
         * message : ok
         * data : {"list":[{"batch":"3","addtime":"1594304805"},{"batch":"2","addtime":"1594304725"},{"batch":"1","addtime":"1593940183"}],"total":"5"}
         */

        private String status;
        private String message;
        private DataBean data;

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

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "ResponseBean{" +
                    "status='" + status + '\'' +
                    ", message='" + message + '\'' +
                    ", data=" + data +
                    '}';
        }

        public static class DataBean {
            /**
             * list : [{"batch":"3","addtime":"1594304805"},{"batch":"2","addtime":"1594304725"},{"batch":"1","addtime":"1593940183"}]
             * total : 5
             */

            private String total;
            private List<ListBean> list;

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            @Override
            public String toString() {
                return "DataBean{" +
                        "total='" + total + '\'' +
                        ", list=" + list +
                        '}';
            }

            public static class ListBean {
                /**
                 * batch : 3
                 * addtime : 1594304805
                 */

                private String batch;
                private String addtime;

                public String getBatch() {
                    return batch;
                }

                public void setBatch(String batch) {
                    this.batch = batch;
                }

                public String getAddtime() {
                    return addtime;
                }

                public void setAddtime(String addtime) {
                    this.addtime = addtime;
                }

                @Override
                public String toString() {
                    return "ListBean{" +
                            "batch='" + batch + '\'' +
                            ", addtime='" + addtime + '\'' +
                            '}';
                }
            }
        }
    }
}
