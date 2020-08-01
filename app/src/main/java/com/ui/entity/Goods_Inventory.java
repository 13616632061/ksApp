package com.ui.entity;

/**
 * Created by Administrator on 2020/3/2.
 */

public class Goods_Inventory {

    String goods_id;
    String name;
    String cost;
    String store;
    String reality_store;
    String img_src;
    String bncode;


    public String getBncode() {
        return bncode;
    }

    public void setBncode(String bncode) {
        this.bncode = bncode;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
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

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getReality_store() {
        return reality_store;
    }

    public void setReality_store(String reality_store) {
        this.reality_store = reality_store;
    }
}
