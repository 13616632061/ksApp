package com.ui.entity;

/**
 * 附近商家信息
 * Created by Administrator on 2017/6/8.
 */

public class NearlyShopper {
    private String Shopperid;
    private String shopperphoto;
    private String shoppername;
    private String shopperdistance;
    private String shoppernotes;

    public NearlyShopper(String shopperid, String shopperphoto, String shoppername, String shopperdistance, String shoppernotes) {
        Shopperid = shopperid;
        this.shopperphoto = shopperphoto;
        this.shoppername = shoppername;
        this.shopperdistance = shopperdistance;
        this.shoppernotes = shoppernotes;
    }

    public String getShopperid() {
        return Shopperid;
    }

    public void setShopperid(String shopperid) {
        Shopperid = shopperid;
    }

    public String getShopperphoto() {
        return shopperphoto;
    }

    public void setShopperphoto(String shopperphoto) {
        this.shopperphoto = shopperphoto;
    }

    public String getShoppername() {
        return shoppername;
    }

    public void setShoppername(String shoppername) {
        this.shoppername = shoppername;
    }

    public String getShopperdistance() {
        return shopperdistance;
    }

    public void setShopperdistance(String shopperdistance) {
        this.shopperdistance = shopperdistance;
    }

    public String getShoppernotes() {
        return shoppernotes;
    }

    public void setShoppernotes(String shoppernotes) {
        this.shoppernotes = shoppernotes;
    }

    @Override
    public String toString() {
        return "NearlyShopper{" +
                "Shopperid='" + Shopperid + '\'' +
                ", shopperphoto='" + shopperphoto + '\'' +
                ", shoppername='" + shoppername + '\'' +
                ", shopperdistance='" + shopperdistance + '\'' +
                ", shoppernotes='" + shoppernotes + '\'' +
                '}';
    }
}
