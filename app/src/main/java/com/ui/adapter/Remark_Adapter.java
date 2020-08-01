package com.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ui.entity.Goods_Common_Notes;
import com.ui.ks.R;

import java.util.List;

/**
 * Created by admin on 2018/7/13.
 */

public class Remark_Adapter extends BaseAdapter {

    private Context context;
    private List<Goods_Common_Notes> outInGoodsList;
    public Setonclick setonclick;


    public Remark_Adapter Setitmeonclcik(Setonclick setonclick){
        this.setonclick=setonclick;
        return Remark_Adapter.this;
    }


    public Remark_Adapter(Context context, List<Goods_Common_Notes> outInGoodsList) {
        this.context = context;
        this.outInGoodsList = outInGoodsList;
    }

    @Override
    public int getCount() {
        return outInGoodsList.size();
    }

    @Override
    public Object getItem(int i) {
        return outInGoodsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Holder holder=null;
        if(view!=null) {
            holder= (Holder) view.getTag();
        }else {
            holder=new Holder();
            view= LayoutInflater.from(context).inflate(R.layout.itme_remark,null);
            holder.tv_remark= (TextView) view.findViewById(R.id.tv_remark);
            holder.ll_itme= (LinearLayout) view.findViewById(R.id.ll_itme);
            view.setTag(holder);
        }
        holder.tv_remark.setText(outInGoodsList.get(i).getNotes());

        holder.ll_itme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setonclick.Onitmeclick(i);
            }
        });

        return view;
    }

    public class Holder{
        LinearLayout ll_itme;
        TextView tv_remark;
    }

    public interface Setonclick {
        void Onitmeclick(int i);
    }

}
