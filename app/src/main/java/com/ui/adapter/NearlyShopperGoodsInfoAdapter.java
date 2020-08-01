package com.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ui.entity.Goods_info_type;
import com.ui.global.Global;
import com.ui.ks.R;
import com.ui.util.DialogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/8.
 */

public class NearlyShopperGoodsInfoAdapter extends SectionedBaseAdapter {
    private Activity context;
    private ArrayList<Goods_info_type> pruduct_info;
    private DisplayImageOptions options;
    private AlertDialog mAlertDialog;
    private InputMethodManager imm;

    public NearlyShopperGoodsInfoAdapter(Activity context, ArrayList<Goods_info_type> pruduct_info) {
        this.context = context;
        this.pruduct_info = pruduct_info;
        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.picture_default)
                .showImageForEmptyUri(R.drawable.picture_default)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    @Override
    public Object getItem(int section, int position) {
        return pruduct_info.get(section).getProduct_info().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return pruduct_info.size();
    }

    @Override
    public int getCountForSection(int section) {
        return pruduct_info.get(section).getProduct_info().size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_goods,null);
            holder=new Holder();
            holder.goods_photo= (ImageView) convertView.findViewById(R.id.goods_photo);
            holder.tv_goods_name= (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_good_stocknum= (TextView) convertView.findViewById(R.id.tv_good_stocknum);
            holder.tv_good_salesnum= (TextView) convertView.findViewById(R.id.tv_good_salesnum);
            holder.tv_good_price= (TextView) convertView.findViewById(R.id.tv_good_price);
            holder.tv_good_stock= (TextView) convertView.findViewById(R.id.tv_good_stock);
            holder.et_num= (EditText) convertView.findViewById(R.id.et_num);
            holder.btn_add= (Button) convertView.findViewById(R.id.btn_add);
            holder.btn_cell= (Button) convertView.findViewById(R.id.btn_cell_et);

            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.et_num.setInputType(InputType.TYPE_NULL);
        holder.et_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEdit_numDialog(holder.et_num,section,position);
            }
        });
        if(pruduct_info.get(section).getProduct_info().get(position).getBtn_switch_type()==1){
            holder.tv_good_stocknum.setVisibility(View.GONE);
            holder.tv_good_stock.setVisibility(View.GONE);
        }else {
            holder.tv_good_stocknum.setVisibility(View.VISIBLE);
            holder.tv_good_stock.setVisibility(View.VISIBLE);
        }
        //加载图片
        ImageLoader.getInstance().displayImage(pruduct_info.get(section).getProduct_info().get(position).getImageurl(),holder.goods_photo,options);
        holder.tv_goods_name.setText(pruduct_info.get(section).getProduct_info().get(position).getName());
        holder.tv_good_stocknum.setText(pruduct_info.get(section).getProduct_info().get(position).getStore()+"");
        holder.tv_good_salesnum.setText(pruduct_info.get(section).getProduct_info().get(position).getBuy_count()+"");
        holder.tv_good_price.setText("￥"+pruduct_info.get(section).getProduct_info().get(position).getPrice());

        holder.btn_add.setVisibility(View.VISIBLE);
        holder.tv_good_stock.setVisibility(View.GONE);
        holder.tv_good_stocknum.setVisibility(View.GONE);

        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pruduct_info.get(section).getProduct_info().get(position).setIschoose(true);
                int num=pruduct_info.get(section).getProduct_info().get(position).getSelect_num();
                num++;
                pruduct_info.get(section).getProduct_info().get(position).setSelect_num(num);
                int num_src=pruduct_info.get(section).getProduct_info().get(position).getSelect_num();
                holder.et_num.setText(num_src+"");
                holder.et_num.setVisibility(View.VISIBLE);
                holder.btn_cell.setVisibility(View.VISIBLE);

                final int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                context.sendBroadcast(new Intent(Global.BROADCAST_NearlyShopperGoodsActivity_ACTION).putExtra("type",1).putExtra("startLocation",startLocation));

            }
        });
        holder.btn_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num=pruduct_info.get(section).getProduct_info().get(position).getSelect_num();
                num--;
                pruduct_info.get(section).getProduct_info().get(position).setSelect_num(num);
                int num_src=pruduct_info.get(section).getProduct_info().get(position).getSelect_num();
                if(num_src>0){
                    holder.et_num.setText(num_src+"");
                }else {
                    pruduct_info.get(section).getProduct_info().get(position).setIschoose(false);
                    holder.et_num.setVisibility(View.GONE);
                    holder.btn_cell.setVisibility(View.GONE);
                }
                context.sendBroadcast(new Intent(Global.BROADCAST_NearlyShopperGoodsActivity_ACTION).putExtra("type",2));
            }
        });

        if(pruduct_info.get(section).getProduct_info().get(position).ischoose()){
            int num_src=pruduct_info.get(section).getProduct_info().get(position).getSelect_num();
            holder.et_num.setText(num_src+"");
            holder.et_num.setVisibility(View.VISIBLE);
            holder.btn_cell.setVisibility(View.VISIBLE);
        }else {
            holder.et_num.setVisibility(View.GONE);
            holder.btn_cell.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ((TextView) layout.findViewById(R.id.textItem)).setText(pruduct_info.get(section).getTag_name());
        return layout;
    }
    private class Holder{
        ImageView goods_photo;
        TextView tv_goods_name;
        TextView tv_good_stocknum;
        TextView tv_good_salesnum;
        TextView tv_good_price;
        TextView tv_good_stock;
        Button btn_add;
        Button btn_cell;
        EditText et_num;

    }
    private void setEdit_numDialog(final EditText edittext, final int section, final int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view=View.inflate(context,R.layout.dialog_add_shopping_num,null);
        Button btn_add= (Button) view.findViewById(R.id.btn_add);
        Button btn_cell= (Button) view.findViewById(R.id.btn_cell);
        final EditText et_num= (EditText) view.findViewById(R.id.et_num);
        TextView tv_cell= (TextView ) view.findViewById(R.id.tv_cell);
        TextView tv_sure= (TextView ) view.findViewById(R.id.tv_sure);
        et_num.setText(edittext.getText().toString().trim());
        et_num.setSelection(et_num.getText().toString().length());
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num_src=et_num.getText().toString().toString().trim();
                int num=Integer.parseInt(num_src);
                num++;
                et_num.setText(num+"");
                et_num.setSelection(et_num.getText().toString().length());
            }
        });
        btn_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num_src=et_num.getText().toString().toString().trim();
                int num=Integer.parseInt(num_src);
                num--;
                et_num.setText(num+"");
            }
        });
        //软键盘显示
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        tv_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                if(DialogUtils.isSoftShowing(context)){
                    //再次调用软键盘消失
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num_src=et_num.getText().toString().toString().trim();
                int num=Integer.parseInt(num_src);
                pruduct_info.get(section).getProduct_info().get(position).setSelect_num(num);
                int num_sr=pruduct_info.get(section).getProduct_info().get(position).getSelect_num();
                edittext.setText(num_sr+"");
                context.sendBroadcast(new Intent(Global.BROADCAST_NearlyShopperGoodsActivity_ACTION).putExtra("type",2));
                mAlertDialog.dismiss();
                if(DialogUtils.isSoftShowing(context)){
                    //再次调用软键盘消失
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        mAlertDialog=builder.setView(view).show();
        mAlertDialog.setCancelable(false);
        Window dialogWindow = mAlertDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.TOP);
        lp.y = 200; // 新位置Y坐标
        dialogWindow.setAttributes(lp);
        mAlertDialog.show();
    }
}
