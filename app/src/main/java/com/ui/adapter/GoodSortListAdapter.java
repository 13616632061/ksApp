package com.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ui.entity.GoodSort;
import com.ui.ks.R;

import java.util.ArrayList;

/**
 * 商品管理页面，分类列表适配器
 * Created by Administrator on 2017/3/11.
 */

public class GoodSortListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<GoodSort> goodsortList;
    private int cur_pos;

    public GoodSortListAdapter(Context context, ArrayList<GoodSort> goodsortList) {
        this.context = context;
        this.goodsortList = goodsortList;
    }


    @Override
    public int getCount() {
        return goodsortList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsortList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     适配器中添加这个方法
     */
    public void setDefSelect(int position) {
        this.cur_pos = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.goodsort_item_layout,null);
            holder=new Holder();
            holder.tv_goodsortname= (TextView) convertView.findViewById(R.id.tv_goodsortname);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        if(goodsortList.size()>0){
            holder.tv_goodsortname.setText(goodsortList.get(position).getName());
            if(position==cur_pos){
                convertView.setBackgroundResource(R.color.white);
                holder.tv_goodsortname.setTextColor(Color.parseColor("#ffff8905"));
            }else {
                convertView.setBackgroundResource(R.drawable.select_click_grag);
                holder.tv_goodsortname.setTextColor(Color.BLACK);
            }
        }
        return convertView;
    }
    private class Holder{
        TextView tv_goodsortname;
    }
}
