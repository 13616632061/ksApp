package com.ui.entity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11.
 */

public class Goods_info_type {
    String tag_id;
    String tag_name;
     ArrayList<Goods_info> product_info;

    public Goods_info_type(String tag_id, String tag_name, ArrayList<Goods_info> product_info) {
        this.tag_id = tag_id;
        this.tag_name = tag_name;
        this.product_info = product_info;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public ArrayList<Goods_info> getProduct_info() {
        return product_info;
    }

    public void setProduct_info(ArrayList<Goods_info> product_info) {
        this.product_info = product_info;
    }

    @Override
    public String toString() {
        return "Goods_info_type{" +
                "tag_id='" + tag_id + '\'' +
                ", tag_name='" + tag_name + '\'' +
                ", product_info=" + product_info +
                '}';
    }
}
