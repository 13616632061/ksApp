package com.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ui.entity.DeskOrder;
import com.ui.ks.R;
import com.ui.util.DateUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */

public class DeskOrderAdapter extends BaseAdapter {
    private Context context;
    private List<DeskOrder> deskOrders;
//    private DisplayImageOptions options;
    private SetOnClick setOnClick;

    public void SetOnClicks(SetOnClick setOnClick){
        this.setOnClick=setOnClick;
    }


    public DeskOrderAdapter(Context context, List<DeskOrder> deskOrders) {
        this.context = context;
        this.deskOrders = deskOrders;
//       options=new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.product)
//                .showImageForEmptyUri(R.drawable.product)
//                .cacheInMemory(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(new RoundedBitmapDisplayer(20))
//                .build();
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
            convertView=View.inflate(context, R.layout.itme_desk,null);
            holder=new Holder();
            holder.tv_number= (TextView) convertView.findViewById(R.id.tv_number);
            holder.tv_notes= (TextView) convertView.findViewById(R.id.tv_notes);
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_confirm= (TextView) convertView.findViewById(R.id.tv_confirm);
            holder.tv_pay_state= (TextView) convertView.findViewById(R.id.tv_pay_state);
            holder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            holder.btn_remove= (Button) convertView.findViewById(R.id.btn_remove);
            holder.btn_complete= (Button) convertView.findViewById(R.id.btn_complete);
            holder.btn_print= (Button) convertView.findViewById(R.id.btn_print);
            holder.btn_edit= (Button) convertView.findViewById(R.id.btn_edit);
            holder.ll_itme= (LinearLayout) convertView.findViewById(R.id.ll_itme);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.tv_number.setText((position+1)+"");
        if (position%2!=0){
            holder.ll_itme.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }else {
            holder.ll_itme.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.tv_notes.setText(deskOrders.get(position).getDesk_num());
        holder.tv_name.setText(deskOrders.get(position).getMemo());
        holder.tv_time.setText(DateUtils.getDateTimeFromMilli(Long.parseLong(deskOrders.get(position).getCreatetime())*1000));
        if (deskOrders.get(position).getPay_status().equals("0")){
            holder.tv_pay_state.setTextColor(Color.parseColor("#ff333333"));
            holder.tv_pay_state.setText("未支付");
            if (deskOrders.get(position).getMenu_confirm().equals("0")){
                holder.tv_confirm.setText("未确定");
            }else {
                holder.tv_confirm.setText("已确定");
            }
            holder.btn_edit.setVisibility(View.VISIBLE);
            holder.btn_complete.setVisibility(View.VISIBLE);
            holder.btn_edit.setVisibility(View.VISIBLE);
            holder.btn_remove.setVisibility(View.VISIBLE);
            holder.btn_edit.setText("查看");
        }else {
            holder.tv_pay_state.setTextColor(Color.parseColor("#ff0000"));
            holder.tv_pay_state.setText("已支付");
            holder.tv_confirm.setText("已确定");
            holder.btn_complete.setVisibility(View.GONE);
            holder.btn_remove.setVisibility(View.GONE);
            holder.btn_edit.setText("查看");
        }

        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClick.ondelete(position);
            }
        });

        holder.btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClick.oncomplete(position);
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deskOrders.get(position).getPay_status().equals("0")) {
                    setOnClick.onEdit(position, true);
                }else {
                    setOnClick.onEdit(position, false);
                }
            }
        });
        holder.btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClick.OnPrint(position);
            }
        });
        //加载图片
//        ImageLoader.getInstance().displayImage(goodsinfoList.get(position).getImageurl(),holder.goods_photo,options);
        return convertView;
    }


    public interface SetOnClick{
        void ondelete(int i);
        void oncomplete(int i);
        void onEdit(int i,boolean isedit);
        void OnPrint(int i);
    }


    public class Holder{
        TextView tv_number;
        TextView tv_notes;
        TextView tv_name;
        TextView tv_confirm;
        TextView tv_pay_state;
        TextView tv_time;
        Button btn_remove;
        Button btn_complete;
        Button btn_print;
        Button btn_edit;
        LinearLayout ll_itme;
    }
}
