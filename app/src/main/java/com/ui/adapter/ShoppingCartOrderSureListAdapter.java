package com.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ui.entity.ShopperCartInfo_item;
import com.ui.ks.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ShoppingCartOrderSureListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ShopperCartInfo_item> ShopperCartInfo_items;
    private DisplayImageOptions options;

    public ShoppingCartOrderSureListAdapter(Context mContext, ArrayList<ShopperCartInfo_item> shopperCartInfo_items) {
        this.mContext = mContext;
        ShopperCartInfo_items = shopperCartInfo_items;
        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.picture_default)
                .showImageForEmptyUri(R.drawable.picture_default)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(300))
                .build();
    }

    @Override
    public int getCount() {
        return ShopperCartInfo_items.size();
    }

    @Override
    public Object getItem(int position) {
        return ShopperCartInfo_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder mholder=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_orderlist,null);
            mholder=new Holder();
            mholder.iv_shoppingpicture= (ImageView) convertView.findViewById(R.id.iv_shoppingpicture);
            mholder.tv_shoppingname= (TextView) convertView.findViewById(R.id.tv_shoppingname);
            mholder.tv_shoppingprice= (TextView) convertView.findViewById(R.id.tv_shoppingprice);
            mholder.tv_nums= (TextView) convertView.findViewById(R.id.tv_nums);
            convertView.setTag(mholder);
        }else {
            mholder= (Holder) convertView.getTag();
        }
        //加载图片
        ImageLoader.getInstance().displayImage(ShopperCartInfo_items.get(position).getImg_src(),mholder.iv_shoppingpicture,options);
        mholder.tv_shoppingname.setText(ShopperCartInfo_items.get(position).getGoods_name());
        mholder.tv_shoppingprice.setText("￥"+ShopperCartInfo_items.get(position).getGoods_price());
        mholder.tv_nums.setText("x "+ShopperCartInfo_items.get(position).getGoods_nums());
        return convertView;
    }
    private class Holder{
        ImageView iv_shoppingpicture;
        TextView tv_shoppingname;
        TextView tv_shoppingprice;
        TextView tv_nums;
    }
}
