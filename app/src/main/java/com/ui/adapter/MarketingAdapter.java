package com.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ui.entity.MemberSpecifications;
import com.ui.ks.MemberSpecificationsActivity;
import com.ui.ks.R;

import java.util.List;

/**
 * 首页订单列表适配器
 * Created by Administrator on 2016/12/24.
 */

public class MarketingAdapter extends BaseAdapter{

    private List<MemberSpecifications> cat_list;
    private MemberSpecificationsActivity Activity;

    public MarketingAdapter(MemberSpecificationsActivity Activity, List<MemberSpecifications> cat_list) {
        this.Activity = Activity;
        this.cat_list = cat_list;
    }

    @Override
    public int getCount() {
        return cat_list.size();
    }

    @Override
    public Object getItem(int position) {
        return cat_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(Activity, R.layout.itme_marketing,null);
            holder=new Holder();
            holder.tv_recharge_amount= (TextView) convertView.findViewById(R.id.tv_recharge_amount);
            holder.tv_donation_amount= (TextView) convertView.findViewById(R.id.tv_donation_amount);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }

        MemberSpecifications data=cat_list.get(position);
        holder.tv_recharge_amount.setText(data.getVal());
        holder.tv_donation_amount.setText(data.getGive());
        return convertView;
    }
    public class Holder{
        TextView tv_recharge_amount;
        TextView tv_donation_amount;

    }

}
