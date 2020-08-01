package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.entity.AddressCity;
import com.ui.util.CustomRequest;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressLocationActivity extends BaseActivity {
    private ListView list;
    List<AddressCity> l;
    BusAdapter myAdapter;
    private int provinceId = 0, cityId = 0, townId = 0;
    private int level = 0;
    private String provinceStr = "", cityStr = "", townStr = "", areaStr = "";
    private boolean isLoading = false;
    private RelativeLayout relativeLayout1;
    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_location);

        initToolbar(this);

        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        textView1 = (TextView) findViewById(R.id.textView1);

        l = new ArrayList<AddressCity>();
        list = (ListView) findViewById(R.id.listViewBusLine);

        myAdapter = new BusAdapter();
        list.setAdapter(myAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                int p = position;

                if (p >= 0 && p < l.size()) {
                    String id = l.get(p).getCid();
                    String name = l.get(p).getTitle();

                    if(level == 1 || level == 2) {
                        //省或城市找下一级
                        getCityList(id, name);
                    } else if(level == 3) {
                        //区县
                        townId = Integer.parseInt(id);
                        townStr = name;
                        updateAreaStr();
                        toAddressDetail();
                    }
                }
            }
        });

        getCityList("0", "");
    }

    private void toAddressDetail() {
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("provinceId", provinceId);
        bundle.putInt("cityId", cityId);
        bundle.putInt("townId", townId);
        bundle.putString("areaStr", areaStr);
        returnIntent.putExtras(bundle);

        setResult(RESULT_OK, returnIntent);


        onBackPressed();
    }


    private void toAddressDetail(String id, String title) {
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("city", title);
        returnIntent.putExtras(bundle);

        setResult(RESULT_OK, returnIntent);


        onBackPressed();
    }

    public class BusAdapter extends BaseAdapter {
        private int count;

        public int getCount() {
            return l.size();
        }

        public Object getItem(int position) {
            return l.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                try {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(AddressLocationActivity.this).inflate(R.layout.line_item, null);
                    holder.tv_title = (TextView) convertView.findViewById(R.id.area_name);
                    convertView.setTag(holder);
                } catch (Exception e) {
                }
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_title.setText(l.get(position).getTitle());


            return convertView;
        }

    }

    static class ViewHolder {
        public TextView tv_title;
    }


    private void getCityList(final String pid, final String name) {
        if(isLoading) {
            return;
        }

        isLoading = true;

        HashMap<String,String> finalMap = new HashMap<String,String>();

        String method = "";
        if (level > 0) {
            finalMap.put("p_region_id", pid);
            method = "p_region";
        } else {
            method = "regions";
        }

        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl(method), finalMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                isLoading = false;

                if(level == 1) {
                    //点到城市一级
                    provinceId = Integer.parseInt(pid);
                    provinceStr = name;
                } else if(level == 2) {
                    //点到区县一级
                    cityId = Integer.parseInt(pid);
                    cityStr = name;
                }
                updateAreaStr();

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (status.equals("200")) {
                        JSONArray array = dataObject.optJSONArray("data");
                        if (array != null && array.length() > 0) {
                            //有数据
                            l.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.optJSONObject(i);
                                AddressCity bean = new AddressCity();
                                bean.setCid(o.getString("region_id"));
                                bean.setTitle(o.getString("local_name"));
                                l.add(bean);
                            }
                            list.setAdapter(myAdapter);

                            level++;
                        } else {
                            //没有数据，尝试返回
                            toAddressDetail();
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                SysUtils.showNetworkError();
            }
        });

        executeRequest(r);
        showLoading(this);
    }

    private void updateAreaStr() {
        areaStr = "";
        if(provinceId > 0) {
            areaStr += provinceStr;

            if(cityId > 0) {
                areaStr += "/" + cityStr;

                if(townId > 0) {
                    areaStr += "/" + townStr;
                }
            }
        }

        if(!StringUtils.isEmpty(areaStr)) {
            textView1.setText(areaStr);
            relativeLayout1.setVisibility(View.VISIBLE);
        }
    }
}
