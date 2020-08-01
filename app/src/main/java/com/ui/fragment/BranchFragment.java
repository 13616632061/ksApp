package com.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ui.adapter.BranchStoreAdapter;
import com.ui.entity.StoreInfo;
import com.ui.ks.R;
import com.ui.util.CustomRequest;
import com.ui.util.RequestManager;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 分店
 * Created by Administrator on 2017/1/18.
 */

public class BranchFragment  extends  BaseFragmentMainBranch {

    private SwipeRefreshLayout refresh_header;
    private ListView lv_content;
    private ArrayList<StoreInfo> branch_list;
    private BranchStoreAdapter branchStoreAdapter;
    private int type=2;//总店：1，分店：2；

    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;
    @Override
    protected View initView() {
        View view=View.inflate(mContext, R.layout.branchstorelayout,null);
        layout_err= view.findViewById(R.id.layout_err);
        include_nowifi=  layout_err.findViewById(R.id.include_nowifi);
        include_noresult=  layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_btn_refresh_net = (Button) include_nowifi.findViewById(R.id.load_btn_refresh_net);

        refresh_header= (SwipeRefreshLayout) view.findViewById(R.id.refresh_header);
        refresh_header.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);
        refresh_header.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        lv_content= (ListView) view.findViewById(R.id.lv_content);
        load_btn_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        return view;
    }
    @Override
    protected void initData() {
        super.initData();
        branch_list=new ArrayList<StoreInfo>();
        getData();
    }
    private void setRefreshing(boolean refreshing) {
        refresh_header.setRefreshing(refreshing);
    }
    private void getData() {
        Map<String,String> map= new HashMap<String,String>();
        map.put("type",type+"");
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("store_list"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                setRefreshing(false);

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("总店数据："+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        branch_list.clear();
                        JSONArray arry = dataObject.optJSONArray("list");
                        if (arry  != null && arry .length() > 0) {
                            for (int i = 0; i < arry .length(); i++) {
                                JSONObject data = arry .optJSONObject(i);
                                StoreInfo storeInfo_branch=new StoreInfo(i+1,data.getString("account_id"),data.getString("login_name"));
                                branch_list.add(storeInfo_branch);
                            }
                        }
                        setView();
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
//                    branchStoreAdapter=new BranchStoreAdapter(getActivity(),branch_list);
//                    lv_content.setAdapter(branchStoreAdapter);
                    updateAdapter();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setRefreshing(false);
                setNoNetwork();
                SysUtils.showNetworkError();
            }
        });

        RequestManager.addRequest(r, mContext);
    }
    //    更新适配器
    private void updateAdapter() {

        if (branchStoreAdapter == null) {
            branchStoreAdapter = new BranchStoreAdapter(getActivity(), branch_list);
            lv_content.setAdapter(branchStoreAdapter);

        } else {
            branchStoreAdapter.notifyDataSetChanged();
        }
    }
    //是否有数据返回
    private void setView() {
//            Log.v("ks", "size: " + cat_list.size());
            if(branch_list.size() < 1) {
                //没有结果
                lv_content.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                include_noresult.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            } else {
                //有结果
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                layout_err.setVisibility(View.GONE);
                lv_content.setVisibility(View.VISIBLE);
            }
    }
    //是否有网络
    private void setNoNetwork() {
        //网络不通
        if(branch_list.size() < 1) {
                lv_content.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
        } else {
            SysUtils.showNetworkError();
        }
    }
}
