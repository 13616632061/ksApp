package com.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ui.db.DBHelper;
import com.ui.entity.Goods_info;
import com.ui.global.Global;
import com.ui.ks.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/6.
 */

public class ShoppingCarAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Goods_info> shopingcarsinfoList;

    public ShoppingCarAdapter(Context context, ArrayList<Goods_info> shopingcarsinfoList) {
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
                addopenorder(shopingcarsinfoList.get(position), finalHolder.tv_count);
                context.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",8));
                context.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",1));
                context.sendBroadcast(new Intent(Global.BROADCAST_OpenOrderActivity_ACTION).putExtra("type",2));
            }
        });
        holder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeopenorder(shopingcarsinfoList.get(position), finalHolder.tv_count);
                context.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",8));
                context.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",3).putExtra("id_position",shopingcarsinfoList.get(position).getGoods_id()));
                context.sendBroadcast(new Intent(Global.BROADCAST_OpenOrderActivity_ACTION).putExtra("type",3));
            }
        });
        return convertView;
    }
    private class  Holder {
        TextView tv_name;
        TextView tv_price;
        TextView tv_count;
        Button iv_add;
        Button iv_remove;
    }
    /**
     *添加
     * @param goods_info
     */
    private void addopenorder(Goods_info goods_info,TextView textView){
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String num_src=textView.getText().toString().trim();
        int num=Integer.parseInt(num_src);
            num++;
            sqlite.execSQL("UPDATE openorder SET num = ? where id="+goods_info.getGoods_id(),
                    new Object[] {num});
        textView.setText(num+"");
        sqlite.close();
    }
    /**
     *减少
     * @param goods_info
     */
    private void removeopenorder(Goods_info goods_info,TextView textView){
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String num_src=textView.getText().toString().trim();
        int num=Integer.parseInt(num_src);
            num--;
            sqlite.execSQL("UPDATE openorder SET num = ? where id="+goods_info.getGoods_id(),
                    new Object[] {num});
        System.out.println("num=="+num);
        if(num>0){
            textView.setText(num+"");
        }else {
            sqlite.execSQL("delete from openorder where id=?", new Object[]{goods_info.getGoods_id()});
        }
        sqlite.close();
    }
}
