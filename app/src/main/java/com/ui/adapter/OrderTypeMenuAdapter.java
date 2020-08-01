package com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ui.ks.R;


/**
 * 订单类型选择适配器
 * Created by Administrator on 2016/12/26.
 */

public class OrderTypeMenuAdapter extends BaseAdapter{
    private Context mContext;
    private String[] datastr;
    private boolean showCheck;
    private int SelectIndex;

    public OrderTypeMenuAdapter(Context mContext,String[] datastr) {
        this.datastr = datastr;
        this.mContext = mContext;
    }
    public void setSelectIndex(int selectIndex) {
        SelectIndex = selectIndex;
    }


    public void setShowCheck(boolean showCheck) {
        this.showCheck = showCheck;
    }
    @Override
    public int getCount() {
        return datastr.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if (convertView==null){
            convertView=View.inflate(mContext, R.layout.item_ordertype_menu,null);
            holder=new Holder();
            holder.tv_ordertypemenu= (TextView) convertView.findViewById(R.id.tv_ordertypemenu);
            holder.iv_ordertypemenu= (ImageView) convertView.findViewById(R.id.iv_ordertypemenu);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.tv_ordertypemenu.setText(datastr[position]);
        final View finalConvertView = convertView;
        final Holder finalHolder = holder;
        if(SelectIndex==position&&showCheck) {
            finalConvertView.setSelected(true);
            finalConvertView.setPressed(true);
            finalHolder.iv_ordertypemenu.setVisibility(View.VISIBLE);
        }else {
            finalConvertView.setSelected(false);
            finalConvertView.setPressed(false);
            finalHolder.iv_ordertypemenu.setVisibility(View.GONE);
        }

        return convertView;
    }
    public  class Holder{
        TextView tv_ordertypemenu;
        ImageView iv_ordertypemenu;
    }
}
