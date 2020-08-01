package com.ui.entity;

/**
 * Created by Administrator on 2017/1/12.
 */

public class MonthDay_Order {
        String order_date;
        String order_total_money;
        String order_total_num;

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public  String getOrder_total_money() {
        return order_total_money;
    }

    public void setOrder_total_money( String order_total_money) {
        this.order_total_money = order_total_money;
    }

    public String getOrder_total_num() {
        return order_total_num;
    }

    public void setOrder_total_num(String order_total_num) {
        this.order_total_num = order_total_num;
    }

    public MonthDay_Order(String order_date,  String order_total_money, String order_total_num) {
        this.order_date = order_date;
        this.order_total_money = order_total_money;
        this.order_total_num = order_total_num;
    }

    @Override
    public String toString() {
        return "MonthDay_Order{" +
                "order_date='" + order_date + '\'' +
                ", order_total_money='" + order_total_money + '\'' +
                ", order_total_num='" + order_total_num + '\'' +
                '}';
    }
}
