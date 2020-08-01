package com.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ui.entity.Goods_info;
import com.ui.ks.GoodsManagementActivity;
import com.ui.ks.R;
import com.ui.ks.StockWarningActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/14.
 */

public class GoodsListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Goods_info> goodsinfoList;
    private boolean isChecked;
    private boolean isup;
    private DisplayImageOptions options;
    private String checkid="";
    private String checkid_up="";
    GoodsManagementActivity goodsManagementActivity=new GoodsManagementActivity();
    StockWarningActivity stockWarningActivity=new StockWarningActivity ();
    /**
     * 用来保存选中状态和对应的位置，用于解决item的复用问题
     */
    public static Map<Integer, Boolean> isSelected;
    /**
     * 用来保存之前选中状态的位置，用于加载更多数据时恢复已选中的位置
     */
    public static List<Integer> hasSelected = new ArrayList<>();
    public GoodsListAdapter(Context context,  ArrayList<Goods_info> goodsinfoList) {
        this.context = context;
        this.goodsinfoList = goodsinfoList;
       options=new DisplayImageOptions.Builder()
                .cacheInMemory(false) //设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .showImageOnLoading(R.drawable.product)
                .showImageForEmptyUri(R.drawable.product)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    @Override
    public int getCount() {
        return goodsinfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsinfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
   public void isisup(boolean isup){
       this.isup=isup;
       notifyDataSetChanged();
   }
    //下架商品
    public String getCheckid(){
        return checkid;
    }
    //上架商品
    public String getcCheckid_up(){
        return checkid_up;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_goods,null);
            holder=new Holder();
            holder.goods_photo= (ImageView) convertView.findViewById(R.id.goods_photo);
            holder.tv_goods_name= (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_good_stocknum= (TextView) convertView.findViewById(R.id.tv_good_stocknum);
            holder.tv_good_salesnum= (TextView) convertView.findViewById(R.id.tv_good_salesnum);
            holder.tv_good_price= (TextView) convertView.findViewById(R.id.tv_good_price);
            holder.tv_good_stock= (TextView) convertView.findViewById(R.id.tv_good_stock);
            holder.tv_good_sales= (TextView) convertView.findViewById(R.id.tv_good_sales);
            holder.checkbox_good= (CheckBox) convertView.findViewById(R.id.checkbox_good);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        if(isup){
            holder.checkbox_good.setVisibility(View.VISIBLE);
        }else {
            holder.checkbox_good.setVisibility(View.GONE);
            checkid=goodsManagementActivity.checkid(goodsinfoList.get(position).getGoods_id(),false);
            goodsinfoList.get(position).setIschoose(false);
        }
        holder.checkbox_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkbox_good.isChecked()){
                    isChecked=true;
                    checkid=goodsManagementActivity.checkid(goodsinfoList.get(position).getGoods_id(),isChecked);
                    checkid_up=stockWarningActivity.checkid(goodsinfoList.get(position).getGoods_id(),isChecked);
                    goodsinfoList.get(position).setIschoose(isChecked);
                }else {
                    isChecked=false;
                    checkid=goodsManagementActivity.checkid(goodsinfoList.get(position).getGoods_id(),isChecked);
                    checkid_up=stockWarningActivity.checkid(goodsinfoList.get(position).getGoods_id(),isChecked);
                    goodsinfoList.get(position).setIschoose(isChecked);
                }
            }
        });
        holder.checkbox_good.setChecked(goodsinfoList.get(position).ischoose());


        if(goodsinfoList.get(position).getBtn_switch_type()==1){
            holder.tv_good_stocknum.setVisibility(View.GONE);
            holder.tv_good_stock.setVisibility(View.GONE);
        }else {
            holder.tv_good_stocknum.setVisibility(View.VISIBLE);
            holder.tv_good_stock.setVisibility(View.VISIBLE);
        }
        //加载图片
        ImageLoader.getInstance().displayImage(goodsinfoList.get(position).getImageurl(),holder.goods_photo,options);
        holder.tv_goods_name.setText(goodsinfoList.get(position).getName());
        holder.tv_good_stocknum.setText(goodsinfoList.get(position).getStore()+"");
        holder.tv_good_salesnum.setText(goodsinfoList.get(position).getBuy_count()+"");
        holder.tv_good_price.setText("￥"+goodsinfoList.get(position).getPrice());


        return convertView;
    }

    public class Holder{
        ImageView goods_photo;
        TextView tv_goods_name;
        TextView tv_good_stocknum;
        TextView tv_good_salesnum;
        TextView tv_good_price;
        TextView tv_good_stock;
        TextView tv_good_sales;
        CheckBox checkbox_good;

    }
}
