package com.ui.ks;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ui.entity.News;
import com.ui.fragment.BaseFragmentMainBranch;
import com.ui.global.Global;
import com.ui.util.CustomRequest;
import com.ui.util.RequestManager;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 总店
 * Created by Administrator on 2017/1/18.
 */

public class MainFrament extends BaseFragmentMainBranch {

    private RelativeLayout statistics_layout;
    private SwipeRefreshLayout refresh_header;
    private ListView lv_content;
    public ArrayList<News> ad_list;
    private String intro = "";
    private TextView id_text_1, id_text_2, id_text_3, id_text_5;
    private TextView textView1, textView2,tv_statistics_title;
    private int type=1;//总店：1，分店：2；
    private  String total_today;
    private String total_money_today;
    private  String total_money_yes;
    private  String total_money_mon;
    private String total_money_last;

    @Override
    protected View initView() {
        View view=View.inflate(mContext,R.layout.activity_statistics,null);
        statistics_layout= (RelativeLayout) view.findViewById(R.id.statistics_layout);
        statistics_layout.setVisibility(View.GONE);
        refresh_header= (android.support.v4.widget.SwipeRefreshLayout) view.findViewById(R.id.refresh_header);
        lv_content= (ListView) view.findViewById(R.id.lv_content);



        return view;
    }

    @Override
    protected void initData() {
        super.initData();
//        getData();
        ad_list = new ArrayList<News>();
        refresh_header.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);
        refresh_header.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        View firstView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.report_seller, lv_content, false);
        lv_content.addHeaderView(firstView);
        lv_content.setAdapter(null);
        RelativeLayout relativeLayout3 = (RelativeLayout) firstView.findViewById(R.id.relativeLayout3);
        //今日全部订单
        View set_item_1 = (View) firstView.findViewById(R.id.set_item_1);
        ImageView idx_1 = (ImageView) set_item_1.findViewById(R.id.ll_set_idx);
        set_item_1.setVisibility(View.GONE);

        //昨日营业额
        View set_item_2 = (View) firstView.findViewById(R.id.set_item_2);
        ImageView idx_2 = (ImageView) set_item_2.findViewById(R.id.ll_set_idx);
        idx_2.setVisibility(View.VISIBLE);

        id_text_2 = (TextView) set_item_2.findViewById(R.id.ll_set_hint_text);
        SysUtils.setLine(set_item_2, Global.SET_CELLWHITE, "昨日营业额", R.drawable.icon_item_2, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        View set_item_4 = (View) firstView.findViewById(R.id.set_item_4);
        set_item_4.setVisibility(View.GONE);

        textView1 = (TextView) firstView.findViewById(R.id.textView1);
        textView2 = (TextView) firstView.findViewById(R.id.textView2);
        refresh_header.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                getData();
            }
        });
    }
    private void setRefreshing(boolean refreshing) {
        refresh_header.setRefreshing(refreshing);
    }
    private void getData() {
        Map<String,String> map= new HashMap<String,String>();
        map.put("type",type+"");
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("store_list"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                setRefreshing(false);

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("总店数据："+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        JSONArray arry = dataObject.optJSONArray("info");
                        if (arry  != null && arry .length() > 0) {
                            for (int i = 0; i < arry .length(); i++) {
                                JSONObject data = arry .optJSONObject(i);
                                 total_today=data.getString("total_today");
                               total_money_today=data.getString("total_money_today");
                                total_money_yes=data.getString("total_money_yes");
                                 total_money_mon=data.getString("total_money_mon");
                                total_money_last=data.getString("total_money_last");
//                                ad_list.add(b);
                            }
                            id_text_2.setText("￥"+total_money_yes);
                            id_text_5.setText("￥"+total_money_last);
                            id_text_3.setText("￥"+total_money_mon);
                            textView1.setText(total_today);
                            textView2.setText("￥"+total_money_today);
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setRefreshing(false);
                SysUtils.showNetworkError();
            }
        });

        RequestManager.addRequest(r, mContext);
    }
}
