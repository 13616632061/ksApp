package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.adapter.GoodsListAdapter;
import com.ui.entity.GoodList_Item_check;
import com.ui.entity.Goods_info;
import com.ui.global.Global;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StockWarningActivity extends BaseActivity implements  AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private int type;
    private SwipeRefreshLayout refresh_header_goods;
    private PagingListView list_goods;
    private GoodsListAdapter goodsListAdapter;
    private ArrayList<Goods_info> goodsinfoList;
    private ArrayList<GoodList_Item_check> goodList_Item_check;
    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry,btn_goods_up;
    private TextView load_tv_noresult;
    private RelativeLayout goods_up_layout;
    private CheckBox check_allselect;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_warning);
        SysUtils.setupUI(this,findViewById(R.id.activity_stock_warning));
        initToolbar(this);

        initView();
        Intent intent=getIntent();
        if(intent!=null){
            type=intent.getIntExtra("type",1);
//            initData();
            if(type==1){
                setToolbarTitle("库存预警");
                goods_up_layout.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }else if(type==2){
                setToolbarTitle("缺货产品");
                goods_up_layout.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }else if(type==3){
                setToolbarTitle("下架商品");
                goods_up_layout.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
            }
        }
        initData();
    }

    private void initData() {
        goodsinfoList=new ArrayList<>();
        goodList_Item_check=new ArrayList<>();
        getselect_popwindowinfo();
    }

    /**
     * 获取选择弹窗商品信息
     */
    private  void getselect_popwindowinfo(){
        Map<String,String> map=new HashMap<>();
        map.put("type",type+"");
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("inventory_list"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
//                refresh_header_goods.setRefreshing(false);
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("分类ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject  data=null;
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        data=ret.getJSONObject("data");
                        JSONArray goods_info=data.getJSONArray("good_list");
                            goodsinfoList.clear();
                            goodList_Item_check.clear();
                        if (goods_info!=null&&goods_info.length()>0){
                            for(int i=0;i<goods_info.length();i++){
                                JSONObject jsonObject_goods_info=goods_info.getJSONObject(i);
                                Goods_info goodsInfo=new Goods_info(jsonObject_goods_info.getString("name"),jsonObject_goods_info.getString("marketable"),
                                        jsonObject_goods_info.getString("goods_id"),jsonObject_goods_info.getInt("buy_count"),
                                        Double.parseDouble(jsonObject_goods_info.getString("store")),jsonObject_goods_info.getDouble("price"),
                                        jsonObject_goods_info.getString("img_src"),jsonObject_goods_info.getInt("btn_switch_type"),0,"",false,0,"","");
                                goodsinfoList.add(goodsInfo);
                            }
                            for(int i=0;i<goodsinfoList.size();i++){
                                GoodList_Item_check mgoodList_Item_check=new GoodList_Item_check();
                                mgoodList_Item_check.setChecked(false);
                                goodList_Item_check.add(mgoodList_Item_check);
                            }
                        }
                        setView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    update_goodlistAdapter();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
//                refresh_header_goods.setRefreshing(false);
                SysUtils.showNetworkError();
                setNoNetwork();

            }
        });
        showLoading(StockWarningActivity.this);
        executeRequest(customRequest);
    }

    /**
     * 商品信息适配器
     */
    private void update_goodlistAdapter() {

//       / if(goodsListAdapter == null) {
            goodsListAdapter = new GoodsListAdapter(StockWarningActivity.this,goodsinfoList);
            list_goods.setAdapter(goodsListAdapter);
//        }else {
            goodsListAdapter.notifyDataSetChanged();
        if(type==3){
            goodsListAdapter.isisup(true);
        }
//        }
    }
    private void initView() {
        view=findViewById(R.id.view);
        list_goods= (PagingListView) findViewById(R.id.list_goods);
        goods_up_layout= (RelativeLayout) findViewById(R.id.goods_up_layout);
        btn_goods_up= (Button) findViewById(R.id.btn_goods_up);
        check_allselect= (CheckBox) findViewById(R.id.check_allselect);
//        refresh_header_goods= (SwipeRefreshLayout) findViewById(R.id.refresh_header_goods);
//        refresh_header_goods.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);
        layout_err = (View)findViewById(R.id.layout_err);
        include_noresult = layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_tv_noresult.setText("没有数据记录哦");
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
                getselect_popwindowinfo();
            }
        });

//        refresh_header_goods.setOnRefreshListener(this);
        list_goods.setOnItemClickListener(this);
        btn_goods_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upGoods();
            }
        });

        check_allselect.setOnCheckedChangeListener(this);
        check_allselect.setOnClickListener(this);
    }


    /**
     * 下拉刷新
     */
//    @Override
//    public void onRefresh() {
//        getselect_popwindowinfo();
//    }

    /**
     * 商品列表点击编辑
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(StockWarningActivity.this,AddGoodsActivity.class);
        intent.putExtra("type",3);
        intent.putExtra("goods_id",goodsinfoList.get(position).getGoods_id());
        startActivity(intent);
    }
    /**
     * 商品上架选择
     * @param checkid
     * @param ischeck
     * @return
     */
    private String checkid_str="";
    public  String checkid(String checkid,boolean ischeck){
        if(ischeck){
            checkid_str+=checkid+",";
        }else {
            if(checkid_str.length()>0){
//                checkid_str=checkid_str.subSequence(0,checkid_str.length()-checkid.length()-1).toString();
                if(checkid_str.contains(checkid)){
                    checkid_str=checkid_str.replace(checkid,"");
                }
            }
        }
        return checkid_str;
    }
    /**
     * 商品上架
     */
    private  void upGoods(){
        for(int i=0;i<goodsinfoList.size();i++){
            if(goodsinfoList.get(i).ischoose()){
                checkid(goodsinfoList.get(i).getGoods_id(),true);
            }

        }
        final Map<String,String> map=new HashMap<String, String>();
        if(checkid_str.length()<=0){
            SysUtils.showError("请选择需要上架的商品");
            return;
        }
        map.put("goods_id",checkid_str);
        map.put("type",1+"");
        System.out.println("map="+map);
        final CustomRequest customRequest=new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("edit_goods_out"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
              hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("商品ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject  data=null;
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                     String   goods_id=  map.get("goods_id");
                        String[] strArray=null;
                        strArray =  goods_id.split(","); //拆分字符为"," ,然后把结果交给数组strArray
                        for(int i=0;i<strArray.length;i++){
                            String good_id=strArray[i];
                            for(int j=0;j<goodsinfoList.size();j++){
                                if(goodsinfoList.get(j).getGoods_id().equals(good_id)){
                                    goodsinfoList.remove(j);
                                }
                                if(goodList_Item_check.get(j).isChecked()){
                                    goodList_Item_check.get(j).setChecked(false);
                                }
                            }
                        }
                        setView();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
//                    getSortlist();
//                    goodsinfoList.r
                    update_goodlistAdapter();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                SysUtils.showNetworkError();

            }
        });
        executeRequest(customRequest);
        showLoading(StockWarningActivity.this);
    }
    private void setView() {
            if(goodsinfoList.size() < 1) {
                //没有结果
                list_goods.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                include_noresult.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            } else {
                //有结果
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                layout_err.setVisibility(View.GONE);
                list_goods.setVisibility(View.VISIBLE);
            }
    }

    private void setNoNetwork() {
        //网络不通
        if(goodsinfoList.size() < 1) {
            if(!include_nowifi.isShown()) {
                list_goods.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            }
        } else {
            SysUtils.showNetworkError();
        }
    }
//全选按钮
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        StockWarningActivity.this.registerReceiver(broadcastReceiver,new IntentFilter(Global.BROADCAST_StockWarningActivity_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        StockWarningActivity.this.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_allselect:
                if(check_allselect.isChecked()){
                    for(int i=0;i<goodsinfoList.size();i++){
                        goodsinfoList.get(i).setIschoose(true);
                    }
                }else {
                    for(int i=0;i<goodsinfoList.size();i++){
                        goodsinfoList.get(i).setIschoose(false);
                    }
                }
                goodsListAdapter.notifyDataSetChanged();
                break;
        }
    }
}
