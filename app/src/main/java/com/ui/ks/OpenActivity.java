package com.ui.ks;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.entity.Order;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OpenActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private PagingListView lv_content;
    private SwipeRefreshLayout refresh_header;
    public ArrayList<Order> cat_list;
    private PartyAdapter adapter = null;
    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;

    int PAGE = 1;
    int NUM_PER_PAGE = 20;
    boolean loadingMore = false;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        initToolbar(this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("type")) {
                type = bundle.getInt("type");
            }
        }

        if(type < 1 || type > 5) {
            type = 1;
        }

        if(type == 1) {
            setToolbarTitle(getString(R.string.str314));//今日付款订单
        } else if(type == 2) {
            setToolbarTitle(getString(R.string.today_all_orders));//今日全部订单
        } else if(type == 3) {
            setToolbarTitle(getString(R.string.str315));//昨日订单
        } else if(type == 4) {
            setToolbarTitle(getString(R.string.str316));//本月订单
        } else if(type == 5) {
            setToolbarTitle(getString(R.string.str317));//上月订单
        }

        layout_err = (View) findViewById(R.id.layout_err);
        include_noresult = layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_tv_noresult.setText(getString(R.string.str318));//订单列表为空
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

        cat_list = new ArrayList<Order>();
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

        //点击
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                int actualPosition = position - lv_content.getHeaderViewsCount();
                if (actualPosition >= 0 && actualPosition < cat_list.size()) {
                    //点击
                    Order data = cat_list.get(actualPosition);

                    Bundle bundle = new Bundle();
                    bundle.putString("order_id", data.getOrderSn());
                    SysUtils.startAct(OpenActivity.this, new OrderDetailActivity(), bundle);
                }
            }
        });

        refresh_header = (SwipeRefreshLayout) findViewById(R.id.refresh_header);
        refresh_header.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);
        refresh_header.setOnRefreshListener(this);

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
        Map<String,String> map = new HashMap<String,String>();
        map.put("page", String.valueOf(PAGE));
        map.put("pagelimit", String.valueOf(NUM_PER_PAGE));
        map.put("type", String.valueOf(type));

        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("openList"), map, new Response.Listener<JSONObject>() {
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

                        JSONArray array = dataObject.getJSONArray("orders_info");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject data = array.optJSONObject(i);

                                Order b = SysUtils.getOrderRow(data);

                                cat_list.add(b);
                            }
                        }
                    }

                    int total = dataObject.getInt("total");
                    int totalPage = (int)Math.ceil((float)total / NUM_PER_PAGE);
                    loadingMore = totalPage > PAGE;

                    if (loadingMore) {
                        PAGE++;
//                            SysUtils.showSuccess("more");
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
                setRefreshing(false);
                lv_content.setIsLoading(false);
                setNoNetwork();
            }
        });

        executeRequest(r);
    }


    private void updateAdapter() {
        lv_content.onFinishLoading(loadingMore, null);

        if(adapter == null) {
            adapter = new PartyAdapter();
            lv_content.setAdapter(adapter);
//            lv_content.setAdapter(adapter);

        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        loadFirst();
    }

    public class PartyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private DisplayImageOptions options;

        public PartyAdapter() {
            super();
            this.inflater = LayoutInflater.from(OpenActivity.this);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.placeholder_default)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
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

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                try {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.item_order, null);

                    holder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
                    holder.textView10 = (TextView) convertView.findViewById(R.id.textView10);
                    holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
                    holder.textView5 = (TextView) convertView.findViewById(R.id.textView5);
                    holder.textView6 = (TextView) convertView.findViewById(R.id.textView6);
                    holder.textView7 = (TextView) convertView.findViewById(R.id.textView7);

                    holder.linearLayout5 = (LinearLayout) convertView.findViewById(R.id.linearLayout5);
                    holder.editText1 = (TextView) convertView.findViewById(R.id.editText1);
                    holder.editText2 = (TextView) convertView.findViewById(R.id.editText2);
                    holder.textView11 = (TextView) convertView.findViewById(R.id.textView11);

                    convertView.setTag(holder);
                } catch (Exception e) {
                }
            }else{
                holder = (ViewHolder) convertView.getTag();
            }


            final Order data = cat_list.get(position);
            if(data != null) {
                holder.textView3.setText(data.getOrderTime());
                holder.textView10.setText(Html.fromHtml(data.getStatusStr()));

                holder.imageView1.setImageResource(data.getShippingRes());
                holder.textView5.setText(getString(R.string.str319) + data.getOrderSn());//订单号

                if (data.getCost_item() > 0) {
                    holder.textView7.setText(SysUtils.getMoneyFormat(data.getCost_item()));
                } else {
                    holder.textView7.setText("");
                }

                if(!TextUtils.isEmpty(data.getOrder_num())) {
                    holder.textView11.setText("#" + data.getOrder_num());
                    holder.textView11.setVisibility(View.VISIBLE);
                } else {
                    holder.textView11.setVisibility(View.GONE);
                }

                holder.linearLayout5.setVisibility(View.GONE);
            }

            return convertView;
        }
    }

    static class ViewHolder {
        public TextView textView3, textView10, textView5, textView6, textView7, textView11;
        public LinearLayout linearLayout5;
        public TextView editText1, editText2;
        public ImageView imageView1;
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
        if(!include_nowifi.isShown()) {
            lv_content.setVisibility(View.GONE);
            include_noresult.setVisibility(View.GONE);
            include_nowifi.setVisibility(View.VISIBLE);
            layout_err.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

//        SysUtils.hideSoftKeyboard(SearchActivity.this);
    }
}
