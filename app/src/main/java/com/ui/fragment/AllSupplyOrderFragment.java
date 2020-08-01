package com.ui.fragment;

import android.app.Dialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ui.adapter.AllSupplyOrderFragmentAdapter;
import com.ui.entity.OrderPageorder;
import com.ui.entity.SupplyOrderPageOrder;
import com.ui.ks.R;
import com.ui.util.CustomRequest;
import com.ui.util.DialogUtils;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *46546464546
 * 所有订单页面
 * Created by Administrator on 2017/7/3.
 */

public class AllSupplyOrderFragment extends BaseFragmentMainBranch implements SwipeRefreshLayout.OnRefreshListener {
    private ListView list_content;
    private SwipeRefreshLayout refresh_header;
    private Dialog progressDialog = null;
    private ArrayList<OrderPageorder> mOrderPageorderlist;
    private ArrayList<SupplyOrderPageOrder> mSupplyOrderPageOrderlist;

    /**
     * 是否可见状态
     */
    private boolean isVisible;
    /**
     * 标志位，View已经初始化完成。
     * isPrepared还是准一些,isAdded有可能出现onCreateView没走完但是isAdded了
     */
    private boolean isPrepared;
    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;


    @Override
    protected View initView() {
        isFirstLoad = true;
        View view=View.inflate(mContext,R.layout.allsupplyorderfragment,null);
        list_content= (ListView) view.findViewById(R.id.list_content);
        refresh_header = (SwipeRefreshLayout) view.findViewById(R.id.refresh_header);
        refresh_header.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);
        refresh_header.setOnRefreshListener(this);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }

    }
    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }
    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared && isVisible && isFirstLoad) {
            isFirstLoad = false;
            getOrder_list();
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    /**
     * 获取订单数据
     */
    private void getOrder_list(){
        Map<String,String> map=new HashMap<>();
        map.put("type","1");
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("purchase_order"),map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                setRefreshing(false);
                if(progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("ret："+ret);
                    String status_str= ret.getString("status");
                    String message = ret.getString("message");
                    if (!status_str.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        JSONArray data=ret.getJSONArray("data");
                        if(data!=null){
                            mSupplyOrderPageOrderlist=new ArrayList<>();
                            for(int i=0;i<data.length();i++){
                                JSONObject object_data=data.getJSONObject(i);
                                String order_id=object_data.getString("order_id");
                                String seller_id=object_data.getString("seller_id");
                                String seller_name=object_data.getString("seller_name");
                                String final_amount=object_data.getString("final_amount");
                                String cost_item=object_data.getString("cost_item");
                                String cost_freight=object_data.getString("cost_freight");
                                String createtime=object_data.getString("createtime");
                                String total_quantity=object_data.getString("total_quantity");
                                String ship_name=object_data.getString("ship_name");
                                String ship_tel=object_data.getString("ship_tel");
                                String ship_area=object_data.getString("ship_area");
                                String ship_addr=object_data.getString("ship_addr");
                                String ship_time=object_data.getString("ship_time");
                                String payment=object_data.getString("payment");
                                String pay_status=object_data.getString("pay_status");
                                String ship_status=object_data.getString("ship_status");
                                String status=object_data.getString("status");
                                JSONArray goods_info=null;
                                try{
                                    goods_info=object_data.getJSONArray("goods_info");
                                }catch (Exception e){
                                    System.out.print("e="+e.toString());
                                }
                                mOrderPageorderlist=new ArrayList<>();
                                for(int j=0;j<goods_info.length();j++){
                                    JSONObject object_goods_info=goods_info.getJSONObject(j);
                                    String goods_id=object_goods_info.getString("goods_id");
                                    String name=object_goods_info.getString("name");
                                    String price=object_goods_info.getString("price");
                                    String quantity=object_goods_info.getString("nums");
                                    String brief=object_goods_info.getString("brief");
                                    String img_src=object_goods_info.getString("img_src");
                                    OrderPageorder mOrderPageorder=new OrderPageorder(goods_id,name,price,quantity,brief,img_src);
                                    mOrderPageorderlist.add(mOrderPageorder);
                                }
                                SupplyOrderPageOrder mSupplyOrderPageOrder=new SupplyOrderPageOrder(order_id,seller_id,seller_name,final_amount,
                                        cost_item,cost_freight,createtime,total_quantity,ship_name,ship_tel,ship_area,ship_addr,ship_time,payment,
                                        pay_status,ship_status,status,mOrderPageorderlist);
                                mSupplyOrderPageOrderlist.add(mSupplyOrderPageOrder);
                            }
                            list_content.setAdapter(new AllSupplyOrderFragmentAdapter(getActivity(), mSupplyOrderPageOrderlist));
                        }
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                    System.out.print("e="+e.toString());
                } finally {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setRefreshing(false);
                if(progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
//                setNoNetwork();
            }
        });
        executeRequest(r);
        progressDialog = DialogUtils.createLoadingDialog(mContext, getString(R.string.str92), true);
        progressDialog.show();

    }

    @Override
    public void onRefresh() {
        getOrder_list();
    }
    private void setRefreshing(boolean refreshing) {
        refresh_header.setRefreshing(refreshing);
    }
}
