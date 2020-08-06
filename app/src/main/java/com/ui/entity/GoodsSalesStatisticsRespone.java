package com.ui.entity;

import android.support.annotation.NonNull;

import com.library.utils.BigDecimalArith;

import java.util.List;

/**
 * Created by lyf on 2020/8/2.
 */

public class GoodsSalesStatisticsRespone {


    /**
     * response : {"status":"200","message":"ok","data":[{"price":"200","nums":"1","sell_price":"200.000","bncode":"986532147","store":"1","goods_id":"859711","goods_name":"初级画画课程","cost":"200.000","total":"200","img_src":"","menu":"默认分类","tag_id":"2198"}]}
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
         * data : [{"price":"200","nums":"1","sell_price":"200.000","bncode":"986532147","store":"1","goods_id":"859711","goods_name":"初级画画课程","cost":"200.000","total":"200","img_src":"","menu":"默认分类","tag_id":"2198"}]
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

        public static class DataBean {
            /**
             * price : 200
             * nums : 1
             * sell_price : 200.000
             * bncode : 986532147
             * store : 1
             * goods_id : 859711
             * goods_name : 初级画画课程
             * cost : 200.000
             * total : 200
             * img_src :
             * menu : 默认分类
             * tag_id : 2198
             */

            private String price;
            private String nums;
            private String sell_price;
            private String bncode;
            private String store;
            private String goods_id;
            private String goods_name;
            private String cost;
            private String total;
            private String img_src;
            private String menu;
            private String tag_id;

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getNums() {
                return nums;
            }

            public void setNums(String nums) {
                this.nums = nums;
            }

            public String getSell_price() {
                return sell_price;
            }

            public void setSell_price(String sell_price) {
                this.sell_price = sell_price;
            }

            public String getBncode() {
                return bncode;
            }

            public void setBncode(String bncode) {
                this.bncode = bncode;
            }

            public String getStore() {
                return store;
            }

            public void setStore(String store) {
                this.store = store;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getCost() {
                return cost;
            }

            public void setCost(String cost) {
                this.cost = cost;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public String getImg_src() {
                return img_src;
            }

            public void setImg_src(String img_src) {
                this.img_src = img_src;
            }

            public String getMenu() {
                return menu;
            }

            public void setMenu(String menu) {
                this.menu = menu;
            }

            public String getTag_id() {
                return tag_id;
            }

            public void setTag_id(String tag_id) {
                this.tag_id = tag_id;
            }


        }
    }
}
