package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.adapter.MonthAllOrderAdapter;
import com.ui.entity.MonthDay_Order;
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

public class ThisMonthAllOrderActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private TextView tv_date,tv_order_date,tv_week,tv_order_total_money,tv_order_total_num;
    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;
    private PagingListView lv_content_thismonthorder;
    private SwipeRefreshLayout refresh_header;
    private RelativeLayout order_total_statistics_layout;

    private  int total=0;//总订单数，初始值为0
    private  String datetime;//总订单数，初始值为0
    private double total_money;//总订单的金额
    private DecimalFormat df   = new DecimalFormat("######0.00");//小数点两位

    private int PAGE = 1;//页数
    private int NUM_PER_PAGE = 10;//加载的条数
    private int my_position=1;//1表示全部订单
    private boolean hasInit = false;
    boolean loadingMore = false;//是否加载更多
    public ArrayList<MonthDay_Order> Order_list;
    private MonthAllOrderAdapter mMonthAllOrderAdapter;
    private Map<String,String> map;
    private String date_begin;
    private String date_end;

    private int type;
    private String url;
    private String account_id;//分店id
    private int paytype;//移动支付还是现金类型;//移动支付还是现金类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_this_month_all_order);

        SysUtils.setupUI(ThisMonthAllOrderActivity.this,findViewById(R.id.activity_this_month_all_order));
        initToolbar(this);
        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){
          type= bundle.getInt("type");
            account_id=bundle.getString("account_id");
            date_begin=bundle.getString("date_begin");
            date_end=bundle.getString("date_end");
            paytype=bundle.getInt("paytype");
        }

        initView();
        initData();
    }

    private void initData() {
        Order_list=new ArrayList<MonthDay_Order>();
        loadFirst();
//        refresh_header.post(new Runnable() {
//            @Override
//            public void run() {
//                setRefreshing(true);
//                loadFirst();
//            }
//        });

        //        订单列表为空的显示
        load_tv_noresult.setText("订单列表为空");
        load_tv_noresult.setCompoundDrawablesWithIntrinsicBounds(
                0, //left
                R.drawable.icon_no_result_melt, //top
                0, //right
                0//bottom
        );
//          上拉刷新
        lv_content_thismonthorder.setPagingableListener(new PagingListView.Pagingable() {
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
        lv_content_thismonthorder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("print","打印点击时间"+type);
                Intent intent=new Intent(ThisMonthAllOrderActivity.this,ThisMonthDayOrderActivity.class);
                intent.putExtra("type",type);
                intent.putExtra("paytype",paytype);
                intent.putExtra("datetime",Order_list.get(position).getOrder_date());
                startActivity(intent);
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
        order_total_statistics_layout = (RelativeLayout) findViewById(R.id.order_total_statistics_layout);

        tv_date= (TextView) findViewById(R.id.tv_date);
        tv_date.setVisibility(View.GONE);
        tv_order_date= (TextView) findViewById(R.id.tv_order_date);
        tv_week= (TextView) findViewById(R.id.tv_week);
        tv_week.setVisibility(View.VISIBLE);
        tv_order_total_money= (TextView) findViewById(R.id.tv_order_total_money);
        tv_order_total_num= (TextView) findViewById(R.id.tv_order_total_num);

        lv_content_thismonthorder= (PagingListView) findViewById(R.id.lv_content_thismonthorder);
        refresh_header= (SwipeRefreshLayout) findViewById(R.id.refresh_header);
        //下拉刷新
        refresh_header.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);

        refresh_header.setOnRefreshListener(this);
        load_btn_refresh_net.setOnClickListener(this);

    }
    /**
     *设置统计条数据显示
     */
    private  void setshowTotalstistics(){
        if(type!=5){
            if(datetime.length()>0) {
                tv_order_date.setText(datetime.substring(0, 4) + getString(R.string.pickerview_year) + datetime.substring(5, 7) + getString(R.string.pickerview_month));
            }

        }else {
            tv_order_date.setText(date_begin.substring(0,10)+"-"+date_end.subSequence(0,10));
        }
        if(total>0) {
            tv_order_total_num.setText(total + "");
            tv_order_total_money.setText("￥" + total_money);
        }else {
            tv_order_total_num.setText(total + "");
            tv_order_total_money.setText("￥" + df.format(0.00));
        }
    }
//    是否下拉刷新
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
            if(Order_list.size() < 1) {
                //没有结果
                lv_content_thismonthorder.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                include_noresult.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            } else {
                //有结果
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                layout_err.setVisibility(View.GONE);
                lv_content_thismonthorder.setVisibility(View.VISIBLE);
            }
//        }
    }
    //是否有网络
    private void setNoNetwork() {
        //网络不通
        if( Order_list.size() < 1) {
            if(!include_nowifi.isShown()) {
                lv_content_thismonthorder.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                order_total_statistics_layout.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            }
        } else {
            SysUtils.showNetworkError();
        }
    }

    @Override
    public void onRefresh() {
        setRefreshing(false);
//        loadFirst();
    }
    //    更新适配器
    private void updateAdapter() {
        lv_content_thismonthorder.onFinishLoading(loadingMore, null);

        if(mMonthAllOrderAdapter == null) {
            mMonthAllOrderAdapter = new MonthAllOrderAdapter(ThisMonthAllOrderActivity.this,Order_list);
            lv_content_thismonthorder.setAdapter(mMonthAllOrderAdapter);

        } else {
            mMonthAllOrderAdapter.notifyDataSetChanged();
        }
    }
    //    数据加载方法
    private void loadData() {
        if(type==3){
            setToolbarTitle(getString(R.string.turnover_of_this_month));//本月营业额
            map = new HashMap<String,String>();
            map.put("page", String.valueOf(PAGE));
            map.put("pagelimit", String.valueOf(NUM_PER_PAGE));
            map.put("date", DateUtils.getMonthDate(0));
            System.out.println("map="+map);
            if(paytype==2){
                url=SysUtils.getSellerServiceUrl("mon_order_cash");
            }else if (paytype==3){
                map.put("tag","member");
                url=SysUtils.getSellerServiceUrl("mon_order");
            }else {
                url=SysUtils.getSellerServiceUrl("mon_order");
            }
        }else if (type==4){
            setToolbarTitle(getString(R.string.turnover_of_last_month));//上月营业额
            map = new HashMap<String,String>();
            map.put("page", String.valueOf(PAGE));
            map.put("pagelimit", String.valueOf(NUM_PER_PAGE));
            map.put("date", DateUtils.getMonthDate(-1));//-1表示s
            if(paytype==2){
                url=SysUtils.getSellerServiceUrl("mon_order_cash");
            }else if (paytype==3){
                map.put("tag","member");
                url=SysUtils.getSellerServiceUrl("mon_order");
            }else {
                url=SysUtils.getSellerServiceUrl("mon_order");
            }
        }else if(type==5){
            setToolbarTitle(getString(R.string.title_activity_search));//查询结果
            map = new HashMap<String,String>();
            map.put("page", String.valueOf(PAGE));
            map.put("pagelimit", String.valueOf(NUM_PER_PAGE));
            map.put("date_begin",date_begin);
            map.put("date_end",date_end);
            System.out.println("map="+map);
            url=SysUtils.getSellerServiceUrl("order_count");
        }else if(type==6){
            setToolbarTitle(getString(R.string.turnover_of_this_month));//本月营业额
            map = new HashMap<String,String>();
            map.put("page", String.valueOf(PAGE));
            map.put("pagelimit", String.valueOf(NUM_PER_PAGE));
            map.put("date", DateUtils.getMonthDate(0));
            map.put("account_id", account_id);
            url=SysUtils.getSellerServiceUrl("mon_order");
        }else if (type==7){
            setToolbarTitle(getString(R.string.turnover_of_last_month));//上月营业额
            map = new HashMap<String,String>();
            map.put("page", String.valueOf(PAGE));
            map.put("pagelimit", String.valueOf(NUM_PER_PAGE));
            map.put("date", DateUtils.getMonthDate(-1));//1表示已支付
            map.put("account_id", account_id);
            url=SysUtils.getSellerServiceUrl("mon_order");
        }
        CustomRequest r = new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                setRefreshing(false);
                hideLoading();

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("今日全部订单："+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
//                        if(status.equals("E.401")){
//                            setView();
//                        }
                    } else {
                        if(PAGE <= 1) {
                            Order_list.clear();
                        }
                        JSONObject dataObject = ret.getJSONObject("data");
                        JSONArray array = dataObject.getJSONArray("orders_info");
                        total = dataObject.getInt("total");
                        total_money=dataObject.getDouble("total_money");
                        if(type!=5){
                        datetime=dataObject.getString("datetime");
                        }
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject data = array.optJSONObject(i);
                                MonthDay_Order monthDay_order=new MonthDay_Order(data.getString("date"),
                                        data.getString("total_money_day"),data.getString("total_day"));
                                Order_list.add(monthDay_order);
                            }
                        }
                        int mtotalPage=dataObject.getInt("totalpage");
                        int totalPage = (int)Math.ceil((float)mtotalPage / NUM_PER_PAGE);
                        loadingMore = totalPage > PAGE;
                        if (loadingMore) {
                            PAGE++;
                        }
                        setView();
                        setshowTotalstistics();
                    }
                    setView();
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
                lv_content_thismonthorder.setIsLoading(false);
                setNoNetwork();
            }
        });

        executeRequest(r);
        showLoading(ThisMonthAllOrderActivity.this,getString(R.string.str92));
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
}
