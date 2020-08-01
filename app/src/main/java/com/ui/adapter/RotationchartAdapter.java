package com.ui.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ui.entity.Carousel;
import com.ui.ks.CodescanningActivity;
import com.ui.ks.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by Administrator on 2016/12/24.
 */

public class RotationchartAdapter extends BaseAdapter{

    private List<Carousel> cat_list;
    private CodescanningActivity Activity;
    private DisplayImageOptions options;

    public RotationchartAdapter(CodescanningActivity Activity, List<Carousel> cat_list) {
        this.Activity = Activity;
        this.cat_list = cat_list;
        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.product)
                .showImageForEmptyUri(R.drawable.product)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    @Override
    public int getCount() {
        return cat_list.size();
    }

    @Override
    public Object getItem(int position) {
        return cat_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(Activity, R.layout.itme_rotationchart,null);
            holder=new Holder();
            convertView.setTag(holder);
            holder.image= (ImageView) convertView.findViewById(R.id.image);
        }else {
            holder= (Holder) convertView.getTag();
        }
        //加载图片
        ImageLoader.getInstance().displayImage(cat_list.get(position).getUrl(),holder.image,options);
        return convertView;
    }
    public class Holder{
        ImageView image;//会员余额
    }

}
