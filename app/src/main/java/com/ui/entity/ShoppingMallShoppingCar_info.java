package com.ui.entity;

import android.os.Parcel;

/**
 * Created by Administrator on 2017/6/14.
 */

public class ShoppingMallShoppingCar_info  {
    private String shopping_picture;
    private String shopping_id;
    private String shopping_name;
    private String shopping_details;
    private String shopping_price;
    private String shopping_nums;
    private boolean is_select;

    public ShoppingMallShoppingCar_info(String shopping_picture, String shopping_id, String shopping_name, String shopping_details, String shopping_price, String shopping_nums, boolean is_select) {
        this.shopping_picture = shopping_picture;
        this.shopping_id = shopping_id;
        this.shopping_name = shopping_name;
        this.shopping_details = shopping_details;
        this.shopping_price = shopping_price;
        this.shopping_nums = shopping_nums;
        this.is_select = is_select;
    }

    public String getShopping_picture() {
        return shopping_picture;
    }

    public void setShopping_picture(String shopping_picture) {
        this.shopping_picture = shopping_picture;
    }

    public String getShopping_id() {
        return shopping_id;
    }

    public void setShopping_id(String shopping_id) {
        this.shopping_id = shopping_id;
    }

    public String getShopping_name() {
        return shopping_name;
    }

    public void setShopping_name(String shopping_name) {
        this.shopping_name = shopping_name;
    }

    public String getShopping_details() {
        return shopping_details;
    }

    public void setShopping_details(String shopping_details) {
        this.shopping_details = shopping_details;
    }

    public String getShopping_price() {
        return shopping_price;
    }

    public void setShopping_price(String shopping_price) {
        this.shopping_price = shopping_price;
    }

    public String getShopping_nums() {
        return shopping_nums;
    }

    public void setShopping_nums(String shopping_nums) {
        this.shopping_nums = shopping_nums;
    }

    public boolean is_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    @Override
    public String toString() {
        return "ShoppingMallShoppingCar_info{" +
                "shopping_picture='" + shopping_picture + '\'' +
                ", shopping_id='" + shopping_id + '\'' +
                ", shopping_name='" + shopping_name + '\'' +
                ", shopping_details='" + shopping_details + '\'' +
                ", shopping_price='" + shopping_price + '\'' +
                ", shopping_nums='" + shopping_nums + '\'' +
                ", is_select=" + is_select +
                '}';
    }

    protected ShoppingMallShoppingCar_info(Parcel in) {
        shopping_picture = in.readString();
        shopping_id = in.readString();
        shopping_name = in.readString();
        shopping_details = in.readString();
        shopping_price = in.readString();
        shopping_nums = in.readString();
        is_select = in.readByte() != 0;
    }

}
