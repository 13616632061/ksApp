package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.ChartView.IndexHorizontalScrollView;
import com.ui.ChartView.NearlySevenDayView;
import com.ui.ChartView.NearlyThirtyDayView;
import com.ui.ChartView.Today24HourView;
import com.ui.util.CustomRequest;
import com.ui.util.DateUtils;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Goods_Sales_ChartStatisticsAcitvity extends BaseActivity implements View.OnClickListener {


    private IndexHorizontalScrollView indexHorizontalScrollView,indexHorizontalScrollView_money,indexHorizontalScrollView_profit;
    private Today24HourView today24HourView,today24HourView_money,today24HourView_profit;
    private IndexHorizontalScrollView indexHorizontalScrollView_yesterday,indexHorizontalScrollView_money_yesterday,indexHorizontalScrollView_profit_yesterday;
    private Today24HourView today24HourView_yesterday,today24HourView_money_yesterday,today24HourView_profit_yesterday;
    private IndexHorizontalScrollView indexHorizontalScrollView_sevenday,indexHorizontalScrollView_money_sevenday,indexHorizontalScrollView_profit_sevenday;
    private NearlySevenDayView sevendayView,sevendayView_money,sevendayView_profit;
    private IndexHorizontalScrollView indexHorizontalScrollView_thirtyday,indexHorizontalScrollView_money_thirtyday,indexHorizontalScrollView_profit_thirtyday;
    private NearlyThirtyDayView thirtydayView,thirtydayView_money,thirtydayView_profit;
    private ImageView iv_back;
    private Button btn_today,btn_yesterday,btn_nearly_seven,btn_nearly_thirty;
    private  View view,yesterview,sevenday_view,nearlythirtyday_view;
    private ArrayList<String> today_sales_money;
    private ArrayList<String> today_sales_nums;
    private ArrayList<String> today_sales_profit;
    private int type_day=0;//今日：0，昨日：1，近7日：2，近30日：3；
    private String endtime;
    private String begintime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods__sales__chart_statistics_acitvity);
        SysUtils.setupUI(this,findViewById(R.id.activity_goods__sales__chart_statistics_acitvity));
        initToolbar(this);

        today_sales_money=new ArrayList<>();
        today_sales_nums=new ArrayList<>();
        today_sales_profit=new ArrayList<>();
        initView();
        initData();
    }

    private void initData() {
        getGoodsSalesInfo();
    }

    private void initView() {

        view=findViewById(R.id.todaysalesView);
        indexHorizontalScrollView= (IndexHorizontalScrollView) view.findViewById(R.id.indexHorizontalScrollView);
        indexHorizontalScrollView_money= (IndexHorizontalScrollView) view.findViewById(R.id.indexHorizontalScrollView_money);
        indexHorizontalScrollView_profit= (IndexHorizontalScrollView) view.findViewById(R.id.indexHorizontalScrollView_profit);
        today24HourView= (Today24HourView) view.findViewById(R.id.today24HourView);
        today24HourView_money= (Today24HourView)view. findViewById(R.id.today24HourView_money);
        today24HourView_profit= (Today24HourView) view.findViewById(R.id.today24HourView_profit);
        today24HourView.getdataTodaty_salenum(today_sales_nums);
        today24HourView_money.getdataTodaty_salemoney(today_sales_money);
        today24HourView_profit.getdataTodaty_saleprofit(today_sales_profit);
        indexHorizontalScrollView.setToday24HourView(today24HourView);
        indexHorizontalScrollView_profit.setToday24HourView(today24HourView_profit);
        indexHorizontalScrollView_money.setToday24HourView(today24HourView_money);

        yesterview=findViewById(R.id.yesterdaysalesView);
        indexHorizontalScrollView_yesterday= (IndexHorizontalScrollView) yesterview.findViewById(R.id.indexHorizontalScrollView);
        indexHorizontalScrollView_money_yesterday= (IndexHorizontalScrollView) yesterview.findViewById(R.id.indexHorizontalScrollView_money);
        indexHorizontalScrollView_profit_yesterday= (IndexHorizontalScrollView) yesterview.findViewById(R.id.indexHorizontalScrollView_profit);
        today24HourView_yesterday= (Today24HourView) yesterview.findViewById(R.id.today24HourView);
        today24HourView_money_yesterday= (Today24HourView)yesterview. findViewById(R.id.today24HourView_money);
        today24HourView_profit_yesterday= (Today24HourView) yesterview.findViewById(R.id.today24HourView_profit);
        today24HourView_yesterday.getdataTodaty_salenum(today_sales_nums);
        today24HourView_money_yesterday.getdataTodaty_salemoney(today_sales_money);
        today24HourView_profit_yesterday.getdataTodaty_saleprofit(today_sales_profit);
        indexHorizontalScrollView_yesterday.setToday24HourView(today24HourView_yesterday);
        indexHorizontalScrollView_profit_yesterday.setToday24HourView(today24HourView_profit_yesterday);
        indexHorizontalScrollView_money_yesterday.setToday24HourView(today24HourView_money_yesterday);

        sevenday_view=findViewById(R.id.nearlysevendayView);
        indexHorizontalScrollView_sevenday= (IndexHorizontalScrollView) sevenday_view.findViewById(R.id.indexHorizontalScrollView);
        indexHorizontalScrollView_money_sevenday= (IndexHorizontalScrollView) sevenday_view.findViewById(R.id.indexHorizontalScrollView_money);
        indexHorizontalScrollView_profit_sevenday= (IndexHorizontalScrollView) sevenday_view.findViewById(R.id.indexHorizontalScrollView_profit);
        sevendayView= (NearlySevenDayView) sevenday_view.findViewById(R.id.today24HourView);
        sevendayView_money= (NearlySevenDayView)sevenday_view. findViewById(R.id.today24HourView_money);
        sevendayView_profit= (NearlySevenDayView)sevenday_view.findViewById(R.id.today24HourView_profit);
        sevendayView.getdataSevenDay_salenum(today_sales_nums);
        sevendayView_money.getdataSevenDay_salemoney(today_sales_money);
        sevendayView_profit.getdataSevenDay_saleprofit(today_sales_profit);
        indexHorizontalScrollView_sevenday.setNearlySevenDayView(sevendayView);
        indexHorizontalScrollView_profit_sevenday.setNearlySevenDayView(sevendayView_profit);
        indexHorizontalScrollView_money_sevenday.setNearlySevenDayView(sevendayView_money);

        nearlythirtyday_view=findViewById(R.id.nearlythirtydayView);
        indexHorizontalScrollView_thirtyday= (IndexHorizontalScrollView) nearlythirtyday_view.findViewById(R.id.indexHorizontalScrollView);
        indexHorizontalScrollView_money_thirtyday= (IndexHorizontalScrollView) nearlythirtyday_view.findViewById(R.id.indexHorizontalScrollView_money);
        indexHorizontalScrollView_profit_thirtyday= (IndexHorizontalScrollView) nearlythirtyday_view.findViewById(R.id.indexHorizontalScrollView_profit);
        thirtydayView= (NearlyThirtyDayView) nearlythirtyday_view.findViewById(R.id.today24HourView);
        thirtydayView_money= (NearlyThirtyDayView) nearlythirtyday_view. findViewById(R.id.today24HourView_money);
        thirtydayView_profit= (NearlyThirtyDayView) nearlythirtyday_view.findViewById(R.id.today24HourView_profit);
        thirtydayView.getdataSevenDay_salenum(today_sales_nums);
        thirtydayView_money.getdataSevenDay_salemoney(today_sales_money);
        thirtydayView_profit.getdataSevenDay_saleprofit(today_sales_money);
        indexHorizontalScrollView_thirtyday.setNearlyThirtyDayView(thirtydayView);
        indexHorizontalScrollView_profit_thirtyday.setNearlyThirtyDayView(thirtydayView_profit);
        indexHorizontalScrollView_money_thirtyday.setNearlyThirtyDayView(thirtydayView_money);



        iv_back= (ImageView) findViewById(R.id.iv_back);
        btn_today= (Button) findViewById(R.id.btn_today);
        btn_yesterday= (Button) findViewById(R.id.btn_yesterday);
        btn_nearly_seven= (Button) findViewById(R.id.btn_nearly_seven);
        btn_nearly_thirty= (Button) findViewById(R.id.btn_nearly_thirty);

        iv_back.setOnClickListener(this);
        btn_today.setOnClickListener(this);
        btn_yesterday.setOnClickListener(this);
        btn_nearly_seven.setOnClickListener(this);
        btn_nearly_thirty.setOnClickListener(this);

    }
     private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {
            int type=intent.getIntExtra("type",0);
              if(type==1){

              }
          }
      };
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_today:
                type_day=0;
                selectBtn(R.drawable.btn_corner_orange,"#ffffff",R.drawable.btn_corner_whitle,
                        "#ffff8905",R.drawable.btn_corner_whitle,"#ffff8905",R.drawable.btn_corner_whitle,"#ffff8905");
                getGoodsSalesInfo();
                break;
            case R.id.btn_yesterday:
                type_day=1;
                selectBtn(R.drawable.btn_corner_whitle, "#ffff8905",R.drawable.btn_corner_orange,
                        "#ffffff",R.drawable.btn_corner_whitle,"#ffff8905",R.drawable.btn_corner_whitle,"#ffff8905");
                getGoodsSalesInfo();
                break;
            case R.id.btn_nearly_seven:
                type_day=2;
                endtime=DateUtils.getCurDateYMD();
                begintime= DateUtils.getNearlyDateYMD(endtime.trim(),7);
                selectBtn(R.drawable.btn_corner_whitle, "#ffff8905",R.drawable.btn_corner_whitle,
                        "#ffff8905",R.drawable.btn_corner_orange,"#ffffff",R.drawable.btn_corner_whitle,"#ffff8905");
                getGoodsSalesInfo();
                break;
            case R.id.btn_nearly_thirty:
                type_day=3;
                endtime=DateUtils.getCurDateYMD();
                begintime= DateUtils.getNearlyDateYMD(endtime.trim(),30);
                selectBtn(R.drawable.btn_corner_whitle, "#ffff8905",R.drawable.btn_corner_whitle,
                        "#ffff8905",R.drawable.btn_corner_whitle, "#ffff8905",R.drawable.btn_corner_orange,"#ffffff");
                getGoodsSalesInfo();
                break;
        }

    }

    /**
     * 时间按钮颜色背景的变化
     * @param resid1
     * @param string1
     * @param resid2
     * @param string2
     * @param resid3
     * @param string3
     * @param resid4
     * @param string4
     */
    private void selectBtn(int resid1,String string1,int resid2,String string2,int resid3,String string3,int resid4,String string4){
        btn_today.setBackgroundResource(resid1);
        btn_today.setTextColor(Color.parseColor(string1));
        btn_yesterday.setBackgroundResource(resid2);
        btn_yesterday.setTextColor(Color.parseColor(string2));
        btn_nearly_seven.setBackgroundResource(resid3);
        btn_nearly_seven.setTextColor(Color.parseColor(string3));
        btn_nearly_thirty.setBackgroundResource(resid4);
        btn_nearly_thirty.setTextColor(Color.parseColor(string4));
    }
    private void getGoodsSalesInfo(){
        Map<String,String> map=new HashMap<>();
        if(type_day==0){
            map.put("type",1+"");
            map.put("nowtime", DateUtils.getCurrentTime());
        }else if(type_day==1){
            map.put("type",1+"");
        }else if(type_day==2||type_day==3){
            map.put("type",2+"");
            map.put("begintime",begintime);
            map.put("endtime",endtime);
        }
        System.out.println("map="+map);
        CustomRequest r=new CustomRequest(CustomRequest.Method.POST, SysUtils.getGoodsServiceUrl("graph"),map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("商品销售图表统计ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                    JSONArray data=ret.getJSONArray("data");
                        if(data!=null){
                            today_sales_nums.clear();
                            today_sales_money.clear();
                            today_sales_profit.clear();
                            int total_nums=0;
                            double total_amount=0.00;
                            double total_profit=0.00;
                            if(type_day==0||type_day==1) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsonObject1 = data.getJSONObject(i);
                                    String amount = jsonObject1.getString("amount");
                                    String nums = jsonObject1.getString("nums");
                                    String profit = jsonObject1.getString("profit");
                                    total_nums += Integer.parseInt(nums);
                                    total_amount += Double.parseDouble(amount);
                                    total_profit += Double.parseDouble(profit);
                                    today_sales_nums.add(total_nums + "");
                                    today_sales_money.add(SetEditTextInput.stringpointtwo(total_amount + ""));
                                    today_sales_profit.add(SetEditTextInput.stringpointtwo(total_profit + ""));

                                }
                            }else if(type_day==2||type_day==3){
                                for (int i = data.length()-1;i>0;i--) {
                                    JSONObject jsonObject1 = data.getJSONObject(i);
                                    String amount = jsonObject1.getString("amount");
                                    String nums = jsonObject1.getString("nums");
                                    String profit = jsonObject1.getString("profit");
                                    today_sales_nums.add(nums);
                                    today_sales_money.add(SetEditTextInput.stringpointtwo(amount));
                                    today_sales_profit.add(SetEditTextInput.stringpointtwo(profit));

                                }
                            }
                            if(type_day==0){
                                view.setVisibility(View.VISIBLE);
                                yesterview.setVisibility(View.GONE);
                                sevenday_view.setVisibility(View.GONE);
                                nearlythirtyday_view.setVisibility(View.GONE);
                                today24HourView.getdataTodaty_salenum(today_sales_nums);
                                today24HourView_money.getdataTodaty_salemoney(today_sales_money);
                                today24HourView_profit.getdataTodaty_saleprofit(today_sales_profit);
                                indexHorizontalScrollView.setToday24HourView(today24HourView);
                                indexHorizontalScrollView_profit.setToday24HourView(today24HourView_profit);
                                indexHorizontalScrollView_money.setToday24HourView(today24HourView_money);

                            }else if(type_day==1){
                                view.setVisibility(View.GONE);
                                yesterview.setVisibility(View.VISIBLE);
                                sevenday_view.setVisibility(View.GONE);
                                nearlythirtyday_view.setVisibility(View.GONE);
                                today24HourView_yesterday.getdataTodaty_salenum(today_sales_nums);
                                today24HourView_money_yesterday.getdataTodaty_salemoney(today_sales_money);
                                today24HourView_profit_yesterday.getdataTodaty_saleprofit(today_sales_profit);
                                indexHorizontalScrollView_yesterday.setToday24HourView(today24HourView_yesterday);
                                indexHorizontalScrollView_profit_yesterday.setToday24HourView(today24HourView_profit_yesterday);
                                indexHorizontalScrollView_money_yesterday.setToday24HourView(today24HourView_money_yesterday);
                            }else if(type_day==2){
                                view.setVisibility(View.GONE);
                                yesterview.setVisibility(View.GONE);
                                sevenday_view.setVisibility(View.VISIBLE);
                                nearlythirtyday_view.setVisibility(View.GONE);
                                sevendayView.getdataSevenDay_salenum(today_sales_nums);
                                sevendayView_money.getdataSevenDay_salemoney(today_sales_money);
                                sevendayView_profit.getdataSevenDay_saleprofit(today_sales_profit);
                                indexHorizontalScrollView_sevenday.setNearlySevenDayView(sevendayView);
                                indexHorizontalScrollView_profit_sevenday.setNearlySevenDayView(sevendayView_profit);
                                indexHorizontalScrollView_money_sevenday.setNearlySevenDayView(sevendayView_money);
                            }else if(type_day==3){
                                view.setVisibility(View.GONE);
                                yesterview.setVisibility(View.GONE);
                                sevenday_view.setVisibility(View.GONE);
                                nearlythirtyday_view.setVisibility(View.VISIBLE);
                                thirtydayView.getdataSevenDay_salenum(today_sales_nums);
                                thirtydayView_money.getdataSevenDay_salemoney(today_sales_money);
                                thirtydayView_profit.getdataSevenDay_saleprofit(today_sales_profit);
                                indexHorizontalScrollView_thirtyday.setNearlyThirtyDayView(thirtydayView);
                                indexHorizontalScrollView_profit_thirtyday.setNearlyThirtyDayView(thirtydayView_profit);
                                indexHorizontalScrollView_money_thirtyday.setNearlyThirtyDayView(thirtydayView_money);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SysUtils.showNetworkError();
                hideLoading();
            }

        });
        executeRequest(r);
        showLoading(Goods_Sales_ChartStatisticsAcitvity.this);

    }
}
