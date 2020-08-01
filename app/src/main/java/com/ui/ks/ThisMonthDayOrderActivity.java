package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.adapter.TodayAllOrderAdapter;
import com.ui.entity.Order;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.DateUtils;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThisMonthDayOrderActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private TextView tv_date,tv_order_date,tv_week,tv_order_total_money,tv_order_total_num;
    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;
    private PagingListView lv_content_month_dayorder;
    private SwipeRefreshLayout refresh_header;

    private  int total=0;//总订单数，初始值为0
    private double total_money;//总订单的金额
    private DecimalFormat df   = new DecimalFormat("######0.00");//小数点两位

    private int PAGE = 1;//页数
    private int NUM_PER_PAGE = 10;//加载的条数
    private int my_position=1;//1表示全部订单
    private boolean hasInit = false;
    boolean loadingMore = false;//是否加载更多
    public ArrayList<Order> Order_list;
    private TodayAllOrderAdapter mTodayAllOrderAdapter;

    private int type;
    private String datetime;
    private int paytype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_this_month_day_order);

        SysUtils.setupUI(this,findViewById(R.id.activity_this_month_day_order));
        initToolbar(this);
        Intent intent=getIntent();
        if(intent!=null){
            type=intent.getIntExtra("type",1);
            paytype=intent.getIntExtra("paytype",0);
            datetime=intent.getStringExtra("datetime");
            if (type==1){
                setToolbarTitle(getString(R.string.turnover_of_this_month));//本月营业额
            }else if(type==2){
                setToolbarTitle(getString(R.string.turnover_of_last_month));//上月营业额
            }else if(type==5){
                setToolbarTitle(getString(R.string.title_activity_search));//查询结果
            }

        }

        initView();
        initData();
    }
    private void initData() {
        Order_list=new ArrayList<Order>();
        loadFirst();
        //        订单列表为空的显示
        load_tv_noresult.setText("订单列表为空");
        load_tv_noresult.setCompoundDrawablesWithIntrinsicBounds(
                0, //left
                R.drawable.icon_no_result_melt, //top
                0, //right
                0//bottom
        );
//        refresh_header.post(new Runnable() {
//            @Override
//            public void run() {
//                setRefreshing(true);
//                loadFirst();
//            }
//        });
//        订单列表数据
//       mTodayAllOrderAdapter=new TodayAllOrderAdapter(TodayAllOrderActivity.this,Order_list);
//        lv_content_todayorder.setAdapter(mTodayAllOrderAdapter);
//          上拉刷新
        lv_content_month_dayorder.setPagingableListener(new PagingListView.Pagingable() {
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
        //点击
        lv_content_month_dayorder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int actualPosition = position - lv_content_month_dayorder.getHeaderViewsCount();
                if (actualPosition >= 0 && actualPosition < Order_list.size()) {
                    //点击
                    Order data = Order_list.get(actualPosition);

                    Bundle bundle = new Bundle();
                    bundle.putString("order_id", data.getOrderSn());
                    SysUtils.startAct(ThisMonthDayOrderActivity.this, new OrderDetailActivity(), bundle);
                }


            }
        });
    }

    private void initView() {
        layout_err= findViewById(R.id.layout_err);
        include_nowifi=  layout_err.findViewById(R.id.include_nowifi);
        include_noresult=  layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_btn_refresh_net = (Button) include_nowifi.findViewById(R.id.load_btn_refresh_net);

        tv_date= (TextView) findViewById(R.id.tv_date);
        tv_date.setVisibility(View.GONE);
        tv_order_date= (TextView) findViewById(R.id.tv_order_date);
        tv_week= (TextView) findViewById(R.id.tv_week);
        tv_week.setVisibility(View.VISIBLE);
        tv_order_total_money= (TextView) findViewById(R.id.tv_order_total_money);
        tv_order_total_num= (TextView) findViewById(R.id.tv_order_total_num);

        lv_content_month_dayorder= (PagingListView) findViewById(R.id.lv_content_month_dayorder);
        refresh_header= (SwipeRefreshLayout) findViewById(R.id.refresh_header);
        refresh_header.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);

        refresh_header.setOnRefreshListener(this);
        load_btn_refresh_net.setOnClickListener(this);

    }

    @Override
    public void onRefresh() {
        setRefreshing(false);
//        loadFirst();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.load_btn_refresh_net:
                //重新加载数据
                loadFirst();
                break;
        }
    }
    //是否下拉刷新
    private void setRefreshing(boolean refreshing) {
        refresh_header.setRefreshing(refreshing);
    }
    //第一次加载
    private void loadFirst() {
        PAGE = 1;
        loadData();
    }
    //是否有数据返回
    private void setView() {
        if(PAGE <= 1) {
//            Log.v("ks", "size: " + cat_list.size());
            if(Order_list.size() < 1) {
                //没有结果
                lv_content_month_dayorder.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                include_noresult.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            } else {
                //有结果
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                layout_err.setVisibility(View.GONE);
                lv_content_month_dayorder.setVisibility(View.VISIBLE);
            }
        }
    }
    //是否有网络
    private void setNoNetwork() {
        //网络不通
        if(PAGE <= 1 && Order_list.size() < 1) {
            if(!include_nowifi.isShown()) {
                lv_content_month_dayorder.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            }
        } else {
            SysUtils.showNetworkError();
        }
    }
    /**
     *设置统计条数据显示
     */
    private  void setshowTotalstistics(){
        tv_order_date.setText(datetime + "");
        tv_week.setText(DateUtils.friendly_time2(this,datetime));
        if(total>0) {
            tv_order_total_num.setText(total + "");
            tv_order_total_money.setText("￥" + df.format(total_money));
        }else {
            tv_order_total_num.setText(total + "");
            tv_order_total_money.setText("￥" + df.format(0.00));
        }
    }
    //    数据加载方法
    private void loadData() {
        Map<String,String> map = new HashMap<String,String>();
        map.put("page", String.valueOf(PAGE));
        map.put("pagelimit", String.valueOf(NUM_PER_PAGE));
        map.put("type", String.valueOf(my_position));
        map.put("pay", String.valueOf(1));//1表示已支付
        map.put("date",datetime);
        String url;
        System.out.println("paytype="+paytype);
        if(paytype==2){
            url="yesterday_cash";
        }else if(type==5){
            url="details";
        }else {
            if (paytype==3){
                map.put("tag","member");
            }
            url="yesterday";
        }
        Log.d("print","打印出来的数据为"+map.toString());
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl(url), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                setRefreshing(false);
                hideLoading();

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("全部订单："+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        if(PAGE <= 1) {
                            Order_list.clear();
                        }

                        JSONArray array = dataObject.getJSONArray("orders_info");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject data = array.optJSONObject(i);

                                Order b = SysUtils.getOrderRow(data);

                                Order_list.add(b);
                            }
                        }
                        setView();
                        setshowTotalstistics();
                    }

                    if(!hasInit) {
                        hasInit = true;
                    }


                    total = dataObject.getInt("total");
                    datetime=dataObject.getString("datetime");
                    total_money=dataObject.getDouble("total_money");
                    setshowTotalstistics();
                    int totalPage = (int)Math.ceil((float)total / NUM_PER_PAGE);
                    loadingMore = totalPage > PAGE;

                    setView();

                    if (loadingMore) {
                        PAGE++;
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    updateAdapter();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                setRefreshing(false);
                lv_content_month_dayorder.setIsLoading(false);
                setNoNetwork();
            }
        });

        executeRequest(r);
        System.out.println("rr="+r);
        showLoading(ThisMonthDayOrderActivity.this,getString(R.string.str92));
    }
    //    更新适配器
    private void updateAdapter() {
        lv_content_month_dayorder.onFinishLoading(loadingMore, null);

        if(mTodayAllOrderAdapter == null) {
            mTodayAllOrderAdapter = new TodayAllOrderAdapter(ThisMonthDayOrderActivity.this,Order_list,type);
            lv_content_month_dayorder.setAdapter( mTodayAllOrderAdapter);

        } else {
            mTodayAllOrderAdapter.notifyDataSetChanged();
        }
    }
}
