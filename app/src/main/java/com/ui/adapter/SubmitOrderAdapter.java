package com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ui.entity.GetOpenOrder;
import com.ui.entity.GetOpenOrder_info;
import com.ui.ks.R;
import com.ui.listview.PagingListView;
import com.library.utils.BigDecimalArith;
import com.ui.util.DateUtils;
import com.ui.util.SetEditTextInput;

import java.util.ArrayList;

/**
 * 开单功能提交订单适配器
 * Created by Administrator on 2017/4/20.
 */

public class SubmitOrderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GetOpenOrder> getOpenOrders_choose;
    private  ArrayList<GetOpenOrder_info> submitorder_item_list;

    public SubmitOrderAdapter(Context context, ArrayList<GetOpenOrder> getOpenOrders_choose) {
        this.context = context;
        this.getOpenOrders_choose = getOpenOrders_choose;
    }

    @Override
    public int getCount() {
        return getOpenOrders_choose.size();
    }

    @Override
    public Object getItem(int position) {
        return getOpenOrders_choose.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.submitorder_item,null);
            holder=new Holder();
            holder.tv_odernum= (TextView) convertView.findViewById(R.id.tv_odernum);
            holder.tv_ordertime= (TextView) convertView.findViewById(R.id.tv_ordertime);
            holder.tv_total_price_item= (TextView) convertView.findViewById(R.id.tv_total_price_item);
            holder.list_submitorder_item= (PagingListView) convertView.findViewById(R.id.list_submitorder_item);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.tv_odernum.setText(getOpenOrders_choose.get(position).getOrder_id());
        holder.tv_ordertime.setText(DateUtils.getDateTimeFromMillisecond(Long.parseLong(getOpenOrders_choose.get(position).getCreat_time())*1000)+"");
        submitorder_item_list=new ArrayList<>();
        double tv_total_price_item=0.00;
        if(getOpenOrders_choose.get(position).getGetOpenOrder_infos()!=null) {
            for (int i = 0; i < getOpenOrders_choose.get(position).getGetOpenOrder_infos().size(); i++) {
                submitorder_item_list.add(getOpenOrders_choose.get(position).getGetOpenOrder_infos().get(i));
                tv_total_price_item += Double.parseDouble(SetEditTextInput.stringpointtwo((Double.parseDouble(getOpenOrders_choose.get(position).getGetOpenOrder_infos().get(i).getPrice()) *
                        Double.parseDouble(getOpenOrders_choose.get(position).getGetOpenOrder_infos().get(i).getNum())) + ""));
            }
            holder.tv_total_price_item.setText("￥" + SetEditTextInput.stringpointtwo(tv_total_price_item+""));
            List_submitorder_itemAdapter list_submitorder_itemAdapter = new List_submitorder_itemAdapter(context, submitorder_item_list);
            holder.list_submitorder_item.setAdapter(list_submitorder_itemAdapter);
            setListViewHeightBasedOnChildren(holder.list_submitorder_item);
        }
        return convertView;
    }
    private class  Holder{
        TextView tv_odernum;//订单号
        TextView tv_ordertime;//订单创建时间
        TextView tv_total_price_item;//订单创建时间
        PagingListView list_submitorder_item;//订单商品列表

    }
    /***
     * 动态设置listview的高度 item 总布局必须是linearLayout
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
                + 1;
        listView.setLayoutParams(params);
    }

    /**
     * 订单列表适配器
     */
    private class  List_submitorder_itemAdapter extends BaseAdapter{

        private Context context;
        private  ArrayList<GetOpenOrder_info> submitorder_item_list;

        public List_submitorder_itemAdapter(Context context, ArrayList<GetOpenOrder_info> submitorder_item_list) {
            this.context = context;
            this.submitorder_item_list = submitorder_item_list;
        }

        @Override
        public int getCount() {
            return submitorder_item_list.size();
        }

        @Override
        public Object getItem(int position) {
            return submitorder_item_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder=null;
            if(convertView==null){
                convertView=View.inflate(context, R.layout.submitorder_item_list_intem,null);
                holder=new Holder();
                holder.tv_good_name= (TextView) convertView.findViewById(R.id.tv_good_name);
                holder.tv_good_num= (TextView) convertView.findViewById(R.id.tv_good_num);
                holder.tv_good_price= (TextView) convertView.findViewById(R.id.tv_good_price);
                convertView.setTag(holder);
            }else {
                holder= (Holder) convertView.getTag();
            }
            holder.tv_good_name.setText(submitorder_item_list.get(position).getName());
            holder.tv_good_num.setText("x"+submitorder_item_list.get(position).getNum());
            double tv_good_num=Double.parseDouble(submitorder_item_list.get(position).getNum()+"");
            double tv_good_price=Double.parseDouble(submitorder_item_list.get(position).getPrice()+"");
            holder.tv_good_price.setText("￥"+SetEditTextInput.stringpointtwo((BigDecimalArith.mul(tv_good_price,tv_good_num)+"")));
            return convertView;
        }

        private class Holder {
            TextView tv_good_name;//商品名称
            TextView tv_good_num;//商品数量
            TextView tv_good_price;//商品价格
        }
    }
}
