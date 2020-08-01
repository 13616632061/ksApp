package com.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/6.
 */

public class OrderPageorder implements Parcelable{
    String goods_id;//商品id
    String name;//商品名字
    String price;//商品价格
    String quantity;//商品数量
    String brief;//商品描述
    String img_src;//图片url

    public OrderPageorder(String goods_id, String name, String price, String quantity, String brief, String img_src) {
        this.goods_id = goods_id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.brief = brief;
        this.img_src = img_src;
    }

    protected OrderPageorder(Parcel in) {
        goods_id = in.readString();
        name = in.readString();
        price = in.readString();
        quantity = in.readString();
        brief = in.readString();
        img_src = in.readString();
    }

    public static final Creator<OrderPageorder> CREATOR = new Creator<OrderPageorder>() {
        @Override
        public OrderPageorder createFromParcel(Parcel in) {
            return new OrderPageorder(in);
        }

        @Override
        public OrderPageorder[] newArray(int size) {
            return new OrderPageorder[size];
        }
    };

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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    @Override
    public String toString() {
        return "OrderPageorder{" +
                "goods_id='" + goods_id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                ", brief='" + brief + '\'' +
                ", img_src='" + img_src + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goods_id);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(quantity);
        dest.writeString(brief);
        dest.writeString(img_src);
    }
}
