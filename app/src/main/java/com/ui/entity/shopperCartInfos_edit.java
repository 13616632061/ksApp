package com.ui.entity;

/**
 * Created by Administrator on 2017/7/3.
 */

public class shopperCartInfos_edit {
   private String goods_id;
   private String goods_name;
    private String goods_nums;
   private String goods_price;

    public shopperCartInfos_edit(String goods_id, String goods_name, String goods_nums, String goods_price) {
        this.goods_id = goods_id;
        this.goods_name = goods_name;
        this.goods_nums = goods_nums;
        this.goods_price = goods_price;
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

    public String getGoods_nums() {
        return goods_nums;
    }

    public void setGoods_nums(String goods_nums) {
        this.goods_nums = goods_nums;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    @Override
    public String toString() {
        return "shopperCartInfos{" +
                "goods_id='" + goods_id + '\'' +
                ", goods_name='" + goods_name + '\'' +
                ", goods_nums='" + goods_nums + '\'' +
                ", goods_price='" + goods_price + '\'' +
                '}';
    }
}
