package com.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ui.entity.NearlyShopper;
import com.ui.ks.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/8.
 */

public class HomePageFragmentNearlyShopperAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NearlyShopper> nearlyShoppers_list;
    private DisplayImageOptions options;

    public HomePageFragmentNearlyShopperAdapter(Context context, ArrayList<NearlyShopper> nearlyShoppers_list) {
        this.context = context;
        this.nearlyShoppers_list = nearlyShoppers_list;
        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.picture_default)
                .showImageForEmptyUri(R.drawable.picture_default)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))//设置图片圆角
                .build();
    }

    @Override
    public int getCount() {
        return nearlyShoppers_list.size();
    }

    @Override
    public Object getItem(int position) {
        return nearlyShoppers_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Horleder horleder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.homepagefragmentnearlyshopper_item,null);
            horleder=new Horleder();
            horleder.iv_shopper= (ImageView) convertView.findViewById(R.id.iv_shopper);
            horleder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            horleder.tv_distance= (TextView) convertView.findViewById(R.id.tv_distance);
            horleder.tv_notes= (TextView) convertView.findViewById(R.id.tv_notes);
            convertView.setTag(horleder);
        }else {
            horleder= (Horleder) convertView.getTag();
        }
        if(nearlyShoppers_list.get(position).getShopperdistance().contains("￥")){
            horleder.tv_notes.setVisibility(View.GONE);
            horleder.tv_notes.setText(nearlyShoppers_list.get(position).getShoppernotes());
            horleder.tv_distance.setText(nearlyShoppers_list.get(position).getShopperdistance());
            horleder.tv_distance.setTextColor(Color.parseColor("#ffff8905"));
            horleder.tv_name.setText(nearlyShoppers_list.get(position).getShoppername());
            ImageLoader.getInstance().displayImage(nearlyShoppers_list.get(position).getShopperphoto(), horleder.iv_shopper, options);
        }else {
            horleder.tv_notes.setVisibility(View.VISIBLE);
            horleder.tv_notes.setText(nearlyShoppers_list.get(position).getShoppernotes());
            horleder.tv_distance.setText(nearlyShoppers_list.get(position).getShopperdistance()+"m");
            horleder.tv_distance.setTextColor(Color.parseColor("#adadad"));
            horleder.tv_name.setText(nearlyShoppers_list.get(position).getShoppername());
            ImageLoader.getInstance().displayImage(nearlyShoppers_list.get(position).getShopperphoto(), horleder.iv_shopper, options);
        }
        return convertView;
    }
    private class Horleder{
        ImageView iv_shopper;
        TextView tv_name;
        TextView tv_distance;
        TextView tv_notes;

    }

}
