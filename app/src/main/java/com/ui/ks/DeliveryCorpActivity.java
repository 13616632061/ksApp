package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.entity.DeliveryCorp;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DeliveryCorpActivity extends BaseActivity {
    private ListView lv_content;
    private SwipeRefreshLayout refresh_header;
    private ReportAdapter adapter = null;
    public ArrayList<DeliveryCorp> cat_list;

    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_corp);

        initToolbar(this);

        layout_err = (View) findViewById(R.id.layout_err);
        include_noresult = layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_tv_noresult.setText("还没有物流公司哦");
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
                loadData();
            }
        });

        cat_list = new ArrayList<DeliveryCorp>();
        lv_content = (ListView) findViewById(R.id.lv_content);
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                int p = position;

                if (p >= 0 && p < cat_list.size()) {
                    DeliveryCorp bean = cat_list.get(p);

                    Intent returnIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("corp_id", bean.getCorp_id());
                    bundle.putString("corp_name", bean.getName());
                    returnIntent.putExtras(bundle);
                    setResult(RESULT_OK, returnIntent);

                    onBackPressed();
                }
            }
        });

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
                loadData();
            }
        });
    }

    private void setRefreshing(boolean refreshing) {
        refresh_header.setRefreshing(refreshing);
    }


    private void loadData() {
        CustomRequest r = new CustomRequest(Request.Method.POST,  SysUtils.getSellerServiceUrl("dlycorp"), null, new Response.Listener<JSONObject>() {
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
                        cat_list.clear();
                        JSONArray array = dataObject.getJSONArray("dlycorp_list");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject data = array.optJSONObject(i);

                                DeliveryCorp bean = new DeliveryCorp();
                                bean.setCorp_id(data.getInt("corp_id"));
                                bean.setName(data.getString("name"));

                                cat_list.add(bean);
                            }
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
            this.inflater = LayoutInflater.from(DeliveryCorpActivity.this);
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
                    convertView = inflater.inflate(R.layout.item_deliver_corp, null);

                    holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);

                    convertView.setTag(holder);
                } catch (Exception e) {
                }
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            final DeliveryCorp data = cat_list.get(position);
            if(data != null) {
                holder.tv_content.setText(data.getName());
            }

            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tv_content;
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
        if(cat_list.size() < 1) {
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
