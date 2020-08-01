package com.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ui.db.DBHelper;
import com.ui.entity.GetOpenOrder;
import com.ui.entity.Goods_info;
import com.ui.entity.OrderGoods;
import com.ui.global.Global;
import com.ui.ks.R;
import com.ui.listview.PagingListView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/13.
 */

public class GetOpenOrderAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<GetOpenOrder> getOpenOrders;

    public ArrayList<OrderGoods> goodsList;

    public GetOpenOrderAdapter(Context context, ArrayList<GetOpenOrder> getOpenOrders) {
        this.context = context;
        this.getOpenOrders = getOpenOrders;
    }

    @Override
    public int getCount() {
        return getOpenOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return getOpenOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Horlder horlder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.getorderopen_item,null);
            horlder=new Horlder();
            horlder.layout_order= (RelativeLayout) convertView.findViewById(R.id.layout_order);
            horlder.layout_choose= (RelativeLayout) convertView.findViewById(R.id.layout_choose);
            horlder.iv_choose= (ImageView) convertView.findViewById(R.id.iv_choose);
            horlder.tv_notes= (TextView) convertView.findViewById(R.id.tv_notes);
            horlder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            horlder.iv_drop= (ImageView) convertView.findViewById(R.id.iv_drop);
            horlder.layout_open= (LinearLayout) convertView.findViewById(R.id.layout_open);
            horlder.list_order= (PagingListView) convertView.findViewById(R.id.list_order);
            horlder.layout_remove= (LinearLayout) convertView.findViewById(R.id.layout_remove);
            horlder.btn_edit= (Button) convertView.findViewById(R.id.btn_edit);
            horlder.btn_print= (Button) convertView.findViewById(R.id.btn_print);
            convertView.setTag(horlder);
        }else {
            horlder= (Horlder) convertView.getTag();
        }
        if(getOpenOrders.get(position).getNotes()==null|| TextUtils.isEmpty(getOpenOrders.get(position).getNotes())
                ||getOpenOrders.get(position).getNotes().equals("null")){
            horlder.tv_notes.setText("");
        }else {
            horlder.tv_notes.setText(getOpenOrders.get(position).getNotes());
        }
        horlder.tv_name.setText(getOpenOrders.get(position).getName());
        final Horlder finalHorlder = horlder;
        //选中
        horlder.layout_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getOpenOrders.get(position).ischoose()){
                    getOpenOrders.get(position).setIschoose(false);
                    finalHorlder.iv_choose.setBackgroundResource(R.drawable.gray_gou);
                    context.sendBroadcast(new Intent(Global.BROADCAST_GetOpenOrder_ACTION).putExtra("type",3).putExtra("price",getOpenOrders.get(position).getPrice()));
                }else {
                    getOpenOrders.get(position).setIschoose(true);
                    finalHorlder.iv_choose.setBackgroundResource(R.drawable.green_gou);
                    context.sendBroadcast(new Intent(Global.BROADCAST_GetOpenOrder_ACTION).putExtra("type",2).putExtra("price",getOpenOrders.get(position).getPrice()));
                }
            }
        });
        if(getOpenOrders.get(position).ischoose()){
            finalHorlder.iv_choose.setBackgroundResource(R.drawable.green_gou);
        }else{
            finalHorlder.iv_choose.setBackgroundResource(R.drawable.gray_gou);
        }
        //点击列表项，展开商品信息，其他列表项关闭
        final Horlder finalHorlder1 = horlder;
        final Horlder finalHorlder2 = horlder;
        horlder.layout_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( getOpenOrders.get(position).isopen()){
                    getOpenOrders.get(position).setIsopen(false);
                    finalHorlder1.layout_open.setVisibility(View.GONE);
                }else {
                    getOpenOrders.get(position).setIsopen(true);
                    finalHorlder1.layout_open.setVisibility(View.VISIBLE);
                    context.sendBroadcast(new Intent(Global.BROADCAST_GetOpenOrder_ACTION).putExtra("type",1).putExtra("position",position));
                    for(int i=0;i<getOpenOrders.size();i++){
                        if(i!=position){
                            getOpenOrders.get(i).setIsopen(false);
                            finalHorlder1.layout_open.setVisibility(View.GONE);
                            notifyDataSetChanged();
                        }
                    }
                    GetOpenOrder_itemAdapter getOpenOrder_itemAdapter=new GetOpenOrder_itemAdapter(context,getOpenOrders.get(position).getGetOpenOrder_infos());
                    finalHorlder2.list_order.setAdapter(getOpenOrder_itemAdapter);
                    setListViewHeightBasedOnChildren(finalHorlder2.list_order);
                }
            }
        });
        if(getOpenOrders.get(position).isopen()){
            finalHorlder1.layout_open.setVisibility(View.VISIBLE);
        }else{
            finalHorlder1.layout_open.setVisibility(View.GONE);
        }
        horlder.layout_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.sendBroadcast(new Intent(Global.BROADCAST_GetOpenOrder_ACTION).putExtra("type",4).putExtra("position",position).putExtra("order_id",getOpenOrders.get(position).getOrder_id()));
            }
        });
        horlder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.sendBroadcast(new Intent(Global.BROADCAST_OpenOrderActivity_ACTION).putExtra("type",6));
//                Intent intent=new Intent(context, OpenOrderActivity.class);
                Bundle bundle=new Bundle();
                ArrayList arrayList1=new ArrayList();
                arrayList1.add(getOpenOrders.get(position));
                ArrayList arrayList2=new ArrayList();
                arrayList2.add(getOpenOrders.get(position).getGetOpenOrder_infos());
                bundle.putParcelableArrayList("list1",arrayList1);
                bundle.putParcelableArrayList("list2",getOpenOrders.get(position).getGetOpenOrder_infos());
//                intent.putExtra("type",4);
//                intent.putExtra("bundle",bundle);
//                context.startActivity(intent);
                context.sendBroadcast(new Intent(Global.BROADCAST_OpenOrderActivity_ACTION).putExtra("type",5 ).putExtra("bundle",bundle));
                context.sendBroadcast(new Intent(Global.BROADCAST_GetOpenOrder_ACTION).putExtra("type",5 ));
            }
        });

        horlder.btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsList=new ArrayList<OrderGoods>();
                goodsList.clear();
                for(int i=0;i<getOpenOrders.get(position).getGetOpenOrder_infos().size();i++){
                    OrderGoods orderGoods=new OrderGoods(Integer.parseInt(getOpenOrders.get(position).getGetOpenOrder_infos().get(i).getNum()),
                            getOpenOrders.get(position).getGetOpenOrder_infos().get(i).getName(),
                            Double.parseDouble(getOpenOrders.get(position).getGetOpenOrder_infos().get(i).getPrice()));
                    goodsList.add(orderGoods);
                }
                context.sendBroadcast(new Intent(Global.BROADCAST_GetOpenOrder_ACTION).putExtra("type",6).putExtra("order_id",getOpenOrders.get(position).getOrder_id()).
                        putExtra("creat_time",getOpenOrders.get(position).getCreat_time()+"").putExtra("price",getOpenOrders.get(position).getPrice()+"").
                        putParcelableArrayListExtra("goodsList", goodsList).putExtra("order_mark",getOpenOrders.get(position).getNotes()));

            }
        });
        return convertView;
    }
    private class Horlder{
        private RelativeLayout layout_order;//开单订单列表
        private RelativeLayout layout_choose;//选中区域
        private ImageView iv_choose;//选中图标
        private TextView tv_notes;//备注
        private TextView tv_name;//列表中的商品名称
        private ImageView iv_drop;//列表中的指示图标
        private LinearLayout layout_open;//点击列表展示的区域
        private PagingListView list_order;//展示区域的商品名字
        private LinearLayout layout_remove;//作废
        private Button btn_edit;//订单编辑
        private Button btn_print;//订单打印
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
                + 35;
        listView.setLayoutParams(params);
    }
    /**
     *编辑
     * @param goods_info
     */
    private void addopenorder(Goods_info goods_info){
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM openorder WHERE id = ?";
        Cursor cursor = sqlite.rawQuery(sql, new String[] { goods_info.getGoods_id()});
        if(cursor.moveToFirst()){
            int num_db=cursor.getInt(cursor.getColumnIndex("num"));
            num_db++;
            sqlite.execSQL("UPDATE openorder SET num = ? where id="+goods_info.getGoods_id(),
                    new Object[] {num_db});
        }else {
            sqlite.execSQL("insert into openorder (id,tag_id,name,price,num) values(?,?,?,?,?)",
                    new Object[]{goods_info.getGoods_id(),goods_info.getTag_id(),
                            goods_info.getName(),goods_info.getPrice(),goods_info.getSelect_num()});
        }
        cursor.close();
        sqlite.close();
    }
    /**
     * 清空数据表
     */
    private void clearShoppingCarDB(){
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = "DELETE FROM  openorder";
        sqlite.execSQL(sql);
    }
}
