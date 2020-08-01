package com.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 取单订单商品信息实体类
 * Created by Administrator on 2017/4/13.
 */

public class GetOpenOrder_info implements Parcelable{
    private String id;//商品id
    private String tagid;//商品所属分类id
    private String name;//商品名字
    private String num;//商品数量
    private String price;//商品价格
    private String obj_id;//商品价格
    private String item_id;//商品价格

    public GetOpenOrder_info(String id, String tagid, String name, String num, String price, String obj_id, String item_id) {
        this.id = id;
        this.tagid = tagid;
        this.name = name;
        this.num = num;
        this.price = price;
        this.obj_id = obj_id;
        this.item_id = item_id;
    }

    protected GetOpenOrder_info(Parcel in) {
        id = in.readString();
        tagid = in.readString();
        name = in.readString();
        num = in.readString();
        price = in.readString();
        obj_id = in.readString();
        item_id = in.readString();
    }

    public static final Creator<GetOpenOrder_info> CREATOR = new Creator<GetOpenOrder_info>() {
        @Override
        public GetOpenOrder_info createFromParcel(Parcel in) {
            return new GetOpenOrder_info(in);
        }

        @Override
        public GetOpenOrder_info[] newArray(int size) {
            return new GetOpenOrder_info[size];
        }
    };

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getObj_id() {
        return obj_id;
    }

    public void setObj_id(String obj_id) {
        this.obj_id = obj_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    @Override
    public String toString() {
        return "GetOpenOrder_info{" +
                "id='" + id + '\'' +
                ", tagid='" + tagid + '\'' +
                ", name='" + name + '\'' +
                ", num='" + num + '\'' +
                ", price='" + price + '\'' +
                ", obj_id='" + obj_id + '\'' +
                ", item_id='" + item_id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(tagid);
        dest.writeString(name);
        dest.writeString(num);
        dest.writeString(price);
        dest.writeString(obj_id);
        dest.writeString(item_id);
    }
}
