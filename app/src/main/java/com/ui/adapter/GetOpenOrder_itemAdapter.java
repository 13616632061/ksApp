package com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ui.entity.GetOpenOrder_info;
import com.ui.ks.R;

import java.util.ArrayList;

/**
 * 取单列表展开数据适配器
 * Created by Administrator on 2017/4/13.
 */

public class GetOpenOrder_itemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GetOpenOrder_info> getOpenOrder_infos;

    public GetOpenOrder_itemAdapter(Context context, ArrayList<GetOpenOrder_info> getOpenOrder_infos) {
        this.context = context;
        this.getOpenOrder_infos = getOpenOrder_infos;
    }

    @Override
    public int getCount() {
        return getOpenOrder_infos.size();
    }

    @Override
    public Object getItem(int position) {
        return getOpenOrder_infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.getopenorder_item_item,null);
            holder=new Holder();
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_num= (TextView) convertView.findViewById(R.id.tv_num);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
            holder.tv_name.setText(getOpenOrder_infos.get(position).getName());
            holder.tv_num.setText(getOpenOrder_infos.get(position).getNum()+"");
        return convertView;
    }

    private class Holder{
        private TextView tv_name;
        private TextView tv_num;
    }
}
