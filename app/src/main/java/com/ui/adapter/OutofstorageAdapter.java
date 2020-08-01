package com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ui.entity.outofstoragelist;
import com.ui.ks.R;
import com.ui.util.DateUtils;

import java.util.List;

/**
 * 今日所有订单适配器
 * Created by Administrator on 2017/1/3.
 */

public class OutofstorageAdapter extends BaseAdapter {

    private Context mContext;
    private List<outofstoragelist> orderList;

    public OutofstorageAdapter(Context mContext, List<outofstoragelist> orderList) {
        this.mContext = mContext;
        this.orderList = orderList;
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
            convertView=View.inflate(mContext, R.layout.item_outofstorage,null);
            holder=new Holder();
            holder.tv_orderlist_date= (TextView) convertView.findViewById(R.id.tv_orderlist_date);
            holder.tv_orderlist_time= (TextView) convertView.findViewById(R.id.tv_orderlist_time);
            holder.tv_payprice= (TextView) convertView.findViewById(R.id.tv_payprice);
            holder.tv_sendtype= (TextView) convertView.findViewById(R.id.tv_sendtype);
            holder.tv_payment= (TextView) convertView.findViewById(R.id.tv_payment);
            holder.tv_type= (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_operator= (TextView) convertView.findViewById(R.id.tv_operator);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }


        holder.tv_orderlist_date.setText(orderList.get(position).getOrder_id()+"");
        holder.tv_orderlist_time.setText(DateUtils.getDateTimeFromMillisecondYMD(Long.parseLong(orderList.get(position).getCreatetime())*1000));
        holder.tv_payprice.setText(Double.parseDouble(orderList.get(position).getMoney())+"");
        if (orderList.get(position).getType().equals("0")) {
            holder.tv_type.setText("入库");
        } else {
            holder.tv_type.setText("出库");
        }

//        holder.tv_operator.setText(data.getOparator());

        return convertView;
    }

    public class Holder{
        TextView tv_orderlist_time;//订单时间
        TextView tv_orderlist_date;//订单日期
        TextView tv_payprice;//订单价格
        TextView tv_sendtype;
        TextView tv_payment;
        TextView tv_type;
        TextView tv_operator;

    }

}

