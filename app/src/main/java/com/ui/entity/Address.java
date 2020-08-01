package com.ui.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    int id, province, city, town, is_default;
    String consignee, address, zipcode, mobile, provinceStr, cityStr, townStr;

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {//该方法用于告诉平台如何从包裹里创建数据类实例
            return new Address(source);
        }
        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public Address(int id, String consignee, int province, int city, int town,
                    String address, String zipcode, String mobile, int is_default, String provinceStr, String cityStr, String townStr){
        this.id = id;
        this.consignee = consignee;
        this.province = province;
        this.city = city;
        this.town = town;
        this.address = address;
        this.zipcode = zipcode;
        this.mobile = mobile;
        this.is_default = is_default;
        this.provinceStr = provinceStr;
        this.cityStr = cityStr;
        this.townStr = townStr;
    }

    public Address(Parcel in){
        this.id = in.readInt();
        this.consignee = in.readString();
        this.province = in.readInt();
        this.city = in.readInt();
        this.town = in.readInt();
        this.address = in.readString();
        this.zipcode = in.readString();
        this.mobile = in.readString();
        this.is_default = in.readInt();
        this.provinceStr = in.readString();
        this.cityStr = in.readString();
        this.townStr = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getId());
        dest.writeString(this.getConsignee());
        dest.writeInt(this.getProvince());
        dest.writeInt(this.getCity());
        dest.writeInt(this.getTown());
        dest.writeString(this.getAddress());
        dest.writeString(this.getZipcode());
        dest.writeString(this.getMobile());
        dest.writeInt(this.getIs_default());
        dest.writeString(this.getProvinceStr());
        dest.writeString(this.getCityStr());
        dest.writeString(this.getTownStr());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getTown() {
        return town;
    }

    public void setTown(int town) {
        this.town = town;
    }

    public String getAreaStr() {
        return getProvinceStr() + "/" + getCityStr() + "/" + getTownStr();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }

    public String getFullAddress(boolean getDefault) {
        String ret = "";
        if(getDefault) {
            if(getIs_default() == 1) {
                ret += "[默认]";
            }
        }
        ret += getFormatArea();
        ret += address;

        return ret;
    }

    public String getFullAddress() {
        return getFullAddress(true);
    }

    public String getFormatArea() {
        return getAreaStr().replace("/", "");
    }


    public String getProvinceStr() {
        return provinceStr;
    }

    public void setProvinceStr(String provinceStr) {
        this.provinceStr = provinceStr;
    }

    public String getCityStr() {
        return cityStr;
    }

    public void setCityStr(String cityStr) {
        this.cityStr = cityStr;
    }

    public String getTownStr() {
        return townStr;
    }

    public void setTownStr(String townStr) {
        this.townStr = townStr;
    }
}

