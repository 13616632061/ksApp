package com.ui.entity;

import java.util.List;

/**
 * Created by Administrator on 2020/3/19.
 */

public class DeskOrder {

    String order_id;
    String memo;
    String mark_text;
    String status;
    String pay_status;
    String createtime;
    String total_amount;
    String desk_num;
    String eat_num;
    String menu_confirm;
    String is_menu_bind;
    List<DeskOrderInfo> deskOrderInfos;


    public List<DeskOrderInfo> getDeskOrderInfos() {
        return deskOrderInfos;
    }

    public void setDeskOrderInfos(List<DeskOrderInfo> deskOrderInfos) {
        this.deskOrderInfos = deskOrderInfos;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMark_text() {
        return mark_text;
    }

    public void setMark_text(String mark_text) {
        this.mark_text = mark_text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getDesk_num() {
        return desk_num;
    }

    public void setDesk_num(String desk_num) {
        this.desk_num = desk_num;
    }

    public String getEat_num() {
        return eat_num;
    }

    public void setEat_num(String eat_num) {
        this.eat_num = eat_num;
    }

    public String getMenu_confirm() {
        return menu_confirm;
    }

    public void setMenu_confirm(String menu_confirm) {
        this.menu_confirm = menu_confirm;
    }

    public String getIs_menu_bind() {
        return is_menu_bind;
    }

    public void setIs_menu_bind(String is_menu_bind) {
        this.is_menu_bind = is_menu_bind;
    }
}
