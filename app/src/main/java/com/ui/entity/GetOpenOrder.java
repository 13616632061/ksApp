package com.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 取单信息
 * Created by Administrator on 2017/4/13.
 */

public class GetOpenOrder implements Parcelable {

    private String order_id;//订单id
    private String creat_time;//订单创建时间
    private String notes;//备注
    private String name;//订单所有的商品名字
    private double price;//订单价格
    private ArrayList<GetOpenOrder_info> getOpenOrder_infos;//订单的商品信息
    private  boolean ischoose;//选中
    private boolean isopen;//商品信息列表是否打开

    public GetOpenOrder(String order_id, String creat_time, String notes, String name,double price,ArrayList<GetOpenOrder_info> getOpenOrder_infos, boolean ischoose,boolean isopen) {
        this.order_id = order_id;
        this.creat_time = creat_time;
        this.notes = notes;
        this.name = name;
        this.getOpenOrder_infos = getOpenOrder_infos;
        this.ischoose = ischoose;
        this.isopen = isopen;
        this.price = price;
    }

    protected GetOpenOrder(Parcel in) {
        order_id = in.readString();
        creat_time = in.readString();
        notes = in.readString();
        name = in.readString();
        price = in.readDouble();
        getOpenOrder_infos = in.createTypedArrayList(GetOpenOrder_info.CREATOR);
        ischoose = in.readByte() != 0;
        isopen = in.readByte() != 0;
    }

    public static final Creator<GetOpenOrder> CREATOR = new Creator<GetOpenOrder>() {
        @Override
        public GetOpenOrder createFromParcel(Parcel in) {
            return new GetOpenOrder(in);
        }

        @Override
        public GetOpenOrder[] newArray(int size) {
            return new GetOpenOrder[size];
        }
    };



    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isopen() {
        return isopen;
    }

    public void setIsopen(boolean isopen) {
        this.isopen = isopen;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCreat_time() {
        return creat_time;
    }

    public void setCreat_time(String creat_time) {
        this.creat_time = creat_time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<GetOpenOrder_info> getGetOpenOrder_infos() {
        return getOpenOrder_infos;
    }

    public void setGetOpenOrder_infos(ArrayList<GetOpenOrder_info> getOpenOrder_infos) {
        this.getOpenOrder_infos = getOpenOrder_infos;
    }

    public boolean ischoose() {
        return ischoose;
    }

    public void setIschoose(boolean ischoose) {
        this.ischoose = ischoose;
    }

    @Override
    public String toString() {
        return "GetOpenOrder{" +
                "order_id='" + order_id + '\'' +
                ", creat_time='" + creat_time + '\'' +
                ", notes='" + notes + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", getOpenOrder_infos=" + getOpenOrder_infos +
                ", ischoose=" + ischoose +
                ", isopen=" + isopen +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(order_id);
        dest.writeString(creat_time);
        dest.writeString(notes);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeTypedList(getOpenOrder_infos);
        dest.writeByte((byte) (ischoose ? 1 : 0));
        dest.writeByte((byte) (isopen ? 1 : 0));
    }
}
