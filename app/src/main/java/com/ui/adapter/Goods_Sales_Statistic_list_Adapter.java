package com.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ui.entity.Goods_Sales;
import com.ui.ks.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/24.
 */

public class Goods_Sales_Statistic_list_Adapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Goods_Sales> goods_sales_list;

    public Goods_Sales_Statistic_list_Adapter(Context mContext, ArrayList<Goods_Sales> goods_sales_list) {
        this.mContext = mContext;
        this.goods_sales_list = goods_sales_list;
    }

    @Override
    public int getCount() {
        return goods_sales_list.size();
    }

    @Override
    public Object getItem(int position) {
        return goods_sales_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.goods_sales_statistics_list_item,null);
            holder=new Holder();
            holder.tv_good_name= (TextView) convertView.findViewById(R.id.tv_good_name);
            holder.tv_good_salesnum= (TextView) convertView.findViewById(R.id.tv_good_salesnum);
            holder.tv_good_salesmoney= (TextView) convertView.findViewById(R.id.tv_good_salesmoney);
            holder.tv_good_salesprofit= (TextView) convertView.findViewById(R.id.tv_good_salesprofit);
            holder.tv_good_salesprofit_num= (TextView) convertView.findViewById(R.id.tv_good_salesprofit_num);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        if(position%2==0){
            convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else {
            convertView.setBackgroundColor(Color.parseColor("#ffedf0f3"));
        }
        holder.tv_good_name.setText(goods_sales_list.get(position).getGoods_name());
        holder.tv_good_salesnum.setText(goods_sales_list.get(position).getGoods_sales_num()+"");
        holder.tv_good_salesmoney.setText(goods_sales_list.get(position).getGoods_sales_money()+"");
        holder.tv_good_salesprofit.setText(goods_sales_list.get(position).getGoods_sales_profit()+"");
        holder.tv_good_salesprofit_num.setText(goods_sales_list.get(position).getGoods_sales_profit_num());

        return convertView;
    }

    private class  Holder{
        TextView tv_good_name;
        TextView tv_good_salesnum;
        TextView tv_good_salesmoney;
        TextView tv_good_salesprofit;
        TextView tv_good_salesprofit_num;

    }
}
