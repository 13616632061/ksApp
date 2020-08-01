package com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.ui.entity.DeskTableOrder;
import com.ui.ks.R;
import com.ui.util.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */

public class TableorderAdapter extends BaseAdapter {
    private Context context;
    private List<DeskTableOrder> deskOrders;
    private SetOnClick setOnClick;
    private boolean isedit=true;


    public void setIsedit(boolean isedit) {
        this.isedit = isedit;
    }

    public void SetOnClicks(SetOnClick setOnClick){
        this.setOnClick=setOnClick;
    }


    public TableorderAdapter(Context context, List<DeskTableOrder> deskOrders) {
        this.context = context;
        this.deskOrders = deskOrders;
    }

    @Override
    public int getCount() {
        return deskOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return deskOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.itme_desk_goods,null);
            holder=new Holder();
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            holder.ed_nums= (EditText) convertView.findViewById(R.id.ed_nums);
            holder.tv_total= (TextView) convertView.findViewById(R.id.tv_total);
            holder.tv_delete= (TextView) convertView.findViewById(R.id.tv_delete);
            holder.btn_determine= (TextView) convertView.findViewById(R.id.btn_determine);
            holder.tv_remark= (TextView) convertView.findViewById(R.id.tv_remark);

            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }


        if (isedit){
            holder.tv_delete.setVisibility(View.VISIBLE);
            holder.btn_determine.setVisibility(View.VISIBLE);
        }else {
            holder.tv_delete.setVisibility(View.GONE);
            holder.btn_determine.setVisibility(View.GONE);
        }

        holder.tv_name.setText(deskOrders.get(position).getName());
        holder.ed_nums.setText(deskOrders.get(position).getQuantity());
        holder.tv_total.setText(StringUtils.round_down(deskOrders.get(position).getPrice(),2));
        holder.tv_remark.setText(deskOrders.get(position).getMenu_memo());
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClick.ondelete(position);
            }
        });
        holder.btn_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClick.onEdit(position,holder.ed_nums);
            }
        });
        return convertView;
    }


    public interface SetOnClick{
        void ondelete(int i);
        void onEdit(int i,EditText editText);
    }


    public class Holder{
        TextView tv_name;
        EditText ed_nums;
        TextView tv_total;
        TextView tv_delete;
        TextView btn_determine;
        TextView tv_remark;
    }
}
