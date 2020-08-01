package com.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ui.entity.Out_in_Goods;
import com.ui.ks.R;
import com.library.utils.BigDecimalArith;

import java.util.List;

/**
 * Created by admin on 2018/7/12.
 */

public class Out_In_Adapter extends BaseAdapter {

    private Context context;
    private List<Out_in_Goods> outInGoodsList;
    public Setonclick setonclick;
    public SetLongOnclick setLongOnclick;


    public Out_In_Adapter Setitmeonclcik(Setonclick setonclick){
        this.setonclick=setonclick;
        return Out_In_Adapter.this;
    }

    public Out_In_Adapter SetitmeLongOnclick(SetLongOnclick setLongOnclick){
        this.setLongOnclick=setLongOnclick;
        return Out_In_Adapter.this;
    }

    public Out_In_Adapter(Context context, List<Out_in_Goods> outInGoodsList) {
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
            view= LayoutInflater.from(context).inflate(R.layout.item_out_inactivity,null);
            holder.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            holder.tv_name= (TextView) view.findViewById(R.id.tv_name);
            holder.tv_pricer= (TextView) view.findViewById(R.id.tv_price);
            holder.tv_num= (TextView) view.findViewById(R.id.tv_nums);
            holder.tv_tolat= (TextView) view.findViewById(R.id.tv_total);
            holder.ll_itme= (LinearLayout) view.findViewById(R.id.ll_itme);
            view.setTag(holder);
        }
        holder.tv_xuhao.setText((outInGoodsList.size()-i)+"");
        holder.tv_name.setText(outInGoodsList.get(i).getName());
        holder.tv_pricer.setText(outInGoodsList.get(i).getCost());
        holder.tv_num.setText(outInGoodsList.get(i).getNums());
        holder.tv_tolat.setText(BigDecimalArith.mul(Double.parseDouble(outInGoodsList.get(i).getCost()),Double.parseDouble(outInGoodsList.get(i).getNums()))+"");
        holder.ll_itme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setonclick.Onitmeclick(i);
            }
        });
        holder.ll_itme.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setLongOnclick.LongitmeClick(i);
                return false;
            }
        });


        return view;
    }

    public class Holder{
        LinearLayout ll_itme;
        TextView tv_xuhao;
        TextView tv_name;
        TextView tv_num;
        TextView tv_pricer;
        TextView tv_tolat;
    }

    public interface Setonclick {
        void Onitmeclick(int i);
    }

    public interface SetLongOnclick{
        void LongitmeClick(int i);
    }


}
