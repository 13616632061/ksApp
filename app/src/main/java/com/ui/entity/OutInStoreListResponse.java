package com.ui.entity;

import java.util.List;

/**
 * 出入库查询列表
 * Created by lyf on 2020/9/12.
 */

public class OutInStoreListResponse {


    /**
     * response : {"status":"200","message":"ok","data":{"list":[{"id":"52","seller_id":"7080","oparator":"1007080","remark":",","order_id":"2009121549516041","nums":"1","money":"0.000","createtime":"1599896995","type":"0"}],"total":[{"num":"1"}]}}
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
         * data : {"list":[{"id":"52","seller_id":"7080","oparator":"1007080","remark":",","order_id":"2009121549516041","nums":"1","money":"0.000","createtime":"1599896995","type":"0"}],"total":[{"num":"1"}]}
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
            private List<ListBean> list;
            private List<TotalBean> total;

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public List<TotalBean> getTotal() {
                return total;
            }

            public void setTotal(List<TotalBean> total) {
                this.total = total;
            }

            public static class ListBean {
                /**
                 * id : 52
                 * seller_id : 7080
                 * oparator : 1007080
                 * remark : ,
                 * order_id : 2009121549516041
                 * nums : 1
                 * money : 0.000
                 * createtime : 1599896995
                 * type : 0
                 */

                private String id;
                private String seller_id;
                private String oparator;
                private String remark;
                private String order_id;
                private String nums;
                private String money;
                private String createtime;
                private String type;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getSeller_id() {
                    return seller_id;
                }

                public void setSeller_id(String seller_id) {
                    this.seller_id = seller_id;
                }

                public String getOparator() {
                    return oparator;
                }

                public void setOparator(String oparator) {
                    this.oparator = oparator;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public String getOrder_id() {
                    return order_id;
                }

                public void setOrder_id(String order_id) {
                    this.order_id = order_id;
                }

                public String getNums() {
                    return nums;
                }

                public void setNums(String nums) {
                    this.nums = nums;
                }

                public String getMoney() {
                    return money;
                }

                public void setMoney(String money) {
                    this.money = money;
                }

                public String getCreatetime() {
                    return createtime;
                }

                public void setCreatetime(String createtime) {
                    this.createtime = createtime;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class TotalBean {
                /**
                 * num : 1
                 */

                private String num;

                public String getNum() {
                    return num;
                }

                public void setNum(String num) {
                    this.num = num;
                }
            }
        }
    }
}
