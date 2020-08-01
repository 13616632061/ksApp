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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ui.db.DBHelper;
import com.ui.entity.Goods_info;
import com.ui.entity.Goods_info_type;
import com.ui.global.Global;
import com.ui.ks.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/14.
 */

public class OpenOrderGoodListAdapter extends SectionedBaseAdapter  {
    private Context context;
    private ArrayList<Goods_info_type> pruduct_info;
    private DisplayImageOptions options;
    private int cur_position;
    private int type_code;//1订单编辑

    public OpenOrderGoodListAdapter(Context context,  ArrayList<Goods_info_type> pruduct_info,int type_code) {
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
        return pruduct_info.get(section).getProduct_info().size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        cur_position=position;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_goods,null);
            holder=new Holder();
            holder.goods_photo= (ImageView) convertView.findViewById(R.id.goods_photo);
            holder.tv_goods_name= (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_good_stocknum= (TextView) convertView.findViewById(R.id.tv_good_stocknum);
            holder.tv_good_salesnum= (TextView) convertView.findViewById(R.id.tv_good_salesnum);
            holder.tv_good_price= (TextView) convertView.findViewById(R.id.tv_good_price);
            holder.tv_good_stock= (TextView) convertView.findViewById(R.id.tv_good_stock);
            holder.tv_num= (TextView) convertView.findViewById(R.id.tv_num);
            holder.btn_add= (Button) convertView.findViewById(R.id.btn_add);
            holder.btn_cell= (Button) convertView.findViewById(R.id.btn_cell);

            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        if(pruduct_info.get(section).getProduct_info().get(position).getBtn_switch_type()==1){
            holder.tv_good_stocknum.setVisibility(View.GONE);
            holder.tv_good_stock.setVisibility(View.GONE);
        }else {
            holder.tv_good_stocknum.setVisibility(View.VISIBLE);
            holder.tv_good_stock.setVisibility(View.VISIBLE);
        }
        //加载图片
        ImageLoader.getInstance().displayImage(pruduct_info.get(section).getProduct_info().get(position).getImageurl(),holder.goods_photo,options);
        holder.tv_goods_name.setText(pruduct_info.get(section).getProduct_info().get(position).getName());
        holder.tv_good_stocknum.setText(pruduct_info.get(section).getProduct_info().get(position).getStore()+"");
        holder.tv_good_salesnum.setText(pruduct_info.get(section).getProduct_info().get(position).getBuy_count()+"");
        holder.tv_good_price.setText("￥"+pruduct_info.get(section).getProduct_info().get(position).getPrice());

        holder.btn_add.setVisibility(View.VISIBLE);
        holder.tv_good_stock.setVisibility(View.GONE);
        holder.tv_good_stocknum.setVisibility(View.GONE);
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pruduct_info.get(section).getProduct_info().get(position).setIschoose(true);
                int num=pruduct_info.get(section).getProduct_info().get(position).getSelect_num();
                num++;
                pruduct_info.get(section).getProduct_info().get(position).setSelect_num(num);
                int num_src=pruduct_info.get(section).getProduct_info().get(position).getSelect_num();
                holder.tv_num.setText(num_src+"");
                holder.tv_num.setVisibility(View.VISIBLE);
                holder.btn_cell.setVisibility(View.VISIBLE);

                final int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        addopenorder(pruduct_info.get(section).getProduct_info().get(position));
                        context.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",8));
                        context.sendBroadcast(new Intent(Global.BROADCAST_OpenOrderActivity_ACTION).putExtra("type",1).putExtra("startLocation",startLocation));
                    }
                }.start();

            }
        });
        holder.btn_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num=pruduct_info.get(section).getProduct_info().get(position).getSelect_num();
                num--;
                pruduct_info.get(section).getProduct_info().get(position).setSelect_num(num);
                int num_src=pruduct_info.get(section).getProduct_info().get(position).getSelect_num();
                if(num_src>0){
                    holder.tv_num.setText(num_src+"");
                }else {
                    pruduct_info.get(section).getProduct_info().get(position).setIschoose(false);
                    holder.tv_num.setVisibility(View.GONE);
                    holder.btn_cell.setVisibility(View.GONE);
                }
                cellopenorder(pruduct_info.get(section).getProduct_info().get(position));
                context.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",8));
                context.sendBroadcast(new Intent(Global.BROADCAST_OpenOrderActivity_ACTION).putExtra("type",2));
//                context.sendBroadcast(new Intent(Global.BROADCAST_ChooseGoodsFrament_ACTION).putExtra("type",3).putExtra("id_position",pruduct_info.get(section).getProduct_info().get(position).getGoods_id()));
            }
        });
        int num_src1=pruduct_info.get(section).getProduct_info().get(position).getSelect_num();
        if(pruduct_info.get(section).getProduct_info().get(position).ischoose()){
            int num_src=pruduct_info.get(section).getProduct_info().get(position).getSelect_num();
            holder.tv_num.setText(num_src+"");
            holder.tv_num.setVisibility(View.VISIBLE);
            holder.btn_cell.setVisibility(View.VISIBLE);
        }else {
            holder.tv_num.setVisibility(View.GONE);
            holder.btn_cell.setVisibility(View.GONE);
        }
        /**
         * 订单编辑时调用
         */
        if(num_src1>0){
            int num_src=pruduct_info.get(section).getProduct_info().get(position).getSelect_num();
            holder.tv_num.setText(num_src+"");
            holder.tv_num.setVisibility(View.VISIBLE);
            holder.btn_cell.setVisibility(View.VISIBLE);
        }else {
            holder.tv_num.setVisibility(View.GONE);
            holder.btn_cell.setVisibility(View.GONE);
        }
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
        ImageView goods_photo;
        TextView tv_goods_name;
        TextView tv_good_stocknum;
        TextView tv_good_salesnum;
        TextView tv_good_price;
        TextView tv_good_stock;
        Button btn_add;
        Button btn_cell;
        TextView tv_num;

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
