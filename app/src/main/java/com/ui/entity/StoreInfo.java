package com.ui.entity;

/**
 * 分店信息
 * Created by Administrator on 2017/1/18.
 */

public class StoreInfo {
    private int store_num;
    private String store_no;
    private String store_name;

    public StoreInfo(int store_num, String store_no, String store_name) {
        this.store_num = store_num;
        this.store_no = store_no;
        this.store_name = store_name;
    }

    public int getStore_num() {
        return store_num;
    }

    public void setStore_num(int store_num) {
        this.store_num = store_num;
    }

    public String getStore_no() {
        return store_no;
    }

    public void setStore_no(String store_no) {
        this.store_no = store_no;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    @Override
    public String toString() {
        return "StoreInfo{" +
                "store_num=" + store_num +
                ", store_no='" + store_no + '\'' +
                ", store_name='" + store_name + '\'' +
                '}';
    }
}
