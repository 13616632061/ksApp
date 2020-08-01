package com.ui.entity;

/**
 * 商品列表选中状态类
 * Created by Administrator on 2017/3/24.
 */

public class GoodList_Item_check {
    private boolean checked;

    public GoodList_Item_check() {
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "GoodList_Item_check{" +
                "checked=" + checked +
                '}';
    }
}
