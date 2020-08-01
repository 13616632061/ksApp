package com.ui.entity;

/**
 * 商品类
 * Created by Administrator on 2017/3/14.
 */

public class Goods {
    String goods_photo;
    String tv_goods_name;
    int tv_good_stocknum;
    int tv_good_salesnum;
    int tv_good_price;

    public Goods(String goods_photo, String tv_goods_name,int tv_good_price, int tv_good_salesnum, int tv_good_stocknum) {
        this.goods_photo = goods_photo;
        this.tv_good_price = tv_good_price;
        this.tv_good_salesnum = tv_good_salesnum;
        this.tv_good_stocknum = tv_good_stocknum;
        this.tv_goods_name = tv_goods_name;
    }

    public String getGoods_photo() {
        return goods_photo;
    }

    public void setGoods_photo(String goods_photo) {
        this.goods_photo = goods_photo;
    }

    public int getTv_good_price() {
        return tv_good_price;
    }

    public void setTv_good_price(int tv_good_price) {
        this.tv_good_price = tv_good_price;
    }

    public int getTv_good_salesnum() {
        return tv_good_salesnum;
    }

    public void setTv_good_salesnum(int tv_good_salesnum) {
        this.tv_good_salesnum = tv_good_salesnum;
    }

    public int getTv_good_stocknum() {
        return tv_good_stocknum;
    }

    public void setTv_good_stocknum(int tv_good_stocknum) {
        this.tv_good_stocknum = tv_good_stocknum;
    }

    public String getTv_goods_name() {
        return tv_goods_name;
    }

    public void setTv_goods_name(String tv_goods_name) {
        this.tv_goods_name = tv_goods_name;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goods_photo='" + goods_photo + '\'' +
                ", tv_goods_name='" + tv_goods_name + '\'' +
                ", tv_good_stocknum=" + tv_good_stocknum +
                ", tv_good_salesnum=" + tv_good_salesnum +
                ", tv_good_price=" + tv_good_price +
                '}';
    }
}
