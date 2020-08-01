package com.ui.ks;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.entity.SellerReport;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportDetailActivity extends BaseActivity {
    private PagingListView lv_content;
    private SwipeRefreshLayout refresh_header;
    private ReportAdapter adapter = null;
    public ArrayList<SellerReport> cat_list;

    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;

    int PAGE = 1;
    int NUM_PER_PAGE = 10;
    boolean loadingMore = false;


    private TextView textView1;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, 2);
        setContentView(R.layout.activity_report_detail);

        initToolbar(this);

        layout_err = (View) findViewById(R.id.layout_err);
        include_noresult = layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_tv_noresult.setText("啊哦，翻山越岭都没有找到数据");
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
                loadFirst();
            }
        });

        cat_list = new ArrayList<SellerReport>();
        lv_content = (PagingListView) findViewById(R.id.lv_content);
        lv_content.setPagingableListener(new PagingListView.Pagingable() {
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


        View firstView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.report_detail, lv_content, false);
        lv_content.addHeaderView(firstView);
        textView1 = (TextView) firstView.findViewById(R.id.textView1);
        textView2 = (TextView) firstView.findViewById(R.id.textView2);

        refresh_header = (SwipeRefreshLayout) findViewById(R.id.refresh_header);
        refresh_header.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);
        refresh_header.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        refresh_header.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                loadFirst();
            }
        });
    }


    private void loadFirst() {
        PAGE = 1;
        loadData();
    }

    private void setRefreshing(boolean refreshing) {
        refresh_header.setRefreshing(refreshing);
    }


    private void loadData() {
        Map<String,String> finalMap = new HashMap<String,String>();
        finalMap.put("page", String.valueOf(PAGE));
        finalMap.put("pagelimit", String.valueOf(NUM_PER_PAGE));

        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getMemberServiceUrl("welcome_detail"), finalMap, new Response.Listener<JSONObject>() {
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
                        //推荐商家数
                        textView1.setText(dataObject.getString("count_sellers"));
                        //本月收入
                        textView2.setText(SysUtils.getMoneyFormat(dataObject.getDouble("month_income")));

                        if(PAGE <= 1) {
                            cat_list.clear();
                        }

                        JSONArray array = dataObject.getJSONArray("seller_detail");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject data = array.optJSONObject(i);

                                SellerReport bean = new SellerReport();
                                bean.setSeller_id(data.getInt("seller_id"));
                                bean.setSeller_name(data.getString("seller_name"));
                                bean.setBn(data.getString("bn"));
                                bean.setToday_income(data.getDouble("today_income"));
                                bean.setYesterday_income(data.getDouble("yesterday_income"));
                                bean.setMonth_income(data.getDouble("month_income"));

                                cat_list.add(bean);
                            }
                        }

                        int total = dataObject.getInt("total");
                        int totalPage = (int)Math.ceil((float)total / NUM_PER_PAGE);

                        loadingMore = totalPage > PAGE;

                        if (loadingMore) {
                            PAGE++;
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    setView();

                    updateAdapter();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setNoNetwork();
                setRefreshing(false);
            }
        });

        executeRequest(r);
    }

    private void updateAdapter() {
        lv_content.onFinishLoading(loadingMore, null);

        if(adapter == null) {
            adapter = new ReportAdapter();
            lv_content.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public class ReportAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public ReportAdapter() {
            super();
            this.inflater = LayoutInflater.from(ReportDetailActivity.this);
        }

        public int getCount() {
            return cat_list.size();
        }

        public Object getItem(int position) {
            return cat_list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                try {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.item_report_detail, null);

                    holder.area_name = (TextView) convertView.findViewById(R.id.area_name);
                    holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
                    holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
                    holder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
                    convertView.setTag(holder);
                } catch (Exception e) {
                }
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            final SellerReport data = cat_list.get(position);
            if(data != null) {
                holder.area_name.setText(data.getSeller_name());
                holder.textView1.setText(SysUtils.getMoneyFormat(data.getToday_income()));
                holder.textView2.setText(SysUtils.getMoneyFormat(data.getYesterday_income()));
                holder.textView3.setText(SysUtils.getMoneyFormat(data.getMonth_income()));
            }

            return convertView;
        }
    }

    static class ViewHolder {
        public TextView area_name;
        public TextView textView1, textView2, textView3;
    }

    private void setView() {
        if(PAGE <= 1) {
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
    }

    private void setNoNetwork() {
        //网络不通
        if(PAGE <= 1 && cat_list.size() < 1) {
            if(!include_nowifi.isShown()) {
                lv_content.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            }
        } else {
            SysUtils.showNetworkError();
        }
    }
}
