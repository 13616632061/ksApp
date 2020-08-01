package com.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by lyf on 2020/7/28.
 */

public class ReportLostListRespone {


    /**
     * response : {"status":"200","message":"ok","data":[{"bncode":"6925526600197","name":"百年糊涂酒52°450ml","cost":"66.500","nums":"1","addtime":"1595518557","desc":"蘑菇","login_name":null}]}
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
         * data : [{"bncode":"6925526600197","name":"百年糊涂酒52°450ml","cost":"66.500","nums":"1","addtime":"1595518557","desc":"蘑菇","login_name":null}]
         */

        private String status;
        private String message;
        private List<DataBean> data;

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

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Parcelable{
            /**
             * bncode : 6925526600197
             * name : 百年糊涂酒52°450ml
             * cost : 66.500
             * nums : 1
             * addtime : 1595518557
             * desc : 蘑菇
             * login_name : null
             */

            private String bncode;
            private String name;
            private String cost;
            private String nums;
            private String addtime;
            private String desc;
            private Object login_name;

            protected DataBean(Parcel in) {
                bncode = in.readString();
                name = in.readString();
                cost = in.readString();
                nums = in.readString();
                addtime = in.readString();
                desc = in.readString();
            }

            public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
                @Override
                public DataBean createFromParcel(Parcel in) {
                    return new DataBean(in);
                }

                @Override
                public DataBean[] newArray(int size) {
                    return new DataBean[size];
                }
            };

            public String getBncode() {
                return bncode;
            }

            public void setBncode(String bncode) {
                this.bncode = bncode;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCost() {
                return cost;
            }

            public void setCost(String cost) {
                this.cost = cost;
            }

            public String getNums() {
                return nums;
            }

            public void setNums(String nums) {
                this.nums = nums;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public Object getLogin_name() {
                return login_name;
            }

            public void setLogin_name(Object login_name) {
                this.login_name = login_name;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(bncode);
                dest.writeString(name);
                dest.writeString(cost);
                dest.writeString(nums);
                dest.writeString(addtime);
                dest.writeString(desc);
            }
        }
    }
}
