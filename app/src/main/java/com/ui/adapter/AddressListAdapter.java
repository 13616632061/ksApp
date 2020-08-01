package com.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ui.entity.AddressList;
import com.ui.global.Global;
import com.ui.ks.AddAdressActivity;
import com.ui.ks.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/29.
 */

public class AddressListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<AddressList> mAddressList;
    private AlertDialog malertDialog=null;

    public AddressListAdapter(Context mContext, ArrayList<AddressList> mAddressList) {
        this.mContext = mContext;
        this.mAddressList = mAddressList;
    }

    @Override
    public int getCount() {
        return mAddressList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAddressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_getaddresslist,null);
            holder=new Holder();
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_phone= (TextView) convertView.findViewById(R.id.tv_phone);
            holder.tv_adress= (TextView) convertView.findViewById(R.id.tv_adress);
            holder.layout_adress= (RelativeLayout) convertView.findViewById(R.id.layout_adress);
            holder.layout_remove= (RelativeLayout) convertView.findViewById(R.id.layout_remove);
            holder.layout_edit= (RelativeLayout) convertView.findViewById(R.id.layout_edit);
            holder.check= (CheckBox) convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.tv_name.setText("收货人："+mAddressList.get(position).getName());
        holder.tv_phone.setText(mAddressList.get(position).getMobile());
        holder.tv_adress.setText("收货地址："+mAddressList.get(position).getArea()+mAddressList.get(position).getAddr());
        if("1".equals(mAddressList.get(position).getAcquiesce())){
            holder.check.setChecked(true);
        }else {
            holder.check.setChecked(false);
        }
        holder.layout_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.sendBroadcast(new Intent(Global.BROADCAST_SelectAdressActivity_ACTION).putExtra("type",3).putExtra("position",position));
            }
        });

        //设默认地址
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.sendBroadcast(new Intent(Global.BROADCAST_SelectAdressActivity_ACTION).putExtra("type",2).putExtra("position",position));
            }
        });
        //编辑地址
        holder.layout_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent_edit=new Intent(mContext,AddAdressActivity.class);
                Intent_edit.putExtra("type",1);
                Intent_edit.putExtra("mAddressList",mAddressList.get(position));
                mContext.startActivity(Intent_edit);
            }
        });
        //删除地址
        holder.layout_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext,R.style.AlertDialog)
                        .setMessage(mContext.getString(R.string.str129))
                        .setPositiveButton(mContext.getString(R.string.sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            mContext.sendBroadcast(new Intent(Global.BROADCAST_SelectAdressActivity_ACTION).putExtra("type",1).putExtra("position",position));
                            }
                        })
                        .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                malertDialog.dismiss();
                            }
                        });
                malertDialog=alertDialog.show();
                malertDialog.show();
            }
        });
        return convertView;
    }
    private class Holder {
        TextView tv_name;
        TextView tv_phone;
        TextView tv_adress;
        RelativeLayout layout_adress;
        RelativeLayout layout_remove;
        RelativeLayout layout_edit;
        CheckBox check;
    }

}
