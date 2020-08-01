package com.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ui.entity.Order;
import com.ui.ks.R;

import java.util.ArrayList;

/**
 * 首页订单列表适配器
 * Created by Administrator on 2016/12/24.
 */

public class OrderFragmentAdapter extends BaseAdapter{

    private ArrayList<Order> cat_list;
    private FragmentActivity fragmentActivity;

    public OrderFragmentAdapter(FragmentActivity fragmentActivity, ArrayList<Order> cat_list) {
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
            convertView=View.inflate(fragmentActivity, R.layout.item_orderfragment,null);
            holder=new Holder();
            holder.tv_orderlist_date= (TextView) convertView.findViewById(R.id.tv_orderlist_date);
            holder.tv_orderlist_time= (TextView) convertView.findViewById(R.id.tv_orderlist_time);
            holder.tv_payprice= (TextView) convertView.findViewById(R.id.tv_payprice);
            holder.iv_payment= (ImageView) convertView.findViewById(R.id.iv_payment);
            holder.tv_sendtype= (TextView) convertView.findViewById(R.id.tv_sendtype);
            holder.tv_payment= (TextView) convertView.findViewById(R.id.tv_payment);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
          Order data=cat_list.get(position);
        if(data!=null){
            holder.tv_orderlist_date.setText(fragmentActivity.getString(R.string.today));
            holder.tv_orderlist_time.setText((data.getOrderTime().substring(11)).substring(0,5));//取后面时间字符,并去掉秒
            holder.tv_payment.setText(data.getPaymentStr(fragmentActivity));
            holder.tv_sendtype.setText(data.getShippingStr(fragmentActivity,holder.tv_sendtype));
            holder.iv_payment.setImageResource(data.getPaymentRes());
            if (data.getCost_item() > 0) {
                holder.tv_payprice.setText(data.getCost_item()+"");
            } else {
                holder.tv_payprice.setText("");
            }

        }

        return convertView;
    }
    public class Holder{
        TextView tv_orderlist_time;//订单时间
        TextView tv_orderlist_date;//订单日期
        TextView tv_payprice;//订单价格
        ImageView iv_payment;//支付图片
        TextView tv_sendtype;
        TextView tv_payment;
    }

}
