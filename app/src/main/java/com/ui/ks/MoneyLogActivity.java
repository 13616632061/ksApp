package com.ui.ks;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.entity.Money;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MoneyLogActivity extends BaseActivity {
    private PagingListView lv_content;
    private SwipeRefreshLayout refresh_header;
    private ReportAdapter adapter = null;
    public ArrayList<Money> cat_list;

    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;

    int PAGE = 1;
    int NUM_PER_PAGE = 10;
    boolean loadingMore = false;
    private int textColor, greenColor, redColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_log);

        initToolbar(this);

        textColor = getResources().getColor(R.color.text_color);
        greenColor = getResources().getColor(R.color.green_color);
        redColor = getResources().getColor(R.color.red_color);

        layout_err = (View) findViewById(R.id.layout_err);
        include_noresult = layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_tv_noresult.setText("还没有充值提现记录哦");
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
//                setRefreshing(true);
                loadFirst();
            }
        });

        cat_list = new ArrayList<Money>();
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

        refresh_header = (SwipeRefreshLayout) findViewById(R.id.refresh_header);
        refresh_header.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);
        refresh_header.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFirst();
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
        Map<String, String> finalMap = new HashMap<String, String>();
        finalMap.put("page", PAGE+"");
        finalMap.put("pagelimit", NUM_PER_PAGE+"");

//        String uri = LoginUtils.isSeller() ? SysUtils.getSellerServiceUrl("getListByMemId") : SysUtils.getMemberServiceUrl("drawal");

        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("getListByMemId"),finalMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                setRefreshing(false);

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("getListByMemId:" + ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        if (PAGE <= 1) {
                            cat_list.clear();
                        }
                        JSONArray array = dataObject.getJSONArray("advance_list");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject data = array.optJSONObject(i);

                                Money bean = new Money();
                                bean.setStatus(data.getInt("STATUS"));
                                bean.setType(data.getString("type"));
                                bean.setMessage(data.getString("message"));
                                bean.setMtime(data.getString("mtime"));
                                bean.setMoney(data.getDouble("money"));
                                bean.setService(data.getDouble("service"));

                                String bank_id = "", bank_name = "";
                                if (!TextUtils.isEmpty(data.optString("bank_id"))) {
                                    bank_id = data.optString("bank_id");
                                }
                                if (!TextUtils.isEmpty(data.optString("bank_name"))) {
                                    bank_name = data.optString("bank_name");
                                }
                                bean.setBank_id(bank_id);
                                bean.setBank_name(bank_name);

                                cat_list.add(bean);
                            }
                        }

                        int total = dataObject.getInt("total");
                        int totalPage = (int) Math.ceil((float) total / NUM_PER_PAGE);
                        loadingMore = totalPage > PAGE;

                        if (loadingMore) {
                            PAGE++;
//                            SysUtils.showSuccess("more");
                        }
                    }
                } catch (Exception e) {
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
            this.inflater = LayoutInflater.from(MoneyLogActivity.this);
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
                    convertView = inflater.inflate(R.layout.item_money, null);

                    holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                    holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
                    holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
                    holder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
                    holder.textView4 = (TextView) convertView.findViewById(R.id.textView4);
                    holder.tv_service = (TextView) convertView.findViewById(R.id.tv_service);
                    convertView.setTag(holder);
                } catch (Exception e) {
                }
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            final Money data = cat_list.get(position);
            if(data != null) {
                holder.tv_content.setText(data.getMtime());
                holder.textView1.setText(data.getStatusStr());
                if (data.getStatus() == 1) {
                    holder.textView1.setTextColor(textColor);
                } else if (data.getStatus() == 2) {
                    holder.textView1.setTextColor(redColor);
                } else {
                    holder.textView1.setTextColor(greenColor);
                }
                holder.textView2.setText("类型：" + (data.getType().equals("withdraw") ? "提现" : "充值"));
                holder.textView3.setText(SysUtils.getMoneyFormat(data.getMoney()));
                if(data.getService()>0){
                    holder.tv_service.setVisibility(View.VISIBLE);
                    holder.tv_service.setText("服务费"+data.getService()+"元");
                }else {
                    holder.tv_service.setVisibility(View.GONE);
                }

                if(data.getType().equals("withdraw") && !TextUtils.isEmpty(data.getBank_id())) {
                    //提现
                    holder.textView4.setText(data.getBank_name() + "(" + data.getBank_id() + ")");
                    holder.textView4.setVisibility(View.VISIBLE);
                } else {
                    holder.textView4.setVisibility(View.GONE);
                }
            }

            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tv_content, textView1, textView2, textView3, textView4,tv_service;
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
        if (PAGE <= 1 && cat_list.size() < 1) {
            if (!include_nowifi.isShown()) {
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