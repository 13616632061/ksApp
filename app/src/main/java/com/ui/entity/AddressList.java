package com.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/6/29.
 */

public class AddressList implements Parcelable{
    String id;
    String seller_id;
    String name;
    String mobile;
    String area;
    String addr;
    String acquiesce;

    public AddressList(String id, String seller_id, String name, String mobile, String area, String addr, String acquiesce) {
        this.id = id;
        this.seller_id = seller_id;
        this.name = name;
        this.mobile = mobile;
        this.area = area;
        this.addr = addr;
        this.acquiesce = acquiesce;
    }

    protected AddressList(Parcel in) {
        id = in.readString();
        seller_id = in.readString();
        name = in.readString();
        mobile = in.readString();
        area = in.readString();
        addr = in.readString();
        acquiesce = in.readString();
    }

    public static final Creator<AddressList> CREATOR = new Creator<AddressList>() {
        @Override
        public AddressList createFromParcel(Parcel in) {
            return new AddressList(in);
        }

        @Override
        public AddressList[] newArray(int size) {
            return new AddressList[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAcquiesce() {
        return acquiesce;
    }

    public void setAcquiesce(String acquiesce) {
        this.acquiesce = acquiesce;
    }

    @Override
    public String toString() {
        return "AddressList{" +
                "id='" + id + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", area='" + area + '\'' +
                ", addr='" + addr + '\'' +
                ", acquiesce='" + acquiesce + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(seller_id);
        dest.writeString(name);
        dest.writeString(mobile);
        dest.writeString(area);
        dest.writeString(addr);
        dest.writeString(acquiesce);
    }
}
