package com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ui.entity.ShoppingMallShoppingCar;
import com.ui.entity.ShoppingMallShoppingCar_info;
import com.ui.ks.R;
import com.ui.listview.PagingListView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/14.
 */

public class ShoppingMallShoppingCarFragmentAdapter1 extends BaseAdapter {
    private Context context;
    private ArrayList<ShoppingMallShoppingCar> shoppingMallShoppingCars;

    public ShoppingMallShoppingCarFragmentAdapter1(Context context, ArrayList<ShoppingMallShoppingCar> shoppingMallShoppingCars) {
        this.context = context;
        this.shoppingMallShoppingCars = shoppingMallShoppingCars;
    }

    @Override
    public int getCount() {
        return shoppingMallShoppingCars.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingMallShoppingCars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_shoppingcarfragment,null);
            holder=new Holder();
            holder.check_shopper= (CheckBox) convertView.findViewById(R.id.check_shopper);
            holder.tv_shoppername= (TextView) convertView.findViewById(R.id.tv_shoppername);
            holder.tv_shoppernotes= (TextView) convertView.findViewById(R.id.tv_shoppernotes);
            holder.lv_content= (PagingListView) convertView.findViewById(R.id.lv_content);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.lv_content.setAdapter(new listitemadapter(context,shoppingMallShoppingCars.get(position).getShoppingMallShoppingCar_infos()));
        setListViewHeightBasedOnChildren(holder.lv_content);
        return convertView;
    }
    private class Holder{
        CheckBox check_shopper;
        TextView tv_shoppername;
        TextView tv_shoppernotes;
        PagingListView lv_content;
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

    private class listitemadapter extends BaseAdapter{
        private Context context;
        private ArrayList<ShoppingMallShoppingCar_info> shoppingMallShoppingCar_infos;

        public listitemadapter(Context context, ArrayList<ShoppingMallShoppingCar_info> shoppingMallShoppingCar_infos) {
            this.context = context;
            this.shoppingMallShoppingCar_infos = shoppingMallShoppingCar_infos;
        }

        @Override
        public int getCount() {
            return shoppingMallShoppingCar_infos.size();
        }

        @Override
        public Object getItem(int position) {
            return shoppingMallShoppingCar_infos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            holder holder=null;
            if(convertView==null){
                convertView=View.inflate(context, R.layout.item_shoppingcaifragment_item,null);
                holder=new holder();
                holder.check_shopping= (CheckBox) convertView.findViewById(R.id.check_shopping);
                holder.iv_shoppingpicture= ( ImageView) convertView.findViewById(R.id.iv_shoppingpicture);
                holder.tv_shoppingname= (TextView) convertView.findViewById(R.id.tv_shoppingname);
                holder.tv_shoppingdetails= (TextView) convertView.findViewById(R.id.tv_shoppingdetails);
                holder.tv_shoppingprice= (TextView) convertView.findViewById(R.id.tv_shoppingprice);
                convertView.setTag(holder);
            }else {
                holder= (holder) convertView.getTag();
            }
            return convertView;
        }
        private class holder {
            CheckBox check_shopping;
            ImageView iv_shoppingpicture;
            TextView tv_shoppingname;
            TextView tv_shoppingdetails;
            TextView tv_shoppingprice;
        }
    }
}
