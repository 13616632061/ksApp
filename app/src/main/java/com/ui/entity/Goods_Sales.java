package com.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/5/24.
 */

public class Goods_Sales implements Parcelable{
    private String goods_name;
    private int goods_sales_num;
    private double goods_sales_money;
    private double goods_sales_profit;
    private String goods_sales_profit_num;
    private String img_src;

    public Goods_Sales(String goods_name, int goods_sales_num, double goods_sales_money, double goods_sales_profit, String goods_sales_profit_num, String img_src) {
        this.goods_name = goods_name;
        this.goods_sales_num = goods_sales_num;
        this.goods_sales_money = goods_sales_money;
        this.goods_sales_profit = goods_sales_profit;
        this.goods_sales_profit_num = goods_sales_profit_num;
        this.img_src = img_src;
    }

    protected Goods_Sales(Parcel in) {
        goods_name = in.readString();
        goods_sales_num = in.readInt();
        goods_sales_money = in.readDouble();
        goods_sales_profit = in.readDouble();
        goods_sales_profit_num = in.readString();
        img_src = in.readString();
    }

    public static final Creator<Goods_Sales> CREATOR = new Creator<Goods_Sales>() {
        @Override
        public Goods_Sales createFromParcel(Parcel in) {
            return new Goods_Sales(in);
        }

        @Override
        public Goods_Sales[] newArray(int size) {
            return new Goods_Sales[size];
        }
    };

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getGoods_sales_num() {
        return goods_sales_num;
    }

    public void setGoods_sales_num(int goods_sales_num) {
        this.goods_sales_num = goods_sales_num;
    }

    public double getGoods_sales_money() {
        return goods_sales_money;
    }

    public void setGoods_sales_money(double goods_sales_money) {
        this.goods_sales_money = goods_sales_money;
    }

    public double getGoods_sales_profit() {
        return goods_sales_profit;
    }

    public void setGoods_sales_profit(double goods_sales_profit) {
        this.goods_sales_profit = goods_sales_profit;
    }

    public String getGoods_sales_profit_num() {
        return goods_sales_profit_num;
    }

    public void setGoods_sales_profit_num(String goods_sales_profit_num) {
        this.goods_sales_profit_num = goods_sales_profit_num;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    @Override
    public String toString() {
        return "Goods_Sales{" +
                "goods_name='" + goods_name + '\'' +
                ", goods_sales_num=" + goods_sales_num +
                ", goods_sales_money=" + goods_sales_money +
                ", goods_sales_profit=" + goods_sales_profit +
                ", goods_sales_profit_num='" + goods_sales_profit_num + '\'' +
                ", img_src='" + img_src + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goods_name);
        dest.writeInt(goods_sales_num);
        dest.writeDouble(goods_sales_money);
        dest.writeDouble(goods_sales_profit);
        dest.writeString(goods_sales_profit_num);
        dest.writeString(img_src);
    }
}
