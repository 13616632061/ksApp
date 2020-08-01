package com.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ui.entity.StoreInfo;
import com.ui.global.Global;
import com.ui.ks.R;
import com.ui.ks.ThisMonthAllOrderActivity;
import com.ui.ks.TodayAllOrderActivity;
import com.ui.util.CustomRequest;
import com.ui.util.DialogUtils;
import com.ui.util.RequestManager;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/18.
 */

public class BranchStoreAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<StoreInfo> branch_list;
    private  String total_today;
    private String total_money_today;
    private  String total_money_yes;
    private  String total_money_mon;
    private String total_money_last;
    private int type=2;//总店：1，分店：2；
    private int click=0;
    private String account_id;
    private int cur_position=-1;
    private Dialog progressDialog = null;

    public BranchStoreAdapter(Context context, ArrayList<StoreInfo> branch_list) {
        this.context = context;
        this.branch_list = branch_list;
    }

    @Override
    public int getCount() {
        return branch_list.size();
    }

    @Override
    public Object getItem(int position) {
        return branch_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
            final Holder holder;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.branchstoreadapter_layout,null);
            holder=new Holder();
            holder.branchstore_layout= (RelativeLayout) convertView.findViewById(R.id.branchstore_layout);
            holder.btn_branchstore_num= (Button) convertView.findViewById(R.id.btn_branchstore_num);
            holder.tv_branchstore_no= (TextView) convertView.findViewById(R.id.tv_branchstore_no);
            holder.tv_branchstore_name= (TextView) convertView.findViewById(R.id.tv_branchstore_name);
            holder.iv_branchstore_drop= (ImageView) convertView.findViewById(R.id.iv_branchstore_drop);
            holder.iv_branchstore_pull_up= (ImageView) convertView.findViewById(R.id.iv_branchstore_pull_up);
            holder.branchstoreinfo_report_seller= (LinearLayout) convertView.findViewById(R.id.branchstoreinfo_report_seller);
            holder.set_item_1=convertView.findViewById(R.id.set_item_1);
            holder.set_item_2=convertView.findViewById(R.id.set_item_2);
            holder.set_item_3=convertView.findViewById(R.id.set_item_3);
            holder.set_item_4=convertView.findViewById(R.id.set_item_4);
            holder.set_item_5=convertView.findViewById(R.id.set_item_5);
            holder.relativeLayout3= (RelativeLayout) convertView.findViewById(R.id.relativeLayout3);
            holder.relativeLayout2= (RelativeLayout) convertView.findViewById(R.id.relativeLayout2);
            holder.idx_2= (ImageView) holder.set_item_2.findViewById(R.id.ll_set_idx);
            holder.idx_3= (ImageView) holder.set_item_3.findViewById(R.id.ll_set_idx);
            holder.idx_5= (ImageView) holder.set_item_5.findViewById(R.id.ll_set_idx);
            holder.id_text_2= (TextView) holder.set_item_2.findViewById(R.id.ll_set_hint_text);
            holder.id_text_3= (TextView) holder.set_item_3.findViewById(R.id.ll_set_hint_text);
            holder.id_text_5= (TextView) holder.set_item_5.findViewById(R.id.ll_set_hint_text);
            holder.textView1= (TextView) convertView.findViewById(R.id.textView1);
            holder.textView2= (TextView) convertView.findViewById(R.id.textView2);

            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        if(branch_list.size()>0){
        holder.btn_branchstore_num.setText(branch_list.get(position).getStore_num()+"");
        holder.tv_branchstore_no.setText(branch_list.get(position).getStore_no());
        holder.tv_branchstore_name.setText(branch_list.get(position).getStore_name());
        }
        holder.set_item_1.setVisibility(View.GONE);
        holder.set_item_4.setVisibility(View.GONE);
        SysUtils.setLine( holder.set_item_2, Global.SET_CELLWHITE, context.getString(R.string.turnover_of_last_month), R.drawable.icon_item_2, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_id=branch_list.get(position).getStore_no();
                toDayOrderList(4);
            }
        });
        SysUtils.setLine( holder.set_item_3, Global.SET_CELLWHITE, context.getString(R.string.turnover_of_this_month), R.drawable.icon_item_3, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_id=branch_list.get(position).getStore_no();
                toMonthOrderList(6);
            }
        });
        //上月营业额
        SysUtils.setLine( holder.set_item_5, Global.SET_CELLWHITE, context.getString(R.string.turnover_of_last_month), R.drawable.icon_item_3, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_id=branch_list.get(position).getStore_no();
                toMonthOrderList(7);
            }
        });
        holder.relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_id=branch_list.get(position).getStore_no();
                toDayOrderList(3);
            }
        });
        holder.relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_id=branch_list.get(position).getStore_no();
                toDayOrderList(3);
            }
        });
        holder.branchstore_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click%2==0) {
                    click++;
                    holder.branchstoreinfo_report_seller.setVisibility(View.VISIBLE);
                    holder.iv_branchstore_pull_up.setVisibility(View.VISIBLE);
                    holder.iv_branchstore_drop.setVisibility(View.GONE);

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("type", type + "");
                    map.put("account_id", branch_list.get(position).getStore_no());
                    CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("store_list"), map, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            if(progressDialog != null) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }

                            try {
                                JSONObject ret = SysUtils.didResponse(jsonObject);
                                System.out.println("分店数据：" + ret);
                                String status = ret.getString("status");
                                String message = ret.getString("message");
                                JSONObject dataObject = ret.getJSONObject("data");

                                if (!status.equals("200")) {
                                    SysUtils.showError(message);
                                } else {
                                    JSONArray arry = dataObject.optJSONArray("info");
                                    if (arry != null && arry.length() > 0) {
                                        for (int i = 0; i < arry.length(); i++) {
                                            JSONObject data = arry.optJSONObject(i);
                                            total_today = data.getString("total_today");
                                            total_money_today = data.getString("total_money_today");
                                            total_money_yes = data.getString("total_money_yes");
                                            total_money_mon = data.getString("total_money_mon");
                                            total_money_last = data.getString("total_money_last");
//                                ad_list.add(b);
                                        }
                                        holder.id_text_2.setText("￥" + total_money_yes);
                                        holder.id_text_5.setText("￥" + total_money_last);
                                        holder.id_text_3.setText("￥" + total_money_mon);
                                        holder.textView1.setText(total_today);
                                        holder.textView2.setText("￥" + total_money_today);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if(progressDialog != null) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            SysUtils.showNetworkError();
                        }
                    });
                    RequestManager.addRequest(r, context);
                    progressDialog = DialogUtils.createLoadingDialog(context, context.getResources().getString(R.string.str92), true);
                    progressDialog.show();
                }else{
                    click++;
                    holder.branchstoreinfo_report_seller.setVisibility(View.GONE);
                    holder.iv_branchstore_pull_up.setVisibility(View.GONE);
                    holder.iv_branchstore_drop.setVisibility(View.VISIBLE);
                    RequestManager.cancelAll(context);
                }
            }
        });
//        holder.iv_branchstore_pull_up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.branchstoreinfo_report_seller.setVisibility(View.GONE);
//                holder.iv_branchstore_pull_up.setVisibility(View.GONE);
//                holder.iv_branchstore_drop.setVisibility(View.VISIBLE);
//
//            }
//        });
//        holder.iv_branchstore_drop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.branchstoreinfo_report_seller.setVisibility(View.VISIBLE);
//                holder.iv_branchstore_pull_up.setVisibility(View.VISIBLE);
//                holder.iv_branchstore_drop.setVisibility(View.GONE);
//            }
//        });
        return convertView;
    }
    private class Holder{
        Button btn_branchstore_num;
        TextView tv_branchstore_no;
        TextView tv_branchstore_name;
        ImageView iv_branchstore_pull_up;
        ImageView iv_branchstore_drop;
        LinearLayout branchstoreinfo_report_seller;
        RelativeLayout branchstore_layout;

        View set_item_1;
        View set_item_2;
        View set_item_3;
        View set_item_4;
        View set_item_5;
        ImageView idx_2;
        ImageView idx_3;
        ImageView idx_4;
        ImageView idx_5;
        TextView id_text_2;
        TextView id_text_3;
        TextView id_text_5;
        TextView textView1;
        TextView textView2;
        RelativeLayout relativeLayout3;//今日付款量
        RelativeLayout relativeLayout2;//今日付款量



    }
    /**
     * 3表示今天，4表示昨天
     * @param type
     */
    private void toDayOrderList(int type) {
        Bundle b = new Bundle();
        b.putInt("type", type);
        b.putString("account_id",account_id);

        SysUtils.startAct(context, new TodayAllOrderActivity(), b);
    }

    /**
     * 6表示本月，7表示上月
     * @param type
     */
    private void toMonthOrderList(int type) {
        Bundle b = new Bundle();
        b.putInt("type", type);
        b.putString("account_id",account_id);

        SysUtils.startAct(context, new ThisMonthAllOrderActivity(), b);
    }
}
