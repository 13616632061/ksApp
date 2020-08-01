package com.ui.ks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.google.gson.Gson;
import com.ui.adapter.MarketingAdapter;
import com.ui.entity.MemberSpecifications;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.DialogUtils;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2020/3/10.
 */

public class MemberSpecificationsActivity extends BaseActivity implements View.OnClickListener {


    PagingListView lv_content;
    List<MemberSpecifications> datas=new ArrayList<>();
    MarketingAdapter adapter;
    ImageView iv_managmentfragment_back;
    TextView tv_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specifocations);
//        setContentView(R.layout.activity_pic_view);
        initToolbar(this);

        initView();

        LoadAdats();
    }

    //初始化
    private void initView() {
        lv_content= (PagingListView) findViewById(R.id.lv_content);
        View view= LayoutInflater.from(MemberSpecificationsActivity.this).inflate(R.layout.itme_marketing,null);
        lv_content.addHeaderView(view);
        iv_managmentfragment_back= (ImageView) findViewById(R.id.iv_managmentfragment_back);
        iv_managmentfragment_back.setOnClickListener(this);
        tv_add= (TextView) findViewById(R.id.tv_add);
        tv_add.setOnClickListener(this);
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>=1) {
                    DialogUtils.ShowAddSpecifications(MemberSpecificationsActivity.this, datas.get(i - 1));
                    DialogUtils dialog = new DialogUtils();
                    dialog.SetOnAddSpecifications(new DialogUtils.OnAddSpecifications() {
                        @Override
                        public void AddSpecifications(String recharge, String give, String id) {
                            setAddSpecifications(recharge, give, id);
                        }
                        @Override
                        public void DeleteSpecifications(String id) {
                            setDeleteSpecifications(id);
                        }
                    });
                }
            }
        });
    }

    //加载数据
    public void LoadAdats(){
            Map<String,String> map=new HashMap<>();
            map.put("type","1");
            CustomRequest r=new CustomRequest(Request.Method.POST, SysUtils.getSellerpinbanServiceUrl("recharge_list"), map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        hideLoading();
                        Log.d("print","打印出来的会员充值的数据"+response.toString());
                        JSONObject ret=SysUtils.didResponse(response);
                        String status = ret.getString("status");
                        String message = ret.getString("message");
                        JSONArray data = null;
                        if (!status.equals("200")) {
                            SysUtils.showError(message);
                        } else {
                            data=ret.getJSONArray("data");
                            analysis(data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
            }
        });
            executeRequest(r);
            showLoading(MemberSpecificationsActivity.this);
    }

    //解析
    public void analysis(JSONArray data){
        datas.clear();
        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                MemberSpecifications memberSpecifications = new MemberSpecifications();
                memberSpecifications.setGive(object.getString("give"));
                memberSpecifications.setGids(object.getString("gids"));
                memberSpecifications.setIs_show(object.getString("is_show"));
                memberSpecifications.setRecharge_id(object.getString("recharge_id"));
                memberSpecifications.setType(object.getString("type"));
                memberSpecifications.setVal(object.getString("val"));
                datas.add(memberSpecifications);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            adapter=new MarketingAdapter(MemberSpecificationsActivity.this,datas);
            lv_content.setAdapter(adapter);
        }
    }

    //打印出来
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_managmentfragment_back:
                finish();
                break;
            case R.id.tv_add:
                DialogUtils.ShowAddSpecifications(MemberSpecificationsActivity.this,null);
                DialogUtils dialog = new DialogUtils();
                dialog.SetOnAddSpecifications(new DialogUtils.OnAddSpecifications() {
                    @Override
                    public void AddSpecifications(String recharge, String give, String id) {
                            setAddSpecifications(recharge,give,id);
                    }
                    @Override
                    public void DeleteSpecifications(String id) {

                    }

                });
                break;
        }
    }

    //添加编辑规格
    public void setAddSpecifications(String val,String give,String id){
        Map<String,String> map=new HashMap<>();
        map.put("val",val);
        map.put("give",give);
        map.put("is_show","yes");
        map.put("type","1");
        if (!id.equals("")){
            map.put("recharge_id",id);
        }
        Gson gson=new Gson();
        String str=gson.toJson(map);
        Map<String,String> map1=new HashMap<>();
        map1.put("map",str);
        String url=SysUtils.getSellerpinbanServiceUrl("recharge_type_add");
        CustomRequest r=new CustomRequest(Request.Method.POST, url, map1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("print", "onResponse: 打印出来新增的规格"+response.toString());
                hideLoading();
                try {
                    JSONObject ret=SysUtils.didResponse(response);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        LoadAdats();
                        SysUtils.showSuccess("添加成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
            }
        });
        executeRequest(r);
        showLoading(MemberSpecificationsActivity.this);
    }

    //删除规格
    public void setDeleteSpecifications(String id){
        Map<String,String> map=new HashMap<>();
        map.put("recharge_id",id);
        String url = SysUtils.getSellerpinbanServiceUrl("recharge_del");
        CustomRequest r=new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                try {
                    JSONObject ret=SysUtils.didResponse(response);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        LoadAdats();
                        SysUtils.showSuccess("删除成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
            }
        });
        executeRequest(r);
    }


}
