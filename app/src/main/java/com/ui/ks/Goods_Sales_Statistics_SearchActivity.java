package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.entity.Goods_Sales;
import com.ui.global.Global;
import com.library.utils.BigDecimalArith;
import com.ui.ks.GoodsSearch.GoodsInfoSearchActivity;
import com.ui.ks.SalesStatistics.SalesStatisticsAcitvity;
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

public class Goods_Sales_Statistics_SearchActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back,iv_search;
    private Button btn_nearly_seven,btn_nearly_thirty,btn_reset,btn_sure;
    private RelativeLayout layout_set_starttime,layout_set_endtime,layout_goods_sort;
    private TextView tv_starttime,tv_endtime,tv_goods_sort,tv_clear_time;
    private String goodsort_name,goodsort_id;
    private ArrayList<Goods_Sales> goods_sales_list;
    private  String img_src;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods__sales__statistics__search);
        SysUtils.setupUI(this,findViewById(R.id.activity_goods__sales__statistics__search));
        initToolbar(this);

        initView();
        initData();
    }

    private void initData() {
        tv_endtime.setText(DateUtils.getCurDate());
    }

    private void initView() {
        iv_back= (ImageView) findViewById(R.id.iv_back);
        iv_search= (ImageView) findViewById(R.id.iv_search);
        btn_nearly_seven= (Button) findViewById(R.id.btn_nearly_seven);
        btn_nearly_thirty= (Button) findViewById(R.id.btn_nearly_thirty);
        btn_reset= (Button) findViewById(R.id.btn_reset);
        btn_sure= (Button) findViewById(R.id.btn_sure);
        layout_set_starttime= (RelativeLayout) findViewById(R.id.layout_set_starttime);
        layout_set_endtime= (RelativeLayout) findViewById(R.id.layout_set_endtime);
        layout_goods_sort= (RelativeLayout) findViewById(R.id.layout_goods_sort);
        tv_starttime= (TextView) findViewById(R.id.tv_starttime);
        tv_endtime= (TextView) findViewById(R.id.tv_endtime);
        tv_goods_sort= (TextView) findViewById(R.id.tv_goods_sort);
        tv_clear_time= (TextView) findViewById(R.id.tv_clear_time);

        iv_back.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        btn_nearly_seven.setOnClickListener(this);
        btn_nearly_thirty.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        layout_set_starttime.setOnClickListener(this);
        layout_set_endtime.setOnClickListener(this);
        layout_goods_sort.setOnClickListener(this);
        tv_clear_time.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                Intent intent=new Intent(Goods_Sales_Statistics_SearchActivity.this,GoodsInfoSearchActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                break;
            case R.id.tv_clear_time:
                tv_starttime.setText("");
                tv_endtime.setText("");
                break;
            case R.id.btn_nearly_seven:
                String str_nearly_seven= DateUtils.getNearlyDate(tv_endtime.getText().toString().trim(),7);
                tv_starttime.setText(str_nearly_seven);
                break;
            case R.id.btn_nearly_thirty:
                String str_nearly_thirty= DateUtils.getNearlyDate(tv_endtime.getText().toString().trim(),30);
                tv_starttime.setText(str_nearly_thirty);
                break;
            case R.id.btn_reset:
                tv_goods_sort.setText("");
                tv_starttime.setText("");
                tv_endtime.setText(DateUtils.getCurDate());
                break;
            case R.id.btn_sure:
                getGoodsSalesInfo();
                break;
            case R.id.layout_set_starttime:
                DateUtils.runTime(Goods_Sales_Statistics_SearchActivity.this,tv_starttime);
                break;
            case R.id.layout_set_endtime:
                DateUtils.runTime(Goods_Sales_Statistics_SearchActivity.this,tv_endtime);
                break;
            case R.id.layout_goods_sort:
                Intent good_typeintent=new Intent(Goods_Sales_Statistics_SearchActivity.this,SortManagementActivity.class);
                good_typeintent.putExtra("type",2);
                startActivityForResult(good_typeintent,202);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==202&&resultCode==204){
            Bundle bundle=data.getExtras();
            goodsort_name=bundle.getString("goodtype");
            goodsort_id=bundle.getString("goodtype_id");
            tv_goods_sort.setText(goodsort_name);
        }
    }
    private void getGoodsSalesInfo(){
        String beginTime=tv_starttime.getText().toString().trim();
        String endTime=tv_endtime.getText().toString().trim();
        if(TextUtils.isEmpty(beginTime)){
            Toast.makeText(Goods_Sales_Statistics_SearchActivity.this,"开始时间不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(endTime)){
            Toast.makeText(Goods_Sales_Statistics_SearchActivity.this,"结束时间不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String,String> map=new HashMap<>();
        if(TextUtils.isEmpty(goodsort_id)){
            Toast.makeText(Goods_Sales_Statistics_SearchActivity.this,"商品分类不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        map.put("tag_id",goodsort_id);
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        System.out.println("map="+map);
        CustomRequest r=new CustomRequest(CustomRequest.Method.POST, SysUtils.getGoodsServiceUrl("count_goods"),map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("商品搜索销售统计ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        JSONArray data=ret.getJSONArray("data");
                        goods_sales_list=new ArrayList<>();
                        for(int i=0;i<data.length();i++){
                            JSONObject jsonObject1=data.getJSONObject(i);
                            String goods_id=jsonObject1.getString("goods_id");
                            String goods_name=jsonObject1.getString("goods_name");
                            String cost=jsonObject1.getString("cost");
                            img_src=jsonObject1.getString("img_src");
                            double total=jsonObject1.getDouble("total");
                            int nums=jsonObject1.getInt("nums");
                            double total_cost=Double.parseDouble(SetEditTextInput.stringpointtwo(cost));
                            total_cost= BigDecimalArith.mul(Double.parseDouble(cost),nums);
                            double profit=BigDecimalArith.sub(total,total_cost);
                            double profit_percent=BigDecimalArith.div(profit,total,2);

                            Goods_Sales goods_sales=new Goods_Sales(goods_name,nums,total,profit,SetEditTextInput.stringpointtwo((profit_percent*100)+"")+"%",img_src);
                            goods_sales_list.add(goods_sales);

                        }
                        Goods_Sales_Statistics_SearchActivity.this.sendBroadcast(new Intent(Global.BROADCAST_Goods_Sales_StatisticsAcitvity_ACTION));
                        Intent intent=new Intent(Goods_Sales_Statistics_SearchActivity.this,SalesStatisticsAcitvity.class);
                        intent.putExtra("type",2);
                        intent.putParcelableArrayListExtra("goods_sales_list",goods_sales_list);
                        startActivity(intent);
                        finish();
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
        showLoading(Goods_Sales_Statistics_SearchActivity.this);

    }
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("关闭");
            finish();

        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        Goods_Sales_Statistics_SearchActivity.this.registerReceiver(broadcastReceiver,new IntentFilter(Global.BROADCAST_Goods_Sales_Statistics_SearchActivity_ACTION));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Goods_Sales_Statistics_SearchActivity.this.unregisterReceiver(broadcastReceiver);
    }
}
