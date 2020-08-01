package com.ui.entity;

/**
 * 开单页面的商品的加减
 * Created by Administrator on 2017/4/1.
 */

public class OpenOrderGoodList_Choose {
    private boolean checked;
    private int num;
    private String id;

    public OpenOrderGoodList_Choose(String id,boolean checked, int num) {
        this.checked = checked;
        this.num = num;
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "OpenOrderGoodList_Choose{" +
                "checked=" + checked +
                ", num=" + num +
                ", id='" + id + '\'' +
                '}';
    }
}
