package com.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ui.entity.GoodSort;
import com.ui.ks.R;

import java.util.ArrayList;

/**
 * 开单页面选择商品分类适配器
 * Created by Administrator on 2017/3/30.
 */

public class ChooseGoodsSortListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GoodSort> goodsortList;
    private int cur_pos;

    public ChooseGoodsSortListAdapter(Context context, ArrayList<GoodSort> goodsortList) {
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
            holder.btn_shopping_num= (Button) convertView.findViewById(R.id.btn_shopping_num);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        if(goodsortList.size()>0){
            if(goodsortList.get(position).getChoose_num()>0){
                holder.btn_shopping_num.setVisibility(View.VISIBLE);
                holder.btn_shopping_num.setText(goodsortList.get(position).getChoose_num()+"");
            }else {
                holder.btn_shopping_num.setVisibility(View.GONE);
            }
            holder.tv_goodsortname.setText(goodsortList.get(position).getName());
            if(position==cur_pos){
                convertView.setBackgroundResource(R.color.white);
                holder.tv_goodsortname.setTextColor(Color.parseColor("#ffff8905"));
            }else {
                convertView.setBackgroundResource(R.drawable.select_click_grag);
                holder.tv_goodsortname.setTextColor(Color.parseColor("#adadad"));
            }
        }
        return convertView;
    }
    private class Holder{
        TextView tv_goodsortname;
        Button btn_shopping_num;
    }
}
