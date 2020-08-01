package com.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 商品列表信息
 * Created by Administrator on 2017/3/17.
 */

public class Goods_info implements Parcelable {

    String name;//商品名字
    String imageurl;//图片
    double price;//价格
    double store;//库存
    int buy_count;//
    int btn_switch_type;//有码，无码
    String goods_id;//商品id
    String marketable;//上下架
    int select_num;//销量
    String tag_id;//类id
    boolean ischoose;//是否xuanze
    int num;//选择的数量
    String obj_id;
    String item_id;
    String increase;//增幅

    public Goods_info(String name, String marketable, String goods_id, int buy_count, double store, double price, String imageurl, int btn_switch_type,
                      int select_num, String tag_id,boolean ischoose,int num,String obj_id,String item_id) {
        this.name = name;
        this.marketable = marketable;
        this.goods_id = goods_id;
        this.btn_switch_type = btn_switch_type;
        this.buy_count = buy_count;
        this.store = store;
        this.price = price;
        this.imageurl = imageurl;
        this.select_num =select_num;
        this.tag_id =tag_id;
        this.ischoose=ischoose;
        this.num =num;
        this.obj_id =obj_id;
        this.item_id =item_id;
    }

    protected Goods_info(Parcel in) {
        name = in.readString();
        imageurl = in.readString();
        price = in.readDouble();
        store = in.readDouble();
        buy_count = in.readInt();
        btn_switch_type = in.readInt();
        goods_id = in.readString();
        marketable = in.readString();
        select_num = in.readInt();
        tag_id = in.readString();
        ischoose = in.readByte() != 0;
        num = in.readInt();
        obj_id = in.readString();
        item_id = in.readString();
        increase = in.readString();
    }

    public static final Creator<Goods_info> CREATOR = new Creator<Goods_info>() {
        @Override
        public Goods_info createFromParcel(Parcel in) {
            return new Goods_info(in);
        }

        @Override
        public Goods_info[] newArray(int size) {
            return new Goods_info[size];
        }
    };

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

    public boolean ischoose() {
        return ischoose;
    }

    public void setIschoose(boolean ischoose) {
        this.ischoose = ischoose;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public int getSelect_num() {
        return select_num;
    }

    public void setSelect_num(int select_num) {
        this.select_num = select_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarketable() {
        return marketable;
    }

    public void setMarketable(String marketable) {
        this.marketable = marketable;
    }

    public int getBtn_switch_type() {
        return btn_switch_type;
    }

    public void setBtn_switch_type(int btn_switch_type) {
        this.btn_switch_type = btn_switch_type;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public int getBuy_count() {
        return buy_count;
    }

    public void setBuy_count(int buy_count) {
        this.buy_count = buy_count;
    }

    public double getStore() {
        return store;
    }

    public void setStore(double store) {
        this.store = store;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }



    @Override
    public String toString() {
        return "Goods_info{" +
                "name='" + name + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", price=" + price +
                ", store=" + store +
                ", buy_count=" + buy_count +
                ", btn_switch_type=" + btn_switch_type +
                ", goods_id='" + goods_id + '\'' +
                ", marketable='" + marketable + '\'' +
                ", select_num=" + select_num +
                ", tag_id='" + tag_id + '\'' +
                ", ischoose=" + ischoose +
                ", num=" + num +
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
        dest.writeString(name);
        dest.writeString(imageurl);
        dest.writeDouble(price);
        dest.writeDouble(store);
        dest.writeInt(buy_count);
        dest.writeInt(btn_switch_type);
        dest.writeString(goods_id);
        dest.writeString(marketable);
        dest.writeInt(select_num);
        dest.writeString(tag_id);
        dest.writeByte((byte) (ischoose ? 1 : 0));
        dest.writeInt(num);
        dest.writeString(obj_id);
        dest.writeString(item_id);
    }
}
