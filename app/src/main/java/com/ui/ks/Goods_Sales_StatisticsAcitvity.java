package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.LineGraphicView.LineGraphicView;
import com.ui.adapter.Goods_Sales_Statistic_list_Adapter;
import com.ui.entity.Goods_Sales;
import com.ui.global.Global;
import com.library.utils.BigDecimalArith;
import com.ui.util.CustomRequest;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ListIterator;

public class Goods_Sales_StatisticsAcitvity extends BaseActivity implements View.OnClickListener {

    private LinearLayout goods_sales_layout,linegraphicview_layout;
    private RelativeLayout layout_goods_sales_money,layout_good_sales_num,layout_goods_profit;
    private ListView goods_sales_statistic_list;
    private ArrayList<Goods_Sales> goods_sales_list;
    private TextView tv_search,tv_good_sales_num,tv_goods_sales_money,tv_goods_profit,tv_chart,tv_list;
    private ImageView iv_back,iv_horn_up_num,iv_horn_put_num,iv_horn_up_money,iv_horn_put__money,iv_horn_up_profit,iv_horn_put_profit;
    private DisplayImageOptions options;
    private LineGraphicView linegraphicview;
    private boolean sales_money_up=true;
    private boolean sales_nums_up=true;
    private boolean sales_profit_up=true;
    private Goods_Sales_Statistic_list_Adapter goods_sales_statistic_list_adapter;
    private int type;//type==2,筛选


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods__sales__statistics);
        SysUtils.setupUI(this,findViewById(R.id.activity_goods__sales__statistics));
        initToolbar(this);

        initView();
        initData();

    }

    private void initData() {
        goods_sales_list=new ArrayList<>();
        Intent intent=getIntent();
        if(intent!=null){
            type=intent.getIntExtra("type",0);
        }
        if(type==2){
            goods_sales_list.clear();
            goods_sales_list=intent.getParcelableArrayListExtra("goods_sales_list");
            //前几位商品显示
            if(goods_sales_list!=null) {
                if (goods_sales_list.size() <= 5) {
                    for (int i = 0; i < goods_sales_list.size(); i++) {
                        goods_sales_frontfive(i);
                    }
                } else {
                    for (int i = 0; i < 5; i++) {
                        goods_sales_frontfive(i);
                    }
                }
            }
            tv_goods_sales_money.setTextColor(Color.parseColor("#ffff0000"));
            iv_horn_up_money.setBackgroundResource(R.drawable.horn_red_up);
            iv_horn_put__money.setBackgroundResource(R.drawable.horn_black_put);
            sales_money_up=false;
            goods_sales_statistic_list_adapter=new Goods_Sales_Statistic_list_Adapter(Goods_Sales_StatisticsAcitvity.this,goods_sales_list);
            goods_sales_statistic_list.setAdapter(goods_sales_statistic_list_adapter);
        }else {
            getGoodsSalesInfo();
        }
    }

    private void initView() {
        goods_sales_layout= (LinearLayout) findViewById(R.id.goods_sales_layout);
        layout_goods_sales_money= (RelativeLayout) findViewById(R.id.layout_goods_sales_money);
        layout_good_sales_num= (RelativeLayout) findViewById(R.id.layout_good_sales_num);
        layout_goods_profit= (RelativeLayout) findViewById(R.id.layout_goods_profit);
        goods_sales_statistic_list= (ListView) findViewById(R.id.goods_sales_statistic_list);
        tv_goods_sales_money= (TextView) findViewById(R.id.tv_goods_sales_money);
        tv_good_sales_num= (TextView) findViewById(R.id.tv_good_sales_num);
        tv_goods_profit= (TextView) findViewById(R.id.tv_goods_profit);
        tv_search= (TextView) findViewById(R.id.tv_search);
        tv_chart= (TextView) findViewById(R.id.tv_chart);
        tv_list= (TextView) findViewById(R.id.tv_list);
        iv_horn_up_num= (ImageView) findViewById(R.id.iv_horn_up_num);
        iv_horn_put_num= (ImageView) findViewById(R.id.iv_horn_put_num);
        iv_horn_up_money= (ImageView) findViewById(R.id.iv_horn_up_money);
        iv_horn_put__money= (ImageView) findViewById(R.id.iv_horn_put__money);
        iv_horn_up_profit= (ImageView) findViewById(R.id.iv_horn_up_profit);
        iv_horn_put_profit= (ImageView) findViewById(R.id.iv_horn_put_profit);
        iv_back= (ImageView) findViewById(R.id.iv_back);
        linegraphicview= (LineGraphicView) findViewById(R.id.linegraphicview);




        tv_search= (TextView) findViewById(R.id.tv_search);
        iv_back= (ImageView) findViewById(R.id.iv_back);

        tv_search.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        layout_goods_sales_money.setOnClickListener(this);
        layout_good_sales_num.setOnClickListener(this);
        layout_goods_profit.setOnClickListener(this);
        tv_list.setOnClickListener(this);
        tv_chart.setOnClickListener(this);

        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo_default)
                .showImageForEmptyUri(R.drawable.logo_default)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(100))
                .build();

    }
    String img_src;
    private void getGoodsSalesInfo(){
        CustomRequest r=new CustomRequest(CustomRequest.Method.POST, SysUtils.getGoodsServiceUrl("count_goods"),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("商品销售统计ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        JSONArray data=ret.getJSONArray("data");
                        for(int i=0;i<data.length();i++){
                            JSONObject jsonObject1=data.getJSONObject(i);
                            String goods_id=jsonObject1.getString("goods_id");
                            String goods_name=jsonObject1.getString("goods_name");
                            String cost=jsonObject1.getString("cost");
                             img_src=jsonObject1.getString("img_src");
                            double total=jsonObject1.getDouble("total");
                            int nums=jsonObject1.getInt("nums");
                            double total_cost=Double.parseDouble(SetEditTextInput.stringpointtwo(cost));
                            total_cost=BigDecimalArith.mul(Double.parseDouble(cost),nums);
                            double profit=BigDecimalArith.sub(total,total_cost);
                            double profit_percent;
                            if(profit>0){
                                 profit_percent=BigDecimalArith.div(profit,total,2);
                            }else {
                                profit_percent=0.00;
                            }

                            Goods_Sales goods_sales=new Goods_Sales(goods_name,nums,total,profit,SetEditTextInput.stringpointtwo((profit_percent*100)+"")+"%",img_src);
                            goods_sales_list.add(goods_sales);

                        }
                        //前几位商品显示
                        if(goods_sales_list!=null) {
                            if (goods_sales_list.size() <= 5) {
                                for (int i = 0; i < goods_sales_list.size(); i++) {
                                    goods_sales_frontfive(i);
                                }
                            } else {
                                for (int i = 0; i < 5; i++) {
                                    goods_sales_frontfive(i);
                                }
                            }
                        }
                        tv_goods_sales_money.setTextColor(Color.parseColor("#ffff0000"));
                        iv_horn_up_money.setBackgroundResource(R.drawable.horn_red_up);
                        iv_horn_put__money.setBackgroundResource(R.drawable.horn_black_put);
                        sales_money_up=false;
                        goods_sales_statistic_list_adapter=new Goods_Sales_Statistic_list_Adapter(Goods_Sales_StatisticsAcitvity.this,goods_sales_list);
                        goods_sales_statistic_list.setAdapter(goods_sales_statistic_list_adapter);
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
            }

        });
        executeRequest(r);


    }

    /**
     * 商品销售前五
     * @param i
     */
    private void goods_sales_frontfive(int i){
    View view = View.inflate(Goods_Sales_StatisticsAcitvity.this, R.layout.goods_sales_statistics_hon_item, null);
        ImageView iv_goods_picture = (ImageView) view.findViewById(R.id.iv_goods_picture1);
    TextView tv_numbler1 = (TextView) view.findViewById(R.id.tv_numbler1);
    TextView tv_good_name1 = (TextView) view.findViewById(R.id.tv_good_name1);
    TextView tv_good_salemoney1 = (TextView) view.findViewById(R.id.tv_good_salemoney1);
    TextView tv_good_num1 = (TextView) view.findViewById(R.id.tv_good_num1);

    tv_numbler1.setText("NO."+(i+1));
    tv_good_name1.setText(goods_sales_list.get(i).getGoods_name());
    tv_good_salemoney1.setText("销售"+goods_sales_list.get(i).getGoods_sales_money()+"元");
    tv_good_num1.setText("销量"+goods_sales_list.get(i).getGoods_sales_num());
//                                    加载图片
    ImageLoader.getInstance().displayImage(goods_sales_list.get(i).getImg_src(), iv_goods_picture, options);
    goods_sales_layout.addView(view);
}
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                Intent intent=new Intent(Goods_Sales_StatisticsAcitvity.this,Goods_Sales_Statistics_SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_chart:
                Intent intent_chart=new Intent(Goods_Sales_StatisticsAcitvity.this,Goods_Sales_ChartStatisticsAcitvity.class);
                startActivity(intent_chart);
                break;
            case R.id.tv_list:
                break;
            case R.id.layout_goods_sales_money:
                if(goods_sales_list!=null) {
                    if (sales_money_up) {
                        sales_money_up = false;
                        downsales_money(goods_sales_list);
                        setIconChange(R.drawable.horn_black_up,R.drawable.horn_black_put,R.drawable.horn_red_up,
                                R.drawable.horn_black_put,R.drawable.horn_black_up,R.drawable.horn_black_put);
                        goods_sales_statistic_list_adapter.notifyDataSetChanged();
                    } else {
                        sales_money_up = true;
                        upsales_money(goods_sales_list);
                        setIconChange(R.drawable.horn_black_up,R.drawable.horn_black_put,R.drawable.horn_black_up,
                                R.drawable.horn_red_put,R.drawable.horn_black_up,R.drawable.horn_black_put);
                        goods_sales_statistic_list_adapter.notifyDataSetChanged();
                    }
                    tv_goods_sales_money.setTextColor(Color.parseColor("#ffff0000"));
                    tv_good_sales_num.setTextColor(Color.parseColor("#000000"));
                    tv_goods_profit.setTextColor(Color.parseColor("#000000"));
                }
                break;
            case R.id.layout_good_sales_num:
                if(goods_sales_list!=null) {
                    if (sales_nums_up) {
                        sales_nums_up = false;
                        downsales_num(goods_sales_list);
                        setIconChange(R.drawable.horn_red_up,R.drawable.horn_black_put,R.drawable.horn_black_up,
                                R.drawable.horn_black_put,R.drawable.horn_black_up,R.drawable.horn_black_put);
                        goods_sales_statistic_list_adapter.notifyDataSetChanged();
                    } else {
                        sales_nums_up = true;
                        upsales_num(goods_sales_list);
                        setIconChange(R.drawable.horn_black_up,R.drawable.horn_red_put,R.drawable.horn_black_up,
                                R.drawable.horn_black_put,R.drawable.horn_black_up,R.drawable.horn_black_put);
                        goods_sales_statistic_list_adapter.notifyDataSetChanged();
                    }
                }
                tv_good_sales_num.setTextColor(Color.parseColor("#ffff0000"));
                tv_goods_sales_money.setTextColor(Color.parseColor("#000000"));
                tv_goods_profit.setTextColor(Color.parseColor("#000000"));
                break;
            case R.id.layout_goods_profit:
                if(goods_sales_list!=null) {
                    if (sales_profit_up) {
                        sales_profit_up = false;
                        upsales_profit(goods_sales_list);
                        setIconChange(R.drawable.horn_black_up,R.drawable.horn_black_put,R.drawable.horn_black_up,
                                R.drawable.horn_black_put,R.drawable.horn_red_up,R.drawable.horn_black_put);
                        goods_sales_statistic_list_adapter.notifyDataSetChanged();
                    } else {
                        sales_profit_up = true;
                        downsales_profit(goods_sales_list);
                        setIconChange(R.drawable.horn_black_up,R.drawable.horn_black_put,R.drawable.horn_black_up,
                                R.drawable.horn_black_put,R.drawable.horn_black_up,R.drawable.horn_red_put);
                        goods_sales_statistic_list_adapter.notifyDataSetChanged();
                    }
                }
                tv_good_sales_num.setTextColor(Color.parseColor("#000000"));
                tv_goods_sales_money.setTextColor(Color.parseColor("#000000"));
                tv_goods_profit.setTextColor(Color.parseColor("#ffff0000"));
                break;
        }
    }

    /**
     * 统计升序降序图标的变化
     * @param resid1
     * @param resid2
     * @param resid3
     * @param resid4
     * @param resid5
     * @param resid6
     */
    private void setIconChange(int resid1,int resid2,int resid3,int resid4,int resid5,int resid6){
        iv_horn_up_num.setBackgroundResource(resid1);
        iv_horn_put_num.setBackgroundResource(resid2);
        iv_horn_up_money.setBackgroundResource(resid3);
        iv_horn_put__money.setBackgroundResource(resid4);
        iv_horn_up_profit.setBackgroundResource(resid5);
        iv_horn_put_profit.setBackgroundResource(resid6);
    }
    //销售额升序
    public ArrayList<Goods_Sales>  upsales_money(ArrayList<Goods_Sales>  bookList){
        Goods_Sales[] array = bookList.toArray(new Goods_Sales[bookList.size()]);
        Goods_Sales temp = null;
        for(int i=0;i<array.length;i++){
            for(int j=array.length-1;j>i;j--){
                if((array[j-1].getGoods_sales_money())>array[j].getGoods_sales_money()){
                    temp = array[j];
                    array[j] = array[j-1];
                    array[j-1] = temp;
                }
            }
        }
        ListIterator<Goods_Sales> i = bookList.listIterator();
        for (int j=0; j<array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }
    //销售额降序
    public ArrayList<Goods_Sales>  downsales_money(ArrayList<Goods_Sales>  bookList){
        Goods_Sales[] array = bookList.toArray(new Goods_Sales[bookList.size()]);
        Goods_Sales temp = null;
        for(int i=0;i<array.length;i++){
            for(int j=array.length-1;j>i;j--){
                if(array[j-1].getGoods_sales_money()<array[j].getGoods_sales_money()){
                    temp = array[j];
                    array[j] = array[j-1];
                    array[j-1] = temp;
                }
            }
        }
        ListIterator<Goods_Sales> i = bookList.listIterator();
        for (int j=0; j<array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }
    //销售量升序
    public ArrayList<Goods_Sales>  upsales_num(ArrayList<Goods_Sales>  bookList){
        Goods_Sales[] array = bookList.toArray(new Goods_Sales[bookList.size()]);
        Goods_Sales temp = null;
        for(int i=0;i<array.length;i++){
            for(int j=array.length-1;j>i;j--){
                if((array[j-1].getGoods_sales_num())>array[j].getGoods_sales_num()){
                    temp = array[j];
                    array[j] = array[j-1];
                    array[j-1] = temp;
                }
            }
        }
        ListIterator<Goods_Sales> i = bookList.listIterator();
        for (int j=0; j<array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }
    //销售量降序
    public ArrayList<Goods_Sales>  downsales_num(ArrayList<Goods_Sales>  bookList){
        Goods_Sales[] array = bookList.toArray(new Goods_Sales[bookList.size()]);
        Goods_Sales temp = null;
        for(int i=0;i<array.length;i++){
            for(int j=array.length-1;j>i;j--){
                if(array[j-1].getGoods_sales_num()<array[j].getGoods_sales_num()){
                    temp = array[j];
                    array[j] = array[j-1];
                    array[j-1] = temp;
                }
            }
        }
        ListIterator<Goods_Sales> i = bookList.listIterator();
        for (int j=0; j<array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }
    //利润升序
    public ArrayList<Goods_Sales>  upsales_profit(ArrayList<Goods_Sales>  bookList){
        Goods_Sales[] array = bookList.toArray(new Goods_Sales[bookList.size()]);
        Goods_Sales temp = null;
        for(int i=0;i<array.length;i++){
            for(int j=array.length-1;j>i;j--){
                if((array[j-1].getGoods_sales_profit())>array[j].getGoods_sales_profit()){
                    temp = array[j];
                    array[j] = array[j-1];
                    array[j-1] = temp;
                }
            }
        }
        ListIterator<Goods_Sales> i = bookList.listIterator();
        for (int j=0; j<array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
    }
    //利润降序
    public ArrayList<Goods_Sales>  downsales_profit(ArrayList<Goods_Sales>  bookList){
        Goods_Sales[] array = bookList.toArray(new Goods_Sales[bookList.size()]);
        Goods_Sales temp = null;
        for(int i=0;i<array.length;i++){
            for(int j=array.length-1;j>i;j--){
                if(array[j-1].getGoods_sales_profit()<array[j].getGoods_sales_profit()){
                    temp = array[j];
                    array[j] = array[j-1];
                    array[j-1] = temp;
                }
            }
        }
        ListIterator<Goods_Sales> i = bookList.listIterator();
        for (int j=0; j<array.length; j++) {
            i.next();
            i.set(array[j]);
        }
        return bookList;
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
        Goods_Sales_StatisticsAcitvity.this.registerReceiver(broadcastReceiver,new IntentFilter(Global.BROADCAST_Goods_Sales_StatisticsAcitvity_ACTION));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Goods_Sales_StatisticsAcitvity.this.unregisterReceiver(broadcastReceiver);
    }
}
