package com.bean;

import java.util.List;

/**
 * Created by lyf on 2020/8/5.
 */

public class SalesStatisticsRespone  {


    /**
     * response : {"status":"200","message":"ok","data":{"status":"success","msg":"2020年08月订单","orders_info":[{"total_day":"4","total_money_day":"2457.000","date":"2020-08-02"}],"total":"4","datetime":"2020-08","total_money":"2457.000","page":"1","totalpage":"05"}}
     */

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * status : 200
         * message : ok
         * data : {"status":"success","msg":"2020年08月订单","orders_info":[{"total_day":"4","total_money_day":"2457.000","date":"2020-08-02"}],"total":"4","datetime":"2020-08","total_money":"2457.000","page":"1","totalpage":"05"}
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

        public static class DataBean {
            /**
             * status : success
             * msg : 2020年08月订单
             * orders_info : [{"total_day":"4","total_money_day":"2457.000","date":"2020-08-02"}]
             * total : 4
             * datetime : 2020-08
             * total_money : 2457.000
             * page : 1
             * totalpage : 05
             */

            private String status;
            private String msg;
            private String total;
            private String datetime;
            private String total_money;
            private String page;
            private String totalpage;
            private List<OrdersInfoBean> orders_info;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public String getDatetime() {
                return datetime;
            }

            public void setDatetime(String datetime) {
                this.datetime = datetime;
            }

            public String getTotal_money() {
                return total_money;
            }

            public void setTotal_money(String total_money) {
                this.total_money = total_money;
            }

            public String getPage() {
                return page;
            }

            public void setPage(String page) {
                this.page = page;
            }

            public String getTotalpage() {
                return totalpage;
            }

            public void setTotalpage(String totalpage) {
                this.totalpage = totalpage;
            }

            public List<OrdersInfoBean> getOrders_info() {
                return orders_info;
            }

            public void setOrders_info(List<OrdersInfoBean> orders_info) {
                this.orders_info = orders_info;
            }

            public static class OrdersInfoBean {
                /**
                 * total_day : 4
                 * total_money_day : 2457.000
                 * date : 2020-08-02
                 */

                private String total_day;
                private String total_money_day;
                private String date;

                public String getTotal_day() {
                    return total_day;
                }

                public void setTotal_day(String total_day) {
                    this.total_day = total_day;
                }

                public String getTotal_money_day() {
                    return total_money_day;
                }

                public void setTotal_money_day(String total_money_day) {
                    this.total_money_day = total_money_day;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }
            }
        }
    }
}
