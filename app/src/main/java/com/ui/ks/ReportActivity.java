package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.global.Global;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONObject;

public class ReportActivity extends BaseActivity {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;
    private TextView textView8;
    private ListView lv_content;
    private SwipeRefreshLayout refresh_header;
    private boolean isLoading = false;

    private RelativeLayout relativeLayout3, relativeLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, 2);
        setContentView(R.layout.activity_report);

        initToolbar(this, 1);
//        toolbar.setNavigationIcon(R.drawable.ic_re);

//        View set_tongji_item = (View) findViewById(R.id.set_tongji_item);
//        SysUtils.setLine(set_tongji_item, Global.SET_SINGLE_LINE, "收入统计", R.drawable.icon_tongji_item, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

        lv_content = (ListView) findViewById(R.id.lv_content);
        View reportView = (View) LayoutInflater.from(this).inflate(R.layout.report_main, lv_content, false);

        textView1 = (TextView) reportView.findViewById(R.id.textView1);
        textView2 = (TextView) reportView.findViewById(R.id.textView2);
        textView3 = (TextView) reportView.findViewById(R.id.textView3);
        textView4 = (TextView) reportView.findViewById(R.id.textView4);
        textView5 = (TextView) reportView.findViewById(R.id.textView5);
        textView6 = (TextView) reportView.findViewById(R.id.textView6);
        textView7 = (TextView) reportView.findViewById(R.id.textView7);
        textView8 = (TextView) reportView.findViewById(R.id.textView8);
        lv_content.addHeaderView(reportView);

        relativeLayout3 = (RelativeLayout) reportView.findViewById(R.id.relativeLayout3);
        relativeLayout3.setOnClickListener(a);
        relativeLayout2 = (RelativeLayout) reportView.findViewById(R.id.relativeLayout2);
        relativeLayout2.setOnClickListener(a);

        lv_content.setAdapter(null);

        refresh_header = (SwipeRefreshLayout) findViewById(R.id.refresh_header);
        refresh_header.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);
        refresh_header.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReport();
            }
        });

        loadData();
    }

    private View.OnClickListener a = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SysUtils.startAct(ReportActivity.this, new ReportDetailActivity());
        }
    };

    private void loadData() {
        refresh_header.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                getReport();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_set) {
            //设置
            SysUtils.startAct(ReportActivity.this, new MoneyActivity());
            return true;
        } else if (id == R.id.menu_my) {
            //我的
            SysUtils.startAct(ReportActivity.this, new MemberProfileActivity());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getReport() {
        if (isLoading) {
            return;
        }

        isLoading = true;

        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getMemberServiceUrl("welcome"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                setRefreshing(false);
                isLoading = false;

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        //推荐商家数
                        textView1.setText(dataObject.getString("count_sellers"));
                        //本月收入
                        textView2.setText(SysUtils.getMoneyFormat(dataObject.getDouble("month_income")));
                        //今日收入
                        textView3.setText(SysUtils.getMoneyFormat(dataObject.getDouble("today_income")));
                        //今日购物金
                        textView4.setText(SysUtils.getMoneyFormat(dataObject.getDouble("today_shopping_money")));
                        //昨日收入
                        textView5.setText(SysUtils.getMoneyFormat(dataObject.getDouble("yesterday_income")));
                        //昨日购物金
                        textView6.setText(SysUtils.getMoneyFormat(dataObject.getDouble("yesterday_shopping_money")));
                        //账户余额
                        textView7.setText(SysUtils.getMoneyFormat(dataObject.getDouble("advance")));
                        //账户总金额
                        textView8.setText(SysUtils.getMoneyFormat(dataObject.getDouble("total_shopping_money")));
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setRefreshing(false);
                SysUtils.showNetworkError();
                isLoading = false;
            }
        });

        executeRequest(r);
    }

    private void setRefreshing(boolean refreshing) {
        refresh_header.setRefreshing(refreshing);
    }



    //刷新点赞
    private BroadcastReceiver broadcastLogoutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };


    @Override
    public void onResume() {
        super.onResume();

        registerReceiver(broadcastLogoutReceiver, new IntentFilter(Global.BROADCAST_LOGOUT_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            unregisterReceiver(broadcastLogoutReceiver);
        } catch(Exception e) {

        }
    }
}
