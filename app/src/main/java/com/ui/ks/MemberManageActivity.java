package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ui.adapter.MemberManageAdapter;
import com.ui.entity.Member;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.DialogUtils;
import com.ui.util.SysUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2020/2/10.
 */

public class MemberManageActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {


    SwipeRefreshLayout refresh_header;
    PagingListView lv_content;
    private boolean isFirst = true;// 判断是否第一次进入滑动界面
    private boolean scrollFlag = false;// 表示是否滑动

    boolean loadingMore = false;
    int PAGE=1;
    int PAGE_hdfk=1;
    MemberManageAdapter adapter;
    Member member;
    List<Member.ResponseBean.DataBean.InfoBean> datas=new ArrayList<>();

    ImageView iv_managmentfragment_back;
    TextView tv_add_member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_manage);
//        SysUtils.setupUI(MemberManageActivity.this,findViewById(R.id.member_manage));
//        initToolbar(this);
        initView();
    }



    //初始化View
    private void initView() {
        refresh_header= (SwipeRefreshLayout) findViewById(R.id.refresh_header);
        lv_content= (PagingListView) findViewById(R.id.lv_content);

        lv_content.setOnItemClickListener(this);

        iv_managmentfragment_back= (ImageView) findViewById(R.id.iv_managmentfragment_back);
        iv_managmentfragment_back.setOnClickListener(this);
        tv_add_member= (TextView) findViewById(R.id.tv_add_member);
        tv_add_member.setOnClickListener(this);

        loadFirst();

        View view= LayoutInflater.from(this).inflate(R.layout.itme_member_manage,null);
        lv_content.addHeaderView(view);

        refresh_header.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);

        refresh_header.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefreshing(false);
            }
        });

        lv_content.setHasMoreItems(true);
        lv_content.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if (loadingMore) {
                    //加载更多
                    loadData();
                } else {
                    updateAdapter();
                }
            }
        });

    }

    private void updateAdapter() {
        lv_content.onFinishLoading(loadingMore, null);

        if(adapter == null) {
            if (member!=null) {
                adapter = new MemberManageAdapter(MemberManageActivity.this, datas);
                lv_content.setAdapter(adapter);
            }
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void setRefreshing(boolean refreshing) {
        refresh_header.setRefreshing(refreshing);
    }

    private void setNoNetwork() {
        //网络不通
//        if(PAGE <= 1 && cat_list.size() < 1) {
//            if(!include_nowifi.isShown()) {
//                lv_content.setVisibility(View.GONE);
//                include_noresult.setVisibility(View.GONE);
//                order_total_statistics_layout.setVisibility(View.GONE);
//                include_nowifi.setVisibility(View.VISIBLE);
//                layout_err.setVisibility(View.VISIBLE);
//            }
//        } else {
            SysUtils.showNetworkError();
//        }
    }


    private void loadFirst(){
        PAGE = 1;
        PAGE_hdfk=1;
        loadData();
    }


    public void loadData(){
        String uri=SysUtils.getSellerpinbanServiceUrl("members_list");
        Map<String,String> map = new HashMap<String,String>();
        map.put("page",PAGE+"");
        CustomRequest r =new CustomRequest(Request.Method.POST, uri, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                Gson gson=new Gson();
                member=gson.fromJson(response.toString(),Member.class);
                datas.addAll(member.getResponse().getData().getInfo());
                setRefreshing(false);
                lv_content.setIsLoading(false);
//                adapter=new MemberManageAdapter(MemberManageActivity.this,datas);
//                lv_content.setAdapter(adapter);
                int total=member.getResponse().getData().getNums();
                if (Integer.valueOf(total) % 10 == 0) {
                    if (PAGE < (Integer.valueOf(total) / 10)) {
                        loadingMore=true;
                    }else {
                        loadingMore= false;
                        setRefreshing(false);
                        lv_content.setIsLoading(false);
                    }
                }else {
                    if (PAGE < (Integer.valueOf(total) / 10 + 1)) {
                        loadingMore=true;
                    }else {
                        loadingMore= false;
                        setRefreshing(false);
                        lv_content.setIsLoading(false);
                    }
                }
                    if (loadingMore) {
                        PAGE++;
                    }
                }catch (JsonSyntaxException e){
                    e.printStackTrace();
                }finally {
                    setRefreshing(false);
                    lv_content.setIsLoading(false);
                    updateAdapter();
//                    lv_content.setSelection((PAGE-2)*10);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setRefreshing(false);
                lv_content.setIsLoading(false);
                setNoNetwork();
            }
        });
        executeRequest(r);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(MemberManageActivity.this,MemberDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_managmentfragment_back:
                finish();
                break;
            case R.id.tv_add_member:
                DialogUtils.ShowAddMember(MemberManageActivity.this);
                DialogUtils dialogUtils=new DialogUtils();
                dialogUtils.SetOnAddMember(new DialogUtils.OnAddMember() {
                    @Override
                    public void addMember(String InventoryStock) {
                        AddMember(InventoryStock);
                    }
                });
                break;
        }
    }


    public void AddMember(String mapstr){
        Log.d("print", "AddMember: "+mapstr);
        showLoading(MemberManageActivity.this,"数据上传...");
        String uri=SysUtils.getSellerpinbanServiceUrl("add_members");
        Map<String,String> map = new HashMap<String,String>();
        map.put("map",mapstr);
        CustomRequest r =new CustomRequest(Request.Method.POST, uri, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(response);
                    System.out.println("ret=" + ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = null;
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
            }
        });
        executeRequest(r);
    }


}
