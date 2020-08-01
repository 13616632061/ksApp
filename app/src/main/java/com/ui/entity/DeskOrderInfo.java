package com.ui.entity;

/**
 * Created by Administrator on 2020/3/19.
 */

public class DeskOrderInfo {

    String goods_id;
    String name;
    String price;
    String amount;
    String quantity;
    String unit;
    String is_print;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIs_print() {
        return is_print;
    }

    public void setIs_print(String is_print) {
        this.is_print = is_print;
    }
}
