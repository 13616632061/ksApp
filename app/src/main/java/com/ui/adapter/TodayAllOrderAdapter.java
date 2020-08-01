package com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ui.entity.Order;
import com.ui.ks.R;

import java.util.ArrayList;

/**
 * 今日所有订单适配器
 * Created by Administrator on 2017/1/3.
 */

public class TodayAllOrderAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Order> orderList;
    private int type;

    public TodayAllOrderAdapter(Context mContext, ArrayList<Order> orderList,int type) {
        this.mContext = mContext;
        this.orderList = orderList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_orderfragment,null);
            holder=new Holder();
            holder.tv_orderlist_date= (TextView) convertView.findViewById(R.id.tv_orderlist_date);
            holder.tv_orderlist_time= (TextView) convertView.findViewById(R.id.tv_orderlist_time);
            holder.tv_payprice= (TextView) convertView.findViewById(R.id.tv_payprice);
            holder.iv_payment= (ImageView) convertView.findViewById(R.id.iv_payment);
            holder.tv_sendtype= (TextView) convertView.findViewById(R.id.tv_sendtype);
            holder.tv_payment= (TextView) convertView.findViewById(R.id.tv_payment);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        Order data=orderList.get(position);
        /**
         * type=1,今天的订单列表，type=2,昨天的订单列表，type=0,订单搜索结果列表
         */
        if(data!=null){
            if(type==1){
                holder.tv_orderlist_date.setText(mContext.getResources().getString(R.string.today));//今天
            }else if(type==2){
                holder.tv_orderlist_date.setText(mContext.getResources().getString(R.string.str297));//昨天
            }else if(type==0){
                holder.tv_orderlist_date.setText((data.getOrderTime().substring(0,10)));
            }
            else {
                holder.tv_orderlist_date.setVisibility(View.GONE);
            }
            holder.tv_orderlist_time.setText((data.getOrderTime().substring(11)).substring(0,5));//取后面时间字符,并去掉秒
            holder.tv_payment.setText(data.getPaymentStr(mContext));
            holder.tv_sendtype.setText(data.getShippingStr(mContext,holder.tv_sendtype));
            holder.iv_payment.setImageResource(data.getPaymentRes());
            if (data.getCost_item() > 0) {
                holder.tv_payprice.setText(data.getCost_item()+"");
            } else {
                holder.tv_payprice.setText("");
            }

        }

        return convertView;
    }

    public class Holder{
        TextView tv_orderlist_time;//订单时间
        TextView tv_orderlist_date;//订单日期
        TextView tv_payprice;//订单价格
        ImageView iv_payment;//支付图片
        TextView tv_sendtype;
        TextView tv_payment;

    }

}

