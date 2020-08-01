package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ui.entity.News;
import com.ui.global.Global;
import com.ui.util.CustomRequest;
import com.ui.util.SelectPicPopupWindow;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 营业统计页面
 */
public class StatisticsActivity extends BaseActivity {
    private ShopActivity mainAct;
    private TextView id_text_1, id_text_2, id_text_3, id_text_5;
    private SwipeRefreshLayout refresh_header;
    private ListView lv_content;
    private TextView textView1, textView2,tv_statistics_title;
    private String intro = "";
    public ArrayList<News> ad_list;
    private View adView = null;
    SliderLayout slider;
    private PagerIndicator custom_indicator;
    private int type;
    private String[] title_str=new String[]{"全部店面总额","南山店","福永店","宝安店"};
    private SelectPicPopupWindow mSelectPicPopupWindow;
    private  ImageView iv_statistics_expand_more,iv_statistics_back;
    private  RelativeLayout statistics_title_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        SysUtils.setupUI(this, findViewById(R.id.activity_statistics));//点击空白处隐藏软键盘
        initToolbar(this);//封装的页面返回键，及返回监听

        iv_statistics_expand_more= (ImageView) findViewById(R.id.iv_statistics_expand_more);
        iv_statistics_back= (ImageView) findViewById(R.id.iv_statistics_back);
        statistics_title_layout= (RelativeLayout) findViewById(R.id.statistics_title_layout);
        iv_statistics_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_statistics_title= (TextView) findViewById(R.id.tv_statistics_title);
        Intent intent=getIntent();
        type=intent.getIntExtra("type",0);
        if (type==2){
            tv_statistics_title.setText("营业统计");
            iv_statistics_expand_more.setVisibility(View.GONE);
        }else if (type==1){
            tv_statistics_title.setText("营业统计");
            iv_statistics_expand_more.setVisibility(View.GONE);
        }
//        statistics_title_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSelectPicPopupWindow=new SelectPicPopupWindow(StatisticsActivity.this, 2, new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                        setOrderPay(position + 1);
//                        KsApplication.selectItem=position ;
//                        tv_statistics_title.setText(title_str[position]);
//                        mSelectPicPopupWindow.dismiss();
//                    }
//                });
//                mSelectPicPopupWindow.showAtLocation(findViewById(R.id.tv_statistics_title) , Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
//            }
//        });
        ad_list = new ArrayList<News>();
        refresh_header = (SwipeRefreshLayout)findViewById(R.id.refresh_header);
        refresh_header.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);
        refresh_header.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();
            }
        });

        lv_content = (ListView) findViewById(R.id.lv_content);

        View firstView = (LinearLayout) LayoutInflater.from(StatisticsActivity.this).inflate(R.layout.report_seller, lv_content, false);
        lv_content.addHeaderView(firstView);
        lv_content.setAdapter(null);
        RelativeLayout relativeLayout3 = (RelativeLayout) firstView.findViewById(R.id.relativeLayout3);
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toOpenList(1);
                toDayOrderList(1);
            }
        });

        //今日全部订单
        View set_item_1 = (View) firstView.findViewById(R.id.set_item_1);
        ImageView idx_1 = (ImageView) set_item_1.findViewById(R.id.ll_set_idx);
        idx_1.setVisibility(View.VISIBLE);
        if(type==2){
            set_item_1.setVisibility(View.GONE);
        }else {
            set_item_1.setVisibility(View.GONE);
        }

        id_text_1 = (TextView) set_item_1.findViewById(R.id.ll_set_hint_text);
        //今日全部订单
        SysUtils.setLine(set_item_1, Global.SET_CELLUP, getString(R.string.today_all_orders), R.drawable.icon_item_1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDayOrderList(1);
            }
        });

        //昨日营业额
        View set_item_2 = (View) firstView.findViewById(R.id.set_item_2);
        ImageView idx_2 = (ImageView) set_item_2.findViewById(R.id.ll_set_idx);
        idx_2.setVisibility(View.VISIBLE);

        id_text_2 = (TextView) set_item_2.findViewById(R.id.ll_set_hint_text);
            SysUtils.setLine(set_item_2, Global.SET_CELLWHITE, getString(R.string.yesterday_turnover), R.drawable.icon_item_2, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type!=2) {
                    toDayOrderList(2);
                    }
                }
            });


        //本月营业额
        View set_item_3 = (View) firstView.findViewById(R.id.set_item_3);
        ImageView idx_3 = (ImageView) set_item_3.findViewById(R.id.ll_set_idx);
        idx_3.setVisibility(View.VISIBLE);

        id_text_3 = (TextView) set_item_3.findViewById(R.id.ll_set_hint_text);
            SysUtils.setLine(set_item_3, Global.SET_CELLWHITE, getString(R.string.turnover_of_this_month), R.drawable.icon_item_3, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type != 2) {
                        toMonthOrderList(3);
                    }
                }
            });
        //上月营业额
        View set_item_5 = (View) firstView.findViewById(R.id.set_item_5);
        ImageView idx_5 = (ImageView) set_item_5.findViewById(R.id.ll_set_idx);
        idx_5.setVisibility(View.VISIBLE);

        id_text_5 = (TextView) set_item_5.findViewById(R.id.ll_set_hint_text);
        //上月营业额
            SysUtils.setLine(set_item_5, Global.SET_SINGLE_LINE, getString(R.string.turnover_of_last_month), R.drawable.icon_item_3, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type!=2) {
                        toMonthOrderList(4);
                    }
                }
            });
        //店铺公告
        View set_item_4 = (View) firstView.findViewById(R.id.set_item_4);
        if(type==2){
            set_item_4.setVisibility(View.GONE);
        }else {
            set_item_4.setVisibility(View.VISIBLE);
        }
        SysUtils.setLine(set_item_4, Global.SET_TWO_LINE, getString(R.string.shop_otices), R.drawable.icon_item_4, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new MaterialDialog.Builder(getActivity())
//                        .theme(SysUtils.getDialogTheme())
//                        .title("店铺公告")
//                        .content(intro)
//                        .positiveText("关闭")
//                        .show();

                Bundle b = new Bundle();
                b.putString("notice", intro);

                SysUtils.startAct(StatisticsActivity.this, new NoticeActivity(), b, true);
            }
        });

        textView1 = (TextView) firstView.findViewById(R.id.textView1);
        textView2 = (TextView) firstView.findViewById(R.id.textView2);
//        textView3 = (TextView) firstView.findViewById(R.id.textView3);

        refresh_header.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                initView();
            }
        });

    }
    private void setRefreshing(boolean refreshing) {
        refresh_header.setRefreshing(refreshing);
    }

    private void initView() {
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("open"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                setRefreshing(false);

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        textView1.setText(dataObject.getString("num"));
                        textView2.setText(SysUtils.getMoneyFormat(dataObject.getDouble("amount")));
//                        textView3.setText(SysUtils.getMoneyFormat(dataObject.getDouble("favorable")));

                        id_text_1.setText(dataObject.getString("today_dead_num"));
                        id_text_2.setText(SysUtils.getMoneyFormat(dataObject.getDouble("yesterday_amount")));
                        id_text_3.setText(SysUtils.getMoneyFormat(dataObject.getDouble("month_amount")));
                        id_text_5.setText(SysUtils.getMoneyFormat(dataObject.getDouble("lastmonth_amount")));

                        intro = SysUtils.getFinalString("intro", dataObject);

                        ad_list.clear();
                        JSONArray params_img = dataObject.optJSONArray("params_img");
                        if (params_img != null && params_img.length() > 0) {
                            for (int i = 0; i < params_img.length(); i++) {
                                JSONObject data = params_img.optJSONObject(i);

                                News b = new News(0,
                                        1,
                                        0,
                                        data.getString("linkinfo"),
                                        data.getString("linktarget"),
                                        data.getString("link"),
                                        "");


                                ad_list.add(b);
                            }
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    loadAd();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setRefreshing(false);
                SysUtils.showNetworkError();
            }
        });

        executeRequest(r);
    }


    private void loadAd() {
        if(ad_list.size() > 0) {
            //有广告
            if(adView == null) {
                adView = (View) LayoutInflater.from(StatisticsActivity.this).inflate(R.layout.ad, null);
                if (type==2){
                    adView.setVisibility(View.GONE);
                }
                RelativeLayout ll = (RelativeLayout) adView.findViewById(R.id.ad_layout);
                slider = (SliderLayout) adView.findViewById(R.id.slider);
                custom_indicator = (PagerIndicator) adView.findViewById(R.id.custom_indicator);
                lv_content.addHeaderView(adView);

                int width = Global.magicWidth - 40;
                int height = width * 8 / 15;

                //设置高度
                ll.setLayoutParams(new AbsListView.LayoutParams(width, height));
                slider.setPresetTransformer(SliderLayout.Transformer.Default);
                slider.setDuration(Global.magicDuration);
            }
            slider.removeAllSliders();
//        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slider.setCustomIndicator(custom_indicator);
//            slider.setCustomAnimation(new DescriptionAnimation());
            for(int i = 0; i < ad_list.size(); i++) {
                News bean = ad_list.get(i);

                TextSliderView textSliderView = new TextSliderView(StatisticsActivity.this);
                textSliderView
                        .description(bean.getSubject())
                        .image(bean.getPic_url())
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                News bean = slider.getBundle().getParcelable("data");
                                String u = bean.getLinkurl();

                                if (!StringUtils.isEmpty(u)) {
                                    Bundle b = new Bundle();
                                    b.putString("title", bean.getSubject());
                                    b.putString("url", u);

                                    SysUtils.startAct(StatisticsActivity.this, new AdActivity(), b);
                                }
//                                SysUtils.newsClick(getActivity(), bean);
                            }
                        });

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putParcelable("data", bean);

                slider.addSlider(textSliderView);
            }
        } else {
            //没有广告，尝试移除view
            if(adView != null) {
                lv_content.removeHeaderView(adView);
                adView = null;
            }
        }
    }


    @Override
    public void onStart() {
        if(slider != null) {
            slider.startAutoCycle();
        }

        super.onStart();
    }

    @Override
    public void onStop() {
        if(slider != null) {
            slider.stopAutoCycle();
        }

        super.onStop();
    }

    //刷新广播
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh_header.post(new Runnable() {
                @Override
                public void run() {
                    setRefreshing(true);
                    initView();
                }
            });
        }
    };
    @Override
    public void onResume() {
        super.onResume();

        StatisticsActivity.this.registerReceiver(broadcastReceiver, new IntentFilter(Global.BROADCAST_REFRESH_SHOP_REPORT_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            StatisticsActivity.this.unregisterReceiver(broadcastReceiver);
        } catch(Exception e) {

        }
    }

    /**
     * 1表示今天，2表示昨天
     * @param type
     */
    private void toDayOrderList(int type) {
        Bundle b = new Bundle();
        b.putInt("type", type);

        SysUtils.startAct(StatisticsActivity.this, new TodayAllOrderActivity(), b);
    }

    /**
     * 3表示本月，4表示上月
     * @param type
     */
    private void toMonthOrderList(int type) {
        Bundle b = new Bundle();
        b.putInt("type", type);

        SysUtils.startAct(StatisticsActivity.this, new ThisMonthAllOrderActivity(), b);
    }
}
