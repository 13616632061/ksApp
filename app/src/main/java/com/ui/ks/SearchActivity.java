package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.MyApplication.KsApplication;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.adapter.TodayAllOrderAdapter;
import com.ui.entity.Order;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mEdit;
    private String mSearchText;
    private PagingListView lv_content;
    private SwipeRefreshLayout refresh_header;
    public ArrayList<Order> cat_list;
    private TodayAllOrderAdapter adapter = null;
    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;

    int PAGE = 1;
    int NUM_PER_PAGE = 10;
    boolean loadingMore = false;

    private int textColor, redColor;
    private String date_begin;
    private String date_end;
    private String order_id="";
    private String money="";
    private String wx="";//微信收款方式
    private String alipay="";//支付宝收款方式
    private String qq="";//QQ钱包收款方式
    private String micro="";//扫描收款方式
    private String cash="";//现金收款方式
    private String member="";//现金收款方式

    private  int total=0;//总订单数，初始值为0
    private  String datetime;//总订单数，初始值为0
    private double total_money;//总订单的金额
    private TextView tv_date,tv_order_date,tv_week,tv_order_total_money,tv_order_total_num;
    private DecimalFormat df   = new DecimalFormat("######0.00");//小数点两位
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SysUtils.setupUI(SearchActivity.this,findViewById(R.id.activity_search));
        initToolbar(this);

        Intent intent=getIntent();
        if(intent!=null){
            date_begin=intent.getStringExtra("date_begin");
            date_end=intent.getStringExtra("date_end");
            order_id=intent.getStringExtra("order_id");
            money=intent.getStringExtra("money");
            wx=intent.getStringExtra("wx");
            alipay=intent.getStringExtra("alipay");
            qq=intent.getStringExtra("qq");
            micro=intent.getStringExtra("micro");
            cash=intent.getStringExtra("cash");
            member=intent.getStringExtra("member");

        }
        refresh_header = (SwipeRefreshLayout) findViewById(R.id.refresh_header);
        refresh_header.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);
        refresh_header.setOnRefreshListener(this);

        doSearch();

        textColor = getResources().getColor(R.color.text_color);
        redColor = getResources().getColor(R.color.red_color);

        layout_err = (View) findViewById(R.id.layout_err);
        include_noresult = layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_tv_noresult.setText("订单搜索结果为空");
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
                doSearch();
            }
        });

        cat_list = new ArrayList<Order>();
        lv_content = (PagingListView) findViewById(R.id.lv_content);
        lv_content.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if (loadingMore) {
                    //加载更多
                    __doSearch();
                } else {
                    updateAdapter();
                }
            }
        });

        //点击
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                int actualPosition = position - lv_content.getHeaderViewsCount();
                if (actualPosition >= 0 && actualPosition < cat_list.size()) {
                    //点击
                    Order data = cat_list.get(actualPosition);

                    Bundle bundle = new Bundle();
                    bundle.putString("order_id", data.getOrderSn());
                    SysUtils.startAct(SearchActivity.this, new OrderDetailActivity(), bundle);
                }
            }
        });

        tv_date= (TextView) findViewById(R.id.tv_date);
        tv_date.setVisibility(View.GONE);
        tv_order_date= (TextView) findViewById(R.id.tv_order_date);
        tv_week= (TextView) findViewById(R.id.tv_week);
        tv_week.setVisibility(View.VISIBLE);
        tv_order_total_money= (TextView) findViewById(R.id.tv_order_total_money);
        tv_order_total_num= (TextView) findViewById(R.id.tv_order_total_num);

    }
    private void setRefreshing(boolean refreshing) {
        refresh_header.setRefreshing(refreshing);
    }

    private void doSearch() {
        PAGE = 1;
        refresh_header.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                __doSearch();
            }
        });
    }

    /**
     *设置统计条数据显示
     */
    private  void setshowTotalstistics(){
            tv_order_date.setText(date_begin.substring(0,10)+"-"+date_end.subSequence(0,10));
        if(total>0) {
            tv_order_total_num.setText(total + "");
            tv_order_total_money.setText("￥" + total_money);
        }else {
            tv_order_total_num.setText(total + "");
            tv_order_total_money.setText("￥" + df.format(0.00));
        }
    }

    private void __doSearch() {
            Map<String,String> map = new HashMap<String,String>();
            map.put("page",String.valueOf(PAGE));
            map.put("pagelimit",String.valueOf(NUM_PER_PAGE));
            map.put("date_begin",date_begin);
            map.put("date_end",date_end);
            map.put("order_id",order_id);
            map.put("money",money);
            map.put("wx",wx);
            map.put("alipay",alipay);
            map.put("qq",qq);
            map.put("micro",micro);
            map.put("cash",cash);
            map.put("is_member",member);

        System.out.println("搜索结果："+map+"    "+ KsApplication.getString("token", ""));
            CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("search_list"), map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    setRefreshing(false);
                    try {
                        JSONObject ret = SysUtils.didResponse(jsonObject);
                        System.out.println("搜索结果："+ret);
                        String status = ret.getString("status");
                        String message = ret.getString("message");
                        JSONObject dataObject = ret.getJSONObject("data");

                        if (!status.equals("200")) {
                            SysUtils.showError(message);
                        } else {
                            if(PAGE <= 1) {
                                cat_list.clear();
                            }
                            total = dataObject.getInt("total");
                            total_money=dataObject.getDouble("total_money");
                            JSONArray array = dataObject.getJSONArray("orders_info");
                            if (array != null && array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject data = array.optJSONObject(i);

                                    Order b = SysUtils.getOrderRow(data);

                                    cat_list.add(b);
                                }
                            }
                        }

                        int total = dataObject.getInt("total");
                        int totalPage = (int)Math.ceil((float)total / NUM_PER_PAGE);
                        loadingMore = totalPage > PAGE;

                        if (loadingMore) {
                            PAGE++;
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    } finally {
                        setView();
                        setshowTotalstistics();

                        updateAdapter();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    setRefreshing(false);
                    lv_content.setIsLoading(false);
                    setNoNetwork();
                }
            });

            executeRequest(r);

    }


    private void updateAdapter() {
        lv_content.onFinishLoading(loadingMore, null);

        if(adapter == null) {
            adapter = new TodayAllOrderAdapter(SearchActivity.this,cat_list,0);
            lv_content.setAdapter(adapter);

        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        PAGE = 1;
        __doSearch();
    }



    private void setView() {
        if(cat_list.size() < 1) {
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

    private void setNoNetwork() {
        //网络不通
        if(!include_nowifi.isShown()) {
            lv_content.setVisibility(View.GONE);
            include_noresult.setVisibility(View.GONE);
            include_nowifi.setVisibility(View.VISIBLE);
            layout_err.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

//        SysUtils.hideSoftKeyboard(SearchActivity.this);
    }
}
