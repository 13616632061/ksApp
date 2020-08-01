package com.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ShopperCartInfo_item implements Parcelable {
    String goods_id;
    String goods_name;
    String goods_nums;
    String goods_price;
    String brief;
    String img_src;
    boolean isSelect;

    public ShopperCartInfo_item(String goods_id, String goods_name, String goods_nums, String goods_price, String brief, String img_src, boolean isSelect) {
        this.goods_id = goods_id;
        this.goods_name = goods_name;
        this.goods_nums = goods_nums;
        this.goods_price = goods_price;
        this.brief = brief;
        this.img_src = img_src;
        this.isSelect = isSelect;
    }

    protected ShopperCartInfo_item(Parcel in) {
        goods_id = in.readString();
        goods_name = in.readString();
        goods_nums = in.readString();
        goods_price = in.readString();
        brief = in.readString();
        img_src = in.readString();
        isSelect = in.readByte() != 0;
    }

    public static final Creator<ShopperCartInfo_item> CREATOR = new Creator<ShopperCartInfo_item>() {
        @Override
        public ShopperCartInfo_item createFromParcel(Parcel in) {
            return new ShopperCartInfo_item(in);
        }

        @Override
        public ShopperCartInfo_item[] newArray(int size) {
            return new ShopperCartInfo_item[size];
        }
    };

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_nums() {
        return goods_nums;
    }

    public void setGoods_nums(String goods_nums) {
        this.goods_nums = goods_nums;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "ShopperCartInfo_item{" +
                "goods_id='" + goods_id + '\'' +
                ", goods_name='" + goods_name + '\'' +
                ", goods_nums='" + goods_nums + '\'' +
                ", goods_price='" + goods_price + '\'' +
                ", brief='" + brief + '\'' +
                ", img_src='" + img_src + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goods_id);
        dest.writeString(goods_name);
        dest.writeString(goods_nums);
        dest.writeString(goods_price);
        dest.writeString(brief);
        dest.writeString(img_src);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }
}
