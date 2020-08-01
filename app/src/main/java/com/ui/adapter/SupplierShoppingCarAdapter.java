package com.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ui.entity.Goods_info;
import com.ui.global.Global;
import com.ui.ks.R;

import java.util.ArrayList;

/**
 * 批发商适配器
 * Created by Administrator on 2017/6/9.
 */

public class SupplierShoppingCarAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Goods_info> shopingcarsinfoList;

    public SupplierShoppingCarAdapter(Context context, ArrayList<Goods_info> shopingcarsinfoList) {
        this.context = context;
        this.shopingcarsinfoList = shopingcarsinfoList;
    }

    @Override
    public int getCount() {
        return shopingcarsinfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return shopingcarsinfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.product_item,null);
            holder=new Holder();
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_price= (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_count= (TextView) convertView.findViewById(R.id.tv_count);
            holder.iv_add= (Button) convertView.findViewById(R.id.iv_add);
            holder.iv_remove= (Button) convertView.findViewById(R.id.iv_remove);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.tv_name.setText(shopingcarsinfoList.get(position).getName());
        holder.tv_price.setText("￥ "+shopingcarsinfoList.get(position).getPrice());
        holder.tv_count.setText(shopingcarsinfoList.get(position).getSelect_num()+"");

        final Holder finalHolder = holder;
        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int select_num= shopingcarsinfoList.get(position).getSelect_num();
                select_num++;
                shopingcarsinfoList.get(position).setSelect_num(select_num);
                select_num=shopingcarsinfoList.get(position).getSelect_num();
                finalHolder.tv_count.setText(select_num+"");
                context.sendBroadcast(new Intent(Global.BROADCAST_NearlyShopperGoodsActivity_ACTION).putExtra("type",3));
            }
        });
        holder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int select_num= shopingcarsinfoList.get(position).getSelect_num();
                select_num--;
                shopingcarsinfoList.get(position).setSelect_num(select_num);
                select_num=shopingcarsinfoList.get(position).getSelect_num();
                if(select_num>0){
                    finalHolder.tv_count.setText(shopingcarsinfoList.get(position).getSelect_num()+"");
                }else {
                    shopingcarsinfoList.get(position).setIschoose(false);
                    shopingcarsinfoList.remove(position);
                    notifyDataSetChanged();
                }
                context.sendBroadcast(new Intent(Global.BROADCAST_NearlyShopperGoodsActivity_ACTION).putExtra("type",3));
            }
        });
        if(!shopingcarsinfoList.get(position).ischoose()){
            shopingcarsinfoList.remove(position);
            notifyDataSetChanged();
        }
        return convertView;
    }
    private class  Holder {
        TextView tv_name;
        TextView tv_price;
        TextView tv_count;
        Button iv_add;
        Button iv_remove;
    }
}
