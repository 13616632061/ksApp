package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.adapter.OutofstorageAdapter;
import com.ui.entity.outofstoragelist;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/5/31.
 */

public class Out_In_Activity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private PagingListView lv_content;
    private SwipeRefreshLayout refresh_header;
    private String date_begin="";
    private String date_end="";
    public List<outofstoragelist> out_in=new ArrayList<>();
    public List<outofstoragelist> out_in_page;
//    List<outofstoragelist.DataBean.TotalBean> totalBean;
//    List<outofstoragelist.DataBean.ListBean> listBean;

    private OutofstorageAdapter adapter;
    public int totalnums=0;
    public int page = 1;
    public boolean paging1 = false;
    public RelativeLayout order_total_statistics_layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initToolbar(this);

        out_in_page=new ArrayList<>();
        lv_content= (PagingListView) findViewById(R.id.lv_content);

        refresh_header= (SwipeRefreshLayout) findViewById(R.id.refresh_header);
        refresh_header.setOnRefreshListener(this);
//        order_total_statistics_layout= (RelativeLayout) findViewById(R.id.order_total_statistics_layout);
//        order_total_statistics_layout.setVisibility(View.GONE);

        final Intent intent=getIntent();
        if (intent!=null){
            date_begin=intent.getStringExtra("date_begin");
            date_end=intent.getStringExtra("date_end");
            getdate();
        }


        lv_content.onFinishLoading(true, null);

        lv_content.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if (Integer.valueOf(totalnums) % 8 == 0) {
//                    tv_page.setText(page + "/" + (Integer.valueOf(totalnums) / 8));
                    if (page < (Integer.valueOf(totalnums) / 8)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
                            getdate();
                            paging1 = false;
                        }
                        lv_content.onFinishLoading(true, null);
                    }else {
                        lv_content.onFinishLoading(false, null);
                    }
                } else {
//                    tv_page.setText(page + "/" + (Integer.valueOf(totalnums) / 8 + 1));
                    if (page < (Integer.valueOf(totalnums) / 8 + 1)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
//                            lv_content.onFinishLoading(paging1, null);
                            getdate();
                            paging1 = false;
//                            lv_content.onFinishLoading(paging1, null);
                        }
                        lv_content.onFinishLoading(true, null);
                    }else {
                        lv_content.onFinishLoading(false, null);
                    }
                }
            }
        });

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1=new Intent(Out_In_Activity.this,Out_In_DetailActivity.class);
                intent1.putExtra("order_id",out_in_page.get(i).getId());
                intent1.putExtra("total",out_in_page.get(i).getMoney());
                startActivity(intent1);

            }
        });

    }

    public void getdate() {
        Map<String,String> map = new HashMap<String,String>();
        map.put("page", page+"");
        map.put("starttime", date_begin+":00");
        map.put("endtime", date_end+":00");
        CustomRequest r=new CustomRequest(Request.Method.POST, SysUtils.getSellerpinbanServiceUrl("detail_list"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject ret = SysUtils.didResponse(response);
                try {
                    String status=ret.getString("status");
                    if (status.equals("200")){
                        JSONObject data=ret.getJSONObject("data");
                        JSONArray list=data.getJSONArray("list");
                        JSONArray total=data.getJSONArray("total");
                        totalnums=total.getJSONObject(0).getInt("num");
//                        totalBean.add(totals);
//                        if (out_in!=null){
//                            out_in.getData().setTotal(totalBean);
//                        }
                        for (int i=0;i<list.length();i++){
                            out_in.clear();
                            outofstoragelist totals=new outofstoragelist();
                            JSONObject object=list.getJSONObject(i);
                            totals.setId(object.getString("id"));
                            totals.setCreatetime(object.getString("createtime"));
                            totals.setMoney(object.getString("money"));
                            totals.setNums(object.getString("nums"));
                            totals.setOparator(object.getString("oparator"));
                            totals.setOrder_id(object.getString("order_id"));
                            totals.setRemark(object.getString("remark"));
                            totals.setSeller_id(object.getString("seller_id"));
                            totals.setType(object.getString("type"));
                            out_in_page.add(totals);
                        }
//                        out_in_page.addAll(out_in);
//
                        for (int j=0;j<out_in.size();j++){
                            out_in_page.add(out_in.get(j));
                        }
                        adapter=new OutofstorageAdapter(Out_In_Activity.this,out_in_page);
                        lv_content.setAdapter(adapter);
                        lv_content.setSelection((page-1)*8);
                        lv_content.setIsLoading(false);
                        lv_content.onFinishLoading(true, null);
                    }
//                System.out.println("ret="+ret);
//                Gson gson=new Gson();
//                out_in=gson.fromJson(ret+"",outofstoragelist.class);

//                lv_content.setIsLoading(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lv_content.setIsLoading(false);
            }
        });
        executeRequest(r);
    }

    @Override
    public void onRefresh() {
        refresh_header.setRefreshing(false);
    }
}
