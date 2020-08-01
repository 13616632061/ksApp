package com.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ui.db.DBHelper;
import com.ui.entity.Goods_info;
import com.ui.entity.Goods_info_type;
import com.ui.global.Global;
import com.ui.ks.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * 开单页面方块列表适配器
 * Created by Administrator on 2017/4/8.
 */

public class OpenOrderGoodSquareAdapter extends SectionedBaseAdapter {
    private Context context;
    private ArrayList<Goods_info_type> pruduct_info;
    private DisplayImageOptions options;
    private int type_code;//1订单编辑

    public OpenOrderGoodSquareAdapter(Context context,  ArrayList<Goods_info_type> pruduct_info,int type_code) {
        this.context = context;
        this.pruduct_info = pruduct_info;
        this.type_code = type_code;


        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.book)
                .showImageForEmptyUri(R.drawable.book)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();

    }
    @Override
    public Object getItem(int section, int position) {
        return pruduct_info.get(section).getProduct_info().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return pruduct_info.size();
    }

    @Override
    public int getCountForSection(int section) {
      return   (int)(Math.ceil((float)pruduct_info.get(section).getProduct_info().size() / 3));
//        return pruduct_info.get(section).getProduct_info().size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_goods_square,null);
            holder=new Holder();
            holder.tv_name_left= (TextView) convertView.findViewById(R.id.tv_name_left);
            holder.btn_choose_num_left= (Button) convertView.findViewById(R.id.btn_choose_num_left);
            holder.tv_price_left= (TextView) convertView.findViewById(R.id.tv_price_left);
            holder.tv_name_midle= (TextView) convertView.findViewById(R.id.tv_name_midle);
            holder.btn_choose_num_midle= (Button) convertView.findViewById(R.id.btn_choose_num_midle);
            holder.tv_price_midle= (TextView) convertView.findViewById(R.id.tv_price_midle);
            holder.tv_name_right= (TextView) convertView.findViewById(R.id.tv_name_right);
            holder.btn_choose_num_right= (Button) convertView.findViewById(R.id.btn_choose_num_right);
            holder.tv_price_right= (TextView) convertView.findViewById(R.id.tv_price_right);
            holder.layout_item_left= (LinearLayout) convertView.findViewById(R.id.layout_item_left);
            holder.layout_item_midle= (LinearLayout) convertView.findViewById(R.id.layout_item_midle);
            holder.layout_item_right= (LinearLayout) convertView.findViewById(R.id.layout_item_right);

            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        if(3*position<pruduct_info.get(section).getProduct_info().size()){
            holder.layout_item_left.setVisibility(View.VISIBLE);
            holder.tv_name_left.setText(pruduct_info.get(section).getProduct_info().get(3*position).getName());
            holder.tv_price_left.setText("￥"+pruduct_info.get(section).getProduct_info().get(3*position).getPrice());

            int num_src1=pruduct_info.get(section).getProduct_info().get(3*position).getSelect_num();
            if(pruduct_info.get(section).getProduct_info().get(3*position).ischoose()){
                int num_src=pruduct_info.get(section).getProduct_info().get(3*position).getSelect_num();
                holder.btn_choose_num_left.setText(num_src+"");
                holder.btn_choose_num_left.setVisibility(View.VISIBLE);
            }else {
                holder.btn_choose_num_left.setVisibility(View.GONE);
            }
            /**
             * 订单编辑时调用
             */
            if(num_src1>0){
                int num_src=pruduct_info.get(section).getProduct_info().get(3*position).getSelect_num();
                holder.btn_choose_num_midle.setText(num_src+"");
                holder.btn_choose_num_midle.setVisibility(View.VISIBLE);
            }else {
                holder.btn_choose_num_midle.setVisibility(View.GONE);
            }
        }else {
            holder.layout_item_left.setVisibility(View.INVISIBLE);
        }
        if(3*position+1<pruduct_info.get(section).getProduct_info().size()){
            holder.layout_item_midle.setVisibility(View.VISIBLE);
            holder.tv_name_midle.setText(pruduct_info.get(section).getProduct_info().get(3*position+1).getName());
            holder.tv_price_midle.setText("￥"+pruduct_info.get(section).getProduct_info().get(3*position+1).getPrice()+"");

            int num_src2=pruduct_info.get(section).getProduct_info().get(3*position+1).getSelect_num();
            if(pruduct_info.get(section).getProduct_info().get(3*position+1).ischoose()){
                int num_src=pruduct_info.get(section).getProduct_info().get(3*position+1).getSelect_num();
                holder.btn_choose_num_midle.setText(num_src+"");
                holder.btn_choose_num_midle.setVisibility(View.VISIBLE);
            }else {
                holder.btn_choose_num_midle.setVisibility(View.GONE);
            }
            /**
             * 订单编辑时调用
             */
            if(num_src2>0){
                int num_src=pruduct_info.get(section).getProduct_info().get(3*position+1).getSelect_num();
                holder.btn_choose_num_midle.setText(num_src+"");
                holder.btn_choose_num_midle.setVisibility(View.VISIBLE);
            }else {
                holder.btn_choose_num_midle.setVisibility(View.GONE);
            }
        }else {
            holder.layout_item_midle.setVisibility(View.INVISIBLE);
        }
        if(3*position+2<pruduct_info.get(section).getProduct_info().size()){
            holder.layout_item_right.setVisibility(View.VISIBLE);
            holder.tv_name_right.setText(pruduct_info.get(section).getProduct_info().get(3*position+2).getName());
            holder.tv_price_right.setText("￥"+pruduct_info.get(section).getProduct_info().get(3*position+2).getPrice());

            int num_src3=pruduct_info.get(section).getProduct_info().get(3*position+2).getSelect_num();
            if(pruduct_info.get(section).getProduct_info().get(3*position+2).ischoose()){
                int num_src=pruduct_info.get(section).getProduct_info().get(3*position+2).getSelect_num();
                holder.btn_choose_num_right.setText(num_src+"");
                holder.btn_choose_num_right.setVisibility(View.VISIBLE);
            }else {
                holder.btn_choose_num_right.setVisibility(View.GONE);
            }
            /**
             * 订单编辑时调用
             */
            if(num_src3>0){
                int num_src=pruduct_info.get(section).getProduct_info().get(3*position+2).getSelect_num();
                holder.btn_choose_num_right.setText(num_src+"");
                holder.btn_choose_num_right.setVisibility(View.VISIBLE);
            }else {
                holder.btn_choose_num_right.setVisibility(View.GONE);
            }
        }else {
            holder.layout_item_right.setVisibility(View.INVISIBLE);
        }
        holder.layout_item_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pruduct_info.get(section).getProduct_info().get(3*position).setIschoose(true);
                int num=pruduct_info.get(section).getProduct_info().get(3*position).getSelect_num();
                num++;
                pruduct_info.get(section).getProduct_info().get(3*position).setSelect_num(num);
                int num_src=pruduct_info.get(section).getProduct_info().get(3*position).getSelect_num();
                holder.btn_choose_num_left.setText(num_src+"");
                holder.btn_choose_num_left.setVisibility(View.VISIBLE);
                addopenorder(pruduct_info.get(section).getProduct_info().get(3*position));
                context.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",8));
                context.sendBroadcast(new Intent(Global.BROADCAST_OpenOrderActivity_ACTION).putExtra("type",1));
            }
        });
        holder.layout_item_midle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pruduct_info.get(section).getProduct_info().get(3*position+1).setIschoose(true);
                int num=pruduct_info.get(section).getProduct_info().get(3*position+1).getSelect_num();
                num++;
                pruduct_info.get(section).getProduct_info().get(3*position+1).setSelect_num(num);
                int num_src=pruduct_info.get(section).getProduct_info().get(3*position+1).getSelect_num();
                holder.btn_choose_num_midle.setText(num_src+"");
                holder.btn_choose_num_midle.setVisibility(View.VISIBLE);
                addopenorder(pruduct_info.get(section).getProduct_info().get(3*position+1));
                context.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",8));
                context.sendBroadcast(new Intent(Global.BROADCAST_OpenOrderActivity_ACTION).putExtra("type",1));
            }
        });
        holder.layout_item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pruduct_info.get(section).getProduct_info().get(3*position+2).setIschoose(true);
                int num=pruduct_info.get(section).getProduct_info().get(3*position+2).getSelect_num();
                num++;
                pruduct_info.get(section).getProduct_info().get(3*position+2).setSelect_num(num);
                int num_src=pruduct_info.get(section).getProduct_info().get(3*position+2).getSelect_num();
                holder.btn_choose_num_right.setText(num_src+"");
                holder.btn_choose_num_right.setVisibility(View.VISIBLE);
                addopenorder(pruduct_info.get(section).getProduct_info().get(3*position+2));
                context.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",8));
                context.sendBroadcast(new Intent(Global.BROADCAST_OpenOrderActivity_ACTION).putExtra("type",1));
            }
        });
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ((TextView) layout.findViewById(R.id.textItem)).setText(pruduct_info.get(section).getTag_name());
        return layout;
    }

    public class Holder{
        TextView tv_name_left;
        Button btn_choose_num_left;
        TextView tv_price_left;
        TextView tv_name_midle;
        Button btn_choose_num_midle;
        TextView tv_price_midle;
        TextView tv_name_right;
        Button btn_choose_num_right;
        TextView tv_price_right;
        LinearLayout layout_item_left;
        LinearLayout layout_item_midle;
        LinearLayout layout_item_right;

    }
    /**
     *添加
     * @param goods_info
     */
    private void addopenorder(Goods_info goods_info){
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM openorder WHERE id = ?";
        Cursor cursor = sqlite.rawQuery(sql, new String[] { goods_info.getGoods_id()});
        if(cursor.moveToFirst()){
            int num_db=cursor.getInt(cursor.getColumnIndex("num"));
            System.out.println("num_db=="+num_db);
            num_db++;
            sqlite.execSQL("UPDATE openorder SET num = ? where id="+goods_info.getGoods_id(),
                    new Object[] {num_db});
        }else {
            if(type_code==0){
                sqlite.execSQL("insert into openorder (id,tag_id,name,price,num) values(?,?,?,?,?)",
                        new Object[]{goods_info.getGoods_id(),goods_info.getTag_id(),
                                goods_info.getName(),goods_info.getPrice(),1});
            }else if(type_code==1){
                sqlite.execSQL("insert into openorder (id,tag_id,name,price,num) values(?,?,?,?,?)",
                        new Object[]{goods_info.getGoods_id(),goods_info.getTag_id(),
                                goods_info.getName(),goods_info.getPrice(),goods_info.getSelect_num()});
            }
        }
        cursor.close();
        sqlite.close();
    }

    /**
     * 减少
     * @param goods_info
     */
    private void cellopenorder(Goods_info goods_info){
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM openorder WHERE id = ?";
        Cursor cursor = sqlite.rawQuery(sql, new String[] { goods_info.getGoods_id()});
        if(cursor.moveToFirst()){
            int num_db=cursor.getInt(cursor.getColumnIndex("num"));
            if(num_db>1){
                num_db--;
                sqlite.execSQL("UPDATE openorder SET num = ? where id="+goods_info.getGoods_id(),
                        new Object[] {num_db});
            }else {
                sqlite.execSQL("delete from openorder where id=?", new Object[]{goods_info.getGoods_id()});
            }
        }
        cursor.close();
        sqlite.close();
    }
}
