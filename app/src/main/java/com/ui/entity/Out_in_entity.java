package com.ui.entity;

import java.util.List;

/**
 * Created by admin on 2018/7/9.
 */

public class Out_in_entity {


    /**
     * status : 200
     * message : ok
     * data : [{"id":"4740","seller_id":"7001","store_id":"532","goods_id":"85144","nums":"20","createtime":"1531095353","type":"0","store":"24","name":"康师傅矿泉水1.5L(箱)","cost":"12.000"},{"id":"4741","seller_id":"7001","store_id":"532","goods_id":"85143","nums":"30","createtime":"1531095353","type":"0","store":"34","name":"康师傅矿泉水550ml(箱)*12","cost":"11.000"},{"id":"4742","seller_id":"7001","store_id":"532","goods_id":"85139","nums":"3","createtime":"1531095353","type":"0","store":"3","name":"康师傅 酸梅汤 500ML","cost":"37.000"},{"id":"4743","seller_id":"7001","store_id":"532","goods_id":"96272","nums":"3","createtime":"1531095353","type":"0","store":"3","name":"康师傅茉莉蜜茶500ml*24","cost":"54.000"},{"id":"4744","seller_id":"7001","store_id":"532","goods_id":"96271","nums":"5","createtime":"1531095353","type":"0","store":"5","name":"康师傅茉莉清茶1l*12","cost":"37.920"},{"id":"4745","seller_id":"7001","store_id":"532","goods_id":"93166","nums":"5","createtime":"1531095353","type":"0","store":"6","name":"康师傅冰糖雪梨1l*8瓶","cost":"27.500"},{"id":"4746","seller_id":"7001","store_id":"532","goods_id":"85137","nums":"5","createtime":"1531095353","type":"0","store":"6","name":"康师傅蜂蜜柚子500ml","cost":"37.000"},{"id":"4747","seller_id":"7001","store_id":"532","goods_id":"85136","nums":"5","createtime":"1531095353","type":"0","store":"5","name":"康师傅冰糖雪梨500ml","cost":"30.500"},{"id":"4748","seller_id":"7001","store_id":"532","goods_id":"85134","nums":"5","createtime":"1531095353","type":"0","store":"5","name":"康师傅绿茶1L","cost":"34.500"},{"id":"4749","seller_id":"7001","store_id":"532","goods_id":"85133","nums":"5","createtime":"1531095353","type":"0","store":"9","name":"康师傅绿茶550ml*24","cost":"51.000"},{"id":"4750","seller_id":"7001","store_id":"532","goods_id":"85132","nums":"30","createtime":"1531095353","type":"0","store":"30","name":"康师傅冰红茶1L","cost":"38.000"},{"id":"4751","seller_id":"7001","store_id":"532","goods_id":"85131","nums":"20","createtime":"1531095353","type":"0","store":"20","name":"康师傅冰红茶500ml","cost":"30.500"},{"id":"4752","seller_id":"7001","store_id":"532","goods_id":"85129","nums":"3","createtime":"1531095353","type":"0","store":"3","name":"康师傅冰红茶250ml盒","cost":"25.500"}]
     */

    private String status;
    private String message;
    /**
     * id : 4740
     * seller_id : 7001
     * store_id : 532
     * goods_id : 85144
     * nums : 20
     * createtime : 1531095353
     * type : 0
     * store : 24
     * name : 康师傅矿泉水1.5L(箱)
     * cost : 12.000
     */

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
        private String id;
        private String seller_id;
        private String store_id;
        private String goods_id;
        private String nums;
        private String createtime;
        private String type;
        private String store;
        private String name;
        private String cost;

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

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getNums() {
            return nums;
        }

        public void setNums(String nums) {
            this.nums = nums;
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

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
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
    }
}
