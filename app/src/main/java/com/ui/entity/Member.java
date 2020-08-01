package com.ui.entity;

import java.util.List;

/**
 * Created by admin on 2020/2/10.
 */

public class Member {


    /**
     * status : 200
     * message : ok
     * data : {"info":[{"is_require_pass":"no","member_id":"7007","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"282.010","member_name":"000401","mobile":"16658156125","discount_rate":"1.6","score":"40","addtime":"1574336602","birthday":"1574265600","remark":"remark","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"yes","member_id":"201","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"0.000","member_name":"y","mobile":"18757653662","discount_rate":"1","score":"0","addtime":"1572437654","birthday":"1572364800","remark":null,"member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"no","member_id":"197","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"0.000","member_name":"yggh","mobile":"18720961399","discount_rate":"1","score":"0","addtime":"1564038252","birthday":"0","remark":"null","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"yes","member_id":"196","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"0.000","member_name":"435","mobile":"18720961378","discount_rate":"1","score":"0","addtime":"1564036771","birthday":"0","remark":"null","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"no","member_id":"195","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"606.760","member_name":"滴滴","mobile":"15605896960","discount_rate":"0.88","score":"0","addtime":"1563859927","birthday":"657302400","remark":"","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"yes","member_id":"187","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"320.000","member_name":"测试","mobile":"13437899863","discount_rate":"1","score":"0","addtime":"1562074862","birthday":"1561996800","remark":"已更新openid","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"no","member_id":"173","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"0.000","member_name":"0658680139","mobile":"13456438888","discount_rate":"0.8","score":"0","addtime":"1556008871","birthday":"0","remark":"","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"no","member_id":"172","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"163.640","member_name":"4566","mobile":"13234545777","discount_rate":"0.8","score":"0","addtime":"1555998460","birthday":null,"remark":"","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"no","member_id":"171","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"49.160","member_name":"00076555","mobile":"13455456666","discount_rate":"0.88","score":"0","addtime":"1555998070","birthday":null,"remark":"","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"no","member_id":"158","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"0.000","member_name":"vbf","mobile":"18720961345","discount_rate":"1","score":"0","addtime":"1553585591","birthday":null,"remark":"null5466677","member_lv_custom_name":"铂金3","member_lv_custom_remark":""}],"nums":40}
     */

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        private String status;
        private String message;
        /**
         * info : [{"is_require_pass":"no","member_id":"7007","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"282.010","member_name":"000401","mobile":"16658156125","discount_rate":"1.6","score":"40","addtime":"1574336602","birthday":"1574265600","remark":"remark","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"yes","member_id":"201","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"0.000","member_name":"y","mobile":"18757653662","discount_rate":"1","score":"0","addtime":"1572437654","birthday":"1572364800","remark":null,"member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"no","member_id":"197","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"0.000","member_name":"yggh","mobile":"18720961399","discount_rate":"1","score":"0","addtime":"1564038252","birthday":"0","remark":"null","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"yes","member_id":"196","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"0.000","member_name":"435","mobile":"18720961378","discount_rate":"1","score":"0","addtime":"1564036771","birthday":"0","remark":"null","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"no","member_id":"195","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"606.760","member_name":"滴滴","mobile":"15605896960","discount_rate":"0.88","score":"0","addtime":"1563859927","birthday":"657302400","remark":"","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"yes","member_id":"187","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"320.000","member_name":"测试","mobile":"13437899863","discount_rate":"1","score":"0","addtime":"1562074862","birthday":"1561996800","remark":"已更新openid","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"no","member_id":"173","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"0.000","member_name":"0658680139","mobile":"13456438888","discount_rate":"0.8","score":"0","addtime":"1556008871","birthday":"0","remark":"","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"no","member_id":"172","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"163.640","member_name":"4566","mobile":"13234545777","discount_rate":"0.8","score":"0","addtime":"1555998460","birthday":null,"remark":"","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"no","member_id":"171","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"49.160","member_name":"00076555","mobile":"13455456666","discount_rate":"0.88","score":"0","addtime":"1555998070","birthday":null,"remark":"","member_lv_custom_name":"铂金3","member_lv_custom_remark":""},{"is_require_pass":"no","member_id":"158","member_lv_custom_id":"3","member_lv_custom_key":"1","surplus":"0.000","member_name":"vbf","mobile":"18720961345","discount_rate":"1","score":"0","addtime":"1553585591","birthday":null,"remark":"null5466677","member_lv_custom_name":"铂金3","member_lv_custom_remark":""}]
         * nums : 40
         */

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
            private int nums;
            /**
             * is_require_pass : no
             * member_id : 7007
             * member_lv_custom_id : 3
             * member_lv_custom_key : 1
             * surplus : 282.010
             * member_name : 000401
             * mobile : 16658156125
             * discount_rate : 1.6
             * score : 40
             * addtime : 1574336602
             * birthday : 1574265600
             * remark : remark
             * member_lv_custom_name : 铂金3
             * member_lv_custom_remark :
             */

            private List<InfoBean> info;

            public int getNums() {
                return nums;
            }

            public void setNums(int nums) {
                this.nums = nums;
            }

            public List<InfoBean> getInfo() {
                return info;
            }

            public void setInfo(List<InfoBean> info) {
                this.info = info;
            }

            public static class InfoBean {
                private String is_require_pass;
                private String member_id;
                private String member_lv_custom_id;
                private String member_lv_custom_key;
                private String surplus;
                private String member_name;
                private String mobile;
                private String discount_rate;
                private String score;
                private String addtime;
                private String birthday;
                private String remark;
                private String member_lv_custom_name;
                private String member_lv_custom_remark;

                public String getIs_require_pass() {
                    return is_require_pass;
                }

                public void setIs_require_pass(String is_require_pass) {
                    this.is_require_pass = is_require_pass;
                }

                public String getMember_id() {
                    return member_id;
                }

                public void setMember_id(String member_id) {
                    this.member_id = member_id;
                }

                public String getMember_lv_custom_id() {
                    return member_lv_custom_id;
                }

                public void setMember_lv_custom_id(String member_lv_custom_id) {
                    this.member_lv_custom_id = member_lv_custom_id;
                }

                public String getMember_lv_custom_key() {
                    return member_lv_custom_key;
                }

                public void setMember_lv_custom_key(String member_lv_custom_key) {
                    this.member_lv_custom_key = member_lv_custom_key;
                }

                public String getSurplus() {
                    return surplus;
                }

                public void setSurplus(String surplus) {
                    this.surplus = surplus;
                }

                public String getMember_name() {
                    return member_name;
                }

                public void setMember_name(String member_name) {
                    this.member_name = member_name;
                }

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                public String getDiscount_rate() {
                    return discount_rate;
                }

                public void setDiscount_rate(String discount_rate) {
                    this.discount_rate = discount_rate;
                }

                public String getScore() {
                    return score;
                }

                public void setScore(String score) {
                    this.score = score;
                }

                public String getAddtime() {
                    return addtime;
                }

                public void setAddtime(String addtime) {
                    this.addtime = addtime;
                }

                public String getBirthday() {
                    return birthday;
                }

                public void setBirthday(String birthday) {
                    this.birthday = birthday;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public String getMember_lv_custom_name() {
                    return member_lv_custom_name;
                }

                public void setMember_lv_custom_name(String member_lv_custom_name) {
                    this.member_lv_custom_name = member_lv_custom_name;
                }

                public String getMember_lv_custom_remark() {
                    return member_lv_custom_remark;
                }

                public void setMember_lv_custom_remark(String member_lv_custom_remark) {
                    this.member_lv_custom_remark = member_lv_custom_remark;
                }
            }
        }
    }
}
