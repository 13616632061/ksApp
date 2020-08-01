package com.ui.entity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/14.
 */

public class ShoppingMallShoppingCar  {
    private String shopper_id;
    private String shopper_name;
    private String shopper_notes;
    private boolean select_shopper;
    private ArrayList<ShoppingMallShoppingCar_info> shoppingMallShoppingCar_infos;

    public ShoppingMallShoppingCar(String shopper_id, String shopper_name, String shopper_notes, boolean select_shopper, ArrayList<ShoppingMallShoppingCar_info> shoppingMallShoppingCar_infos) {
        this.shopper_id = shopper_id;
        this.shopper_name = shopper_name;
        this.shopper_notes = shopper_notes;
        this.select_shopper = select_shopper;
        this.shoppingMallShoppingCar_infos = shoppingMallShoppingCar_infos;
    }

    public String getShopper_id() {
        return shopper_id;
    }

    public void setShopper_id(String shopper_id) {
        this.shopper_id = shopper_id;
    }

    public String getShopper_name() {
        return shopper_name;
    }

    public void setShopper_name(String shopper_name) {
        this.shopper_name = shopper_name;
    }

    public String getShopper_notes() {
        return shopper_notes;
    }

    public void setShopper_notes(String shopper_notes) {
        this.shopper_notes = shopper_notes;
    }

    public boolean isSelect_shopper() {
        return select_shopper;
    }

    public void setSelect_shopper(boolean select_shopper) {
        this.select_shopper = select_shopper;
    }

    public ArrayList<ShoppingMallShoppingCar_info> getShoppingMallShoppingCar_infos() {
        return shoppingMallShoppingCar_infos;
    }

    public void setShoppingMallShoppingCar_infos(ArrayList<ShoppingMallShoppingCar_info> shoppingMallShoppingCar_infos) {
        this.shoppingMallShoppingCar_infos = shoppingMallShoppingCar_infos;
    }

    @Override
    public String toString() {
        return "ShoppingMallShoppingCar{" +
                "shopper_id='" + shopper_id + '\'' +
                ", shopper_name='" + shopper_name + '\'' +
                ", shopper_notes='" + shopper_notes + '\'' +
                ", select_shopper=" + select_shopper +
                ", shoppingMallShoppingCar_infos=" + shoppingMallShoppingCar_infos +
                '}';
    }


}
