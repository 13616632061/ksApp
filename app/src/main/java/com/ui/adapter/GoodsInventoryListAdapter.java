package com.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ui.entity.Goods_Inventory;
import com.ui.ks.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/3/2.
 */

public class GoodsInventoryListAdapter extends BaseAdapter {

    List<Goods_Inventory> adtas=new ArrayList<>();
    public Context context;

    public GoodsInventoryListAdapter(Context context,List<Goods_Inventory> adtas) {
        this.context = context;
        this.adtas=adtas;
        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.product)
                .showImageForEmptyUri(R.drawable.product)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    private DisplayImageOptions options;

    @Override
    public int getCount() {
        return adtas.size();
    }

    @Override
    public Object getItem(int i) {
        return adtas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Holder holder;
        if(convertView==null) {
            convertView = View.inflate(context, R.layout.item_inventory_goods, null);
            holder=new Holder();
            holder.goods_photo= (ImageView) convertView.findViewById(R.id.goods_photo);
            holder.tv_goods_name= (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_good_code= (TextView) convertView.findViewById(R.id.tv_good_code);
            holder.tv_stock= (TextView) convertView.findViewById(R.id.tv_stock);
            holder.tv_arrow= (TextView) convertView.findViewById(R.id.tv_arrow);
            holder.tv_old_stock= (TextView) convertView.findViewById(R.id.tv_old_stock);
            holder.image_state= (ImageView) convertView.findViewById(R.id.image_state);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        //加载图片
        ImageLoader.getInstance().displayImage(adtas.get(position).getImg_src(),holder.goods_photo,options);
        holder.tv_goods_name.setText(adtas.get(position).getName());
        holder.tv_good_code.setText(adtas.get(position).getBncode());
        holder.tv_stock.setText(adtas.get(position).getStore());
        if (adtas.get(position).getReality_store().equals("0")){
            holder.tv_arrow.setVisibility(View.GONE);
            holder.tv_old_stock.setVisibility(View.GONE);
            holder.tv_old_stock.setText(adtas.get(position).getReality_store());
            holder.image_state.setImageResource(R.drawable.ic_action_3);
        }else {
            holder.tv_arrow.setVisibility(View.VISIBLE);
            holder.tv_old_stock.setVisibility(View.VISIBLE);
            holder.tv_old_stock.setText(adtas.get(position).getReality_store());
//            holder.image_state.setText(adats.get(i).getStore()+"/"+liststate.get(i));
            if (Float.parseFloat(adtas.get(position).getStore())==Float.parseFloat(adtas.get(position).getReality_store())){
                holder.image_state.setImageResource(R.drawable.ic_action_app);
            }else {
                holder.image_state.setImageResource(R.drawable.ic_action_2);
            }
        }

        return convertView;
    }


    public class Holder{
        ImageView goods_photo;
        TextView tv_goods_name;
        TextView tv_good_code;
        TextView tv_stock;
        TextView tv_arrow;
        TextView tv_old_stock;
        ImageView image_state;
    }

}
