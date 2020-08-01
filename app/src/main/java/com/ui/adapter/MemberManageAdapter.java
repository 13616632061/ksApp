package com.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ui.entity.Member;
import com.ui.ks.MemberManageActivity;
import com.ui.ks.R;
import com.ui.util.SetEditTextInput;

import java.util.List;

/**
 * 首页订单列表适配器
 * Created by Administrator on 2016/12/24.
 */

public class MemberManageAdapter extends BaseAdapter{

    private List<Member.ResponseBean.DataBean.InfoBean> cat_list;
    private MemberManageActivity fragmentActivity;

    public MemberManageAdapter(MemberManageActivity fragmentActivity, List<Member.ResponseBean.DataBean.InfoBean> cat_list) {
        this.fragmentActivity = fragmentActivity;
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
            convertView=View.inflate(fragmentActivity, R.layout.itme_member_manage,null);
            holder=new Holder();
            holder.tv_number= (TextView) convertView.findViewById(R.id.tv_number);
            holder.tv_member_name= (TextView) convertView.findViewById(R.id.tv_member_name);
            holder.tv_member_phone= (TextView) convertView.findViewById(R.id.tv_member_phone);
            holder.tv_member_integral= (TextView) convertView.findViewById(R.id.tv_member_integral);
            holder.tv_member_balance= (TextView) convertView.findViewById(R.id.tv_member_balance);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }

        Member.ResponseBean.DataBean.InfoBean data=cat_list.get(position);
        holder.tv_number.setText((position+1)+"");
        holder.tv_member_name.setText(data.getMember_name());
        holder.tv_member_phone.setText(data.getMobile());
        holder.tv_member_integral.setText(data.getScore());
        holder.tv_member_balance.setText(SetEditTextInput.stringpointtwo(data.getSurplus()));
        return convertView;
    }

    public class Holder{
        TextView tv_number;//序号
        TextView tv_member_name;//会员名
        TextView tv_member_phone;//会员手机号
        TextView tv_member_integral;//会员积分
        TextView tv_member_balance;//会员余额
    }

}
