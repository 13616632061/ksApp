package com.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Administrator on 2017/6/15.
 */

public class ShopperSortInfo implements Parcelable {
    String cat_id;
    String cat_name;
    String cat_icon;

    public ShopperSortInfo(String cat_id, String cat_name, String cat_icon) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.cat_icon = cat_icon;
    }

    protected ShopperSortInfo(Parcel in) {
        cat_id = in.readString();
        cat_name = in.readString();
        cat_icon = in.readString();
    }

    public static final Creator<ShopperSortInfo> CREATOR = new Creator<ShopperSortInfo>() {
        @Override
        public ShopperSortInfo createFromParcel(Parcel in) {
            return new ShopperSortInfo(in);
        }

        @Override
        public ShopperSortInfo[] newArray(int size) {
            return new ShopperSortInfo[size];
        }
    };

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_icon() {
        return cat_icon;
    }

    public void setCat_icon(String cat_icon) {
        this.cat_icon = cat_icon;
    }

    @Override
    public String toString() {
        return "ShopperSortInfo{" +
                "cat_id='" + cat_id + '\'' +
                ", cat_name='" + cat_name + '\'' +
                ", cat_icon='" + cat_icon + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cat_id);
        dest.writeString(cat_name);
        dest.writeString(cat_icon);
    }
}
