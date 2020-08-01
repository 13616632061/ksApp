package com.ui.ks;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.adapter.GoodSortEditAdapter;
import com.ui.entity.GoodSort;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 分类编辑页面
 */
public class SortEditActivity extends BaseActivity implements PagingListView.Pagingable {

    private PagingListView  list_goodsortedit;
    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;

    private int total=0;
    private int page=1;
    private int NUM_PER_PAGE = 20;//加载的条数
    private boolean loadingMore = false;//是否加载更多
    private ArrayList<GoodSort> goodsortList;
    private GoodSortEditAdapter goodSortEditAdapter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getSortlist();
            updateAdapter();
        }
    };
    View ContentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentView=View.inflate(SortEditActivity.this,R.layout.activity_sort_edit,null);
        setContentView(ContentView);

        SysUtils.setupUI(this,findViewById(R.id.activity_sort_edit));
        initToolbar(this);

        initView();
        initData();
    }

    private void initData() {
        goodsortList=new ArrayList<>();
        getSortlist();
    }

    private void initView() {
        layout_err = (View)findViewById(R.id.layout_err);
        include_noresult = layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_tv_noresult.setText("没有数据记录哦");
        load_tv_noresult.setCompoundDrawablesWithIntrinsicBounds(
                0, //left
                R.drawable.icon_no_result_melt, //top
                0, //right
                0//bottom
        );
        include_nowifi = layout_err.findViewById(R.id.include_nowifi);
        load_btn_refresh_net = (Button) include_nowifi.findViewById(R.id.load_btn_refresh_net);
        load_btn_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //重新加载数据
                getSortlist();
            }
        });
        list_goodsortedit= (PagingListView) findViewById(R.id.list_goodsortedit);
        list_goodsortedit.setPagingableListener(this);
    }
    /**
     * 获取分类信息
     */
    private  void getSortlist(){
        Map<String,String> map=new HashMap<String, String>();
        map.put("page",page+"");
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("cat_getlist"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("分类ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject  data=null;
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        if(page<=1){
                            goodsortList.clear();
                        }
                        data=ret.getJSONObject("data");
                        JSONArray arry=data.getJSONArray("nav_info");
                        if(arry!=null&&arry.length()>0){
                            for(int i=0;i<arry.length();i++){
                                JSONObject jsonObject1=arry.getJSONObject(i);
                                GoodSort goodSort=new GoodSort(jsonObject1.getString("tag_name"),jsonObject1.getString("tag_id"),0);
                                goodsortList.add(goodSort);
                            }
                        }
                        total=data.getInt("total");
                        int totalpage= (int) Math.ceil((float)total/NUM_PER_PAGE);
                        loadingMore=totalpage>page;
                        if(loadingMore){
                            page++;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    setView();
                    updateAdapter();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                SysUtils.showNetworkError();
                setNoNetwork();

            }
        });
        executeRequest(customRequest);
        showLoading(SortEditActivity.this);
    }
    private void updateAdapter() {
//        list_goodsortedit.onFinishLoading(loadingMore, null);

        if(goodSortEditAdapter== null) {
            goodSortEditAdapter = new GoodSortEditAdapter(SortEditActivity.this,goodsortList,handler);
            list_goodsortedit.setAdapter( goodSortEditAdapter);

        } else {
            goodSortEditAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onLoadMoreItems() {
        if (loadingMore) {
            //加载更多
            getSortlist();
        } else {
            updateAdapter();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_UP){
            if(goodSortEditAdapter!=null){
                goodSortEditAdapter.istonch(true);
            }
        }
        return true;
    }
    private void setView() {
        if(goodsortList.size() < 1) {
            //没有结果
            list_goodsortedit.setVisibility(View.GONE);
            include_nowifi.setVisibility(View.GONE);
            include_noresult.setVisibility(View.VISIBLE);
            layout_err.setVisibility(View.VISIBLE);
        } else {
            //有结果
            include_noresult.setVisibility(View.GONE);
            include_nowifi.setVisibility(View.GONE);
            layout_err.setVisibility(View.GONE);
            list_goodsortedit.setVisibility(View.VISIBLE);
        }
    }

    private void setNoNetwork() {
        //网络不通
        if (goodsortList.size() < 1) {
            if (!include_nowifi.isShown()) {
                list_goodsortedit.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            }
        } else {
            SysUtils.showNetworkError();
        }
    }
}
