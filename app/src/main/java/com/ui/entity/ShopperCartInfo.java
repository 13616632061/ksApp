package com.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ShopperCartInfo implements Parcelable {
    String cart_id;//购物车id
    String total_nums;
    String total_amount;
    String seller_id;
    String seller_name;
    String freight;//满减
    String reward;//配送费
    boolean isSelect;
    boolean isSend;//是否配送
    String sendtime;//预约时间
    String message;//留言
    ArrayList<ShopperCartInfo_item> shopperCartInfo_items;

    public ShopperCartInfo(String cart_id, String total_nums, String total_amount, String seller_id, String seller_name, String freight, String reward, boolean isSelect, boolean isSend, String sendtime, String message, ArrayList<ShopperCartInfo_item> shopperCartInfo_items) {
        this.cart_id = cart_id;
        this.total_nums = total_nums;
        this.total_amount = total_amount;
        this.seller_id = seller_id;
        this.seller_name = seller_name;
        this.freight = freight;
        this.reward = reward;
        this.isSelect = isSelect;
        this.isSend = isSend;
        this.sendtime = sendtime;
        this.message = message;
        this.shopperCartInfo_items = shopperCartInfo_items;
    }

    protected ShopperCartInfo(Parcel in) {
        cart_id = in.readString();
        total_nums = in.readString();
        total_amount = in.readString();
        seller_id = in.readString();
        seller_name = in.readString();
        freight = in.readString();
        reward = in.readString();
        isSelect = in.readByte() != 0;
        isSend = in.readByte() != 0;
        sendtime = in.readString();
        message = in.readString();
        shopperCartInfo_items = in.createTypedArrayList(ShopperCartInfo_item.CREATOR);
    }

    public static final Creator<ShopperCartInfo> CREATOR = new Creator<ShopperCartInfo>() {
        @Override
        public ShopperCartInfo createFromParcel(Parcel in) {
            return new ShopperCartInfo(in);
        }

        @Override
        public ShopperCartInfo[] newArray(int size) {
            return new ShopperCartInfo[size];
        }
    };

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getTotal_nums() {
        return total_nums;
    }

    public void setTotal_nums(String total_nums) {
        this.total_nums = total_nums;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ShopperCartInfo_item> getShopperCartInfo_items() {
        return shopperCartInfo_items;
    }

    public void setShopperCartInfo_items(ArrayList<ShopperCartInfo_item> shopperCartInfo_items) {
        this.shopperCartInfo_items = shopperCartInfo_items;
    }

    @Override
    public String toString() {
        return "ShopperCartInfo{" +
                "cart_id='" + cart_id + '\'' +
                ", total_nums='" + total_nums + '\'' +
                ", total_amount='" + total_amount + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", seller_name='" + seller_name + '\'' +
                ", freight='" + freight + '\'' +
                ", reward='" + reward + '\'' +
                ", isSelect=" + isSelect +
                ", isSend=" + isSend +
                ", sendtime='" + sendtime + '\'' +
                ", message='" + message + '\'' +
                ", shopperCartInfo_items=" + shopperCartInfo_items +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cart_id);
        dest.writeString(total_nums);
        dest.writeString(total_amount);
        dest.writeString(seller_id);
        dest.writeString(seller_name);
        dest.writeString(freight);
        dest.writeString(reward);
        dest.writeByte((byte) (isSelect ? 1 : 0));
        dest.writeByte((byte) (isSend ? 1 : 0));
        dest.writeString(sendtime);
        dest.writeString(message);
        dest.writeTypedList(shopperCartInfo_items);
    }
}
