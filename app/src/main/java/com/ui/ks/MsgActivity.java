package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.entity.Message;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MsgActivity extends BaseActivity {
    private PagingListView lv_content;
    private SwipeRefreshLayout refresh_header;
    private ReportAdapter adapter = null;
    public ArrayList<Message> cat_list;

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
        setContentView(R.layout.activity_msg);

        initToolbar(this);

        textColor = getResources().getColor(R.color.text_color);
        greenColor = getResources().getColor(R.color.green_color);
        redColor = getResources().getColor(R.color.red_color);

        layout_err = (View) findViewById(R.id.layout_err);
        include_noresult = layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_tv_noresult.setText("还没有消息记录哦");
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

        cat_list = new ArrayList<Message>();
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
        Map<String,Object> finalMap = new HashMap<String,Object>();
        finalMap.put("page", PAGE);
        finalMap.put("pagelimit", NUM_PER_PAGE);

        String uri = SysUtils.getSellerServiceUrl("msg");

        CustomRequest r = new CustomRequest(Request.Method.POST, uri, null, new Response.Listener<JSONObject>() {
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
                        if(PAGE <= 1) {
                            cat_list.clear();
                        }

                        JSONArray array = dataObject.getJSONArray("msg_info");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject data = array.optJSONObject(i);

                                Message bean = new Message();
                                bean.setComment_id(data.getInt("comment_id"));
                                bean.setTitle(data.getString("title"));
                                bean.setComment(data.getString("comment"));
                                bean.setTime(data.getString("time"));
                                bean.setHasRead(data.getBoolean("sel_read_status"));

                                cat_list.add(bean);
                            }
                        }

                        int total = dataObject.getInt("total");
                        int totalPage = (int)Math.ceil((float)total / NUM_PER_PAGE);
                        loadingMore = totalPage > PAGE;

                        if (loadingMore) {
                            PAGE++;
//                            SysUtils.showSuccess("more");
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
            this.inflater = LayoutInflater.from(MsgActivity.this);
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
                    convertView = inflater.inflate(R.layout.item_message, null);

                    holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                    holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
                    holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
                    holder.linearLayout5 = (LinearLayout) convertView.findViewById(R.id.linearLayout5);
                    holder.editText1 = (TextView) convertView.findViewById(R.id.editText1);

                    convertView.setTag(holder);
                } catch (Exception e) {
                }
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            final Message data = cat_list.get(position);
            if(data != null) {
                holder.tv_content.setText(data.getTitle());
                holder.textView1.setText(data.getDate());
                holder.textView2.setText(data.getComment());

                if (data.isHasRead()) {
                    holder.linearLayout5.setVisibility(View.GONE);
                } else {
                    holder.linearLayout5.setVisibility(View.VISIBLE);
                }

                holder.editText1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MaterialDialog.Builder(MsgActivity.this)
                                .content(getString(R.string.str111))
                                .theme(SysUtils.getDialogTheme())
                                .positiveText(getString(R.string.sure))
                                .negativeText(getString(R.string.cancel))
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        Map<String,String> map = new HashMap<String,String>();
                                        map.put("comment_id", String.valueOf(data.getComment_id()));
                                        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("msgRead"), map, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject jsonObject) {
                                                hideLoading();
                                                try {
                                                    JSONObject ret = SysUtils.didResponse(jsonObject);
                                                    String status = ret.getString("status");
                                                    String message = ret.getString("message");

                                                    if (!status.equals("200")) {
                                                        SysUtils.showError(message);
                                                    } else {
                                                        SysUtils.showSuccess("操作已执行");
                                                        setHasRead(data.getComment_id());
                                                    }
                                                } catch(Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                                SysUtils.showNetworkError();
                                                hideLoading();
                                            }
                                        });

                                        executeRequest(r);
                                        showLoading(MsgActivity.this);
                                    }
                                })
                                .show();
                    }
                });
            }

            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tv_content, textView1, textView2;
        public LinearLayout linearLayout5;
        public TextView editText1;
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

    private void setHasRead(int comment_id) {
        for(int i = 0; i < cat_list.size(); i++) {
            Message bean = cat_list.get(i);

            if(bean.getComment_id() == comment_id) {
                bean.setHasRead(true);

                cat_list.set(i, bean);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(isTaskRoot()) {
            finish();
//            Intent intent = new Intent(this, WelcomeActivity.class);
            Intent intent = new Intent(this, Welcome2Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }
}
