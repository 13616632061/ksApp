package com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ui.entity.MonthDay_Order;
import com.ui.ks.R;
import com.ui.util.DateUtils;


import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * 本月，上月营业统计适配器
 * Created by Administrator on 2017/1/12.
 */

public class MonthAllOrderAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<MonthDay_Order>  monthDays;
    private DecimalFormat df   = new DecimalFormat("######0.00");//小数点两位

    public MonthAllOrderAdapter(Context context, ArrayList<MonthDay_Order> monthDays) {
        this.context = context;
        this.monthDays = monthDays;
    }

    @Override
    public int getCount() {
        return monthDays.size();
    }

    @Override
    public Object getItem(int position) {
        return monthDays.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if (convertView==null){
                convertView=View.inflate(context, R.layout.monthallorderadapter_layout,null);
                holder=new Holder();
            holder.tv_order_date= (TextView) convertView.findViewById(R.id.tv_order_date);
            holder.tv_week= (TextView) convertView.findViewById(R.id.tv_monthweek);
            holder.tv_order_total_money= (TextView) convertView.findViewById(R.id.tv_order_total_money);
            holder.tv_order_total_num= (TextView) convertView.findViewById(R.id.tv_order_total_num);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        if(monthDays!=null) {
            holder.tv_order_date.setText(monthDays.get(position).getOrder_date());
            holder.tv_week.setText(DateUtils.friendly_time2(context,monthDays.get(position).getOrder_date()));

            if(monthDays.get(position).getOrder_total_money()==null||monthDays.get(position).getOrder_total_money().equals("null")){
                holder.tv_order_total_money.setText("￥"+"0.00");
            }else {
                holder.tv_order_total_money.setText("￥" +monthDays.get(position).getOrder_total_money().substring(0,monthDays.get(position).getOrder_total_money().length()-1));
            }
            holder.tv_order_total_num.setText(monthDays.get(position).getOrder_total_num());
        }
        return convertView;
    }
    public class Holder{
        TextView tv_order_date;
        TextView tv_week;
        TextView tv_order_total_money;
        TextView tv_order_total_num;
    }
}
