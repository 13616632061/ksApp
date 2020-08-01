package com.ui.entity;

/**
 * Created by Administrator on 2017/7/3.
 */

public class shopperCartInfos_edit_all {
    private String cat_id;
    private String seller_id;
    private String nums;
    private String amount;
    private String commodity;

    public shopperCartInfos_edit_all(String cat_id, String seller_id, String nums, String amount, String commodity) {
        this.cat_id = cat_id;
        this.seller_id = seller_id;
        this.nums = nums;
        this.amount = amount;
        this.commodity = commodity;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    @Override
    public String toString() {
        return "shopperCartInfos_edit_all{" +
                "cat_id='" + cat_id + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", nums='" + nums + '\'' +
                ", amount='" + amount + '\'' +
                ", commodity='" + commodity + '\'' +
                '}';
    }
}
