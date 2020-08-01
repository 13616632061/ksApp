package com.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ui.entity.ShopperSortInfo;
import com.ui.ks.R;
import com.ui.ks.ShoppingSortNearlyShopperActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/15.
 */

public class MoreShoppingSortAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ShopperSortInfo> shopperSortInfos;
    private DisplayImageOptions options;

    public MoreShoppingSortAdapter(Context context, ArrayList<ShopperSortInfo> shopperSortInfos) {
        this.context = context;
        this.shopperSortInfos = shopperSortInfos;
        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo_default)
                .showImageForEmptyUri(R.drawable.logo_default)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))//设置图片圆角
                .build();
    }

    @Override
    public int getCount() {
        return (int)(Math.ceil((float)shopperSortInfos.size() / 4));
    }

    @Override
    public Object getItem(int position) {
        return shopperSortInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderView holderView=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_moreshoppingsort,null);
            holderView=new HolderView();
            holderView.layout1= (RelativeLayout) convertView.findViewById(R.id.layout1);
            holderView.layout2= (RelativeLayout) convertView.findViewById(R.id.layout2);
            holderView.layout3= (RelativeLayout) convertView.findViewById(R.id.layout3);
            holderView.layout4= (RelativeLayout) convertView.findViewById(R.id.layout4);
            holderView.imageView1= (ImageView) convertView.findViewById(R.id.imageView1);
            holderView.imageView2= (ImageView) convertView.findViewById(R.id.imageView2);
            holderView.imageView3= (ImageView) convertView.findViewById(R.id.imageView3);
            holderView.imageView4= (ImageView) convertView.findViewById(R.id.imageView4);
            holderView.textView1= (TextView) convertView.findViewById(R.id.textView1);
            holderView.textView2= (TextView) convertView.findViewById(R.id.textView2);
            holderView.textView3= (TextView) convertView.findViewById(R.id.textView3);
            holderView.textView4= (TextView) convertView.findViewById(R.id.textView4);
            convertView.setTag(holderView);
        }else {
            holderView= (HolderView) convertView.getTag();
        }
        if(shopperSortInfos.size()>4*position){
            holderView.layout1.setVisibility(View.VISIBLE);
            holderView.textView1.setText(shopperSortInfos.get(4*position).getCat_name());
            ImageLoader.getInstance().displayImage(shopperSortInfos.get(4*position).getCat_icon(), holderView.imageView1, options);
        }else {
            holderView.layout1.setVisibility(View.INVISIBLE);
        }
        if(shopperSortInfos.size()>4*position+1){
            holderView.layout2.setVisibility(View.VISIBLE);
            holderView.textView2.setText(shopperSortInfos.get(4*position+1).getCat_name());
            ImageLoader.getInstance().displayImage(shopperSortInfos.get(4*position+1).getCat_icon(), holderView.imageView2, options);
        }else {
            holderView.layout2.setVisibility(View.INVISIBLE);
        }
        if(shopperSortInfos.size()>4*position+2){
            holderView.layout3.setVisibility(View.VISIBLE);
            holderView.textView3.setText(shopperSortInfos.get(4*position+2).getCat_name());
            ImageLoader.getInstance().displayImage(shopperSortInfos.get(4*position+2).getCat_icon(), holderView.imageView3, options);
        }else {
            holderView.layout3.setVisibility(View.INVISIBLE);
        }
        if(shopperSortInfos.size()>4*position+3){
            holderView.layout4.setVisibility(View.VISIBLE);
            holderView.textView4.setText(shopperSortInfos.get(4*position+3).getCat_name());
            ImageLoader.getInstance().displayImage(shopperSortInfos.get(4*position+3).getCat_icon(), holderView.imageView3, options);
        }else {
            holderView.layout4.setVisibility(View.INVISIBLE);
        }
        holderView.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ShoppingSortNearlyShopperActivity.class);
                intent.putExtra("cat_id",shopperSortInfos.get(4*position).getCat_id());
                intent.putExtra("cat_name",shopperSortInfos.get(4*position).getCat_name());
                context.startActivity(intent);
            }
        });
        holderView.layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ShoppingSortNearlyShopperActivity.class);
                intent.putExtra("cat_id",shopperSortInfos.get(4*position+1).getCat_id());
                intent.putExtra("cat_name",shopperSortInfos.get(4*position+1).getCat_name());
                context.startActivity(intent);
            }
        });
        holderView.layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ShoppingSortNearlyShopperActivity.class);
                intent.putExtra("cat_id",shopperSortInfos.get(4*position+2).getCat_id());
                intent.putExtra("cat_name",shopperSortInfos.get(4*position+2).getCat_name());
                context.startActivity(intent);
            }
        });
        holderView.layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ShoppingSortNearlyShopperActivity.class);
                intent.putExtra("cat_id",shopperSortInfos.get(4*position+3).getCat_id());
                intent.putExtra("cat_name",shopperSortInfos.get(4*position+3).getCat_name());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    private class HolderView{
        RelativeLayout layout1;
        ImageView imageView1;
        TextView textView1;
        RelativeLayout layout2;
        ImageView imageView2;
        TextView textView2;
        RelativeLayout layout3;
        ImageView imageView3;
        TextView textView3;
        RelativeLayout layout4;
        ImageView imageView4;
        TextView textView4;
    }
}
