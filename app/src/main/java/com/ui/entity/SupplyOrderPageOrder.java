package com.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/6.
 */

public class SupplyOrderPageOrder implements Parcelable{
    String order_id;//订单id
    String seller_id;//供应商id
    String seller_name;//供应商名字
    String final_amount;//订单总金额
    String cost_item;//商品总金额
    String cost_freight;//总配送费
    String createtime;//下单时间
    String total_quantity;//订单总数量
    String ship_name;//收货人名字
    String ship_tel;//收货人电话
    String ship_area;//收货地址区域
    String ship_addr;//收货详细地址
    String ship_time;//与配送时间
    String payment;//付款方式
    String pay_status;//付款状态,0未支付，1已支付，2已付款至担保方，3部分付款，4部分退款，5全额退款，6退款中
    String ship_status;//送货状态，0未发货，1已发货，2部分发货，3部分退货，4以退货，5已签收
    String status;//订单状态 active活动订单，dead已作废，finish已完成
    ArrayList<OrderPageorder> mOrderPageorderlist;//商品信息

    public SupplyOrderPageOrder(String order_id, String seller_id, String seller_name, String final_amount, String cost_item, String cost_freight, String createtime, String total_quantity, String ship_name, String ship_tel, String ship_area, String ship_addr, String ship_time, String payment, String pay_status, String ship_status, String status, ArrayList<OrderPageorder> mOrderPageorderlist) {
        this.order_id = order_id;
        this.seller_id = seller_id;
        this.seller_name = seller_name;
        this.final_amount = final_amount;
        this.cost_item = cost_item;
        this.cost_freight = cost_freight;
        this.createtime = createtime;
        this.total_quantity = total_quantity;
        this.ship_name = ship_name;
        this.ship_tel = ship_tel;
        this.ship_area = ship_area;
        this.ship_addr = ship_addr;
        this.ship_time = ship_time;
        this.payment = payment;
        this.pay_status = pay_status;
        this.ship_status = ship_status;
        this.status = status;
        this.mOrderPageorderlist = mOrderPageorderlist;
    }

    protected SupplyOrderPageOrder(Parcel in) {
        order_id = in.readString();
        seller_id = in.readString();
        seller_name = in.readString();
        final_amount = in.readString();
        cost_item = in.readString();
        cost_freight = in.readString();
        createtime = in.readString();
        total_quantity = in.readString();
        ship_name = in.readString();
        ship_tel = in.readString();
        ship_area = in.readString();
        ship_addr = in.readString();
        ship_time = in.readString();
        payment = in.readString();
        pay_status = in.readString();
        ship_status = in.readString();
        status = in.readString();
        mOrderPageorderlist = in.createTypedArrayList(OrderPageorder.CREATOR);
    }

    public static final Creator<SupplyOrderPageOrder> CREATOR = new Creator<SupplyOrderPageOrder>() {
        @Override
        public SupplyOrderPageOrder createFromParcel(Parcel in) {
            return new SupplyOrderPageOrder(in);
        }

        @Override
        public SupplyOrderPageOrder[] newArray(int size) {
            return new SupplyOrderPageOrder[size];
        }
    };

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(String final_amount) {
        this.final_amount = final_amount;
    }

    public String getCost_item() {
        return cost_item;
    }

    public void setCost_item(String cost_item) {
        this.cost_item = cost_item;
    }

    public String getCost_freight() {
        return cost_freight;
    }

    public void setCost_freight(String cost_freight) {
        this.cost_freight = cost_freight;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getShip_name() {
        return ship_name;
    }

    public void setShip_name(String ship_name) {
        this.ship_name = ship_name;
    }

    public String getShip_tel() {
        return ship_tel;
    }

    public void setShip_tel(String ship_tel) {
        this.ship_tel = ship_tel;
    }

    public String getShip_area() {
        return ship_area;
    }

    public void setShip_area(String ship_area) {
        this.ship_area = ship_area;
    }

    public String getShip_addr() {
        return ship_addr;
    }

    public void setShip_addr(String ship_addr) {
        this.ship_addr = ship_addr;
    }

    public String getShip_time() {
        return ship_time;
    }

    public void setShip_time(String ship_time) {
        this.ship_time = ship_time;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getShip_status() {
        return ship_status;
    }

    public void setShip_status(String ship_status) {
        this.ship_status = ship_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<OrderPageorder> getmOrderPageorderlist() {
        return mOrderPageorderlist;
    }

    public void setmOrderPageorderlist(ArrayList<OrderPageorder> mOrderPageorderlist) {
        this.mOrderPageorderlist = mOrderPageorderlist;
    }

    @Override
    public String toString() {
        return "SupplyOrderPageOrder{" +
                "order_id='" + order_id + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", seller_name='" + seller_name + '\'' +
                ", final_amount='" + final_amount + '\'' +
                ", cost_item='" + cost_item + '\'' +
                ", cost_freight='" + cost_freight + '\'' +
                ", createtime='" + createtime + '\'' +
                ", total_quantity='" + total_quantity + '\'' +
                ", ship_name='" + ship_name + '\'' +
                ", ship_tel='" + ship_tel + '\'' +
                ", ship_area='" + ship_area + '\'' +
                ", ship_addr='" + ship_addr + '\'' +
                ", ship_time='" + ship_time + '\'' +
                ", payment='" + payment + '\'' +
                ", pay_status='" + pay_status + '\'' +
                ", ship_status='" + ship_status + '\'' +
                ", status='" + status + '\'' +
                ", mOrderPageorderlist=" + mOrderPageorderlist +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(order_id);
        dest.writeString(seller_id);
        dest.writeString(seller_name);
        dest.writeString(final_amount);
        dest.writeString(cost_item);
        dest.writeString(cost_freight);
        dest.writeString(createtime);
        dest.writeString(total_quantity);
        dest.writeString(ship_name);
        dest.writeString(ship_tel);
        dest.writeString(ship_area);
        dest.writeString(ship_addr);
        dest.writeString(ship_time);
        dest.writeString(payment);
        dest.writeString(pay_status);
        dest.writeString(ship_status);
        dest.writeString(status);
        dest.writeTypedList(mOrderPageorderlist);
    }
}
