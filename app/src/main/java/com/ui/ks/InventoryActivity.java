package com.ui.ks;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.MyApplication.KsApplication;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.constant.RouterPath;
import com.ui.adapter.GoodSortListAdapter;
import com.ui.adapter.GoodsInventoryListAdapter;
import com.ui.entity.GoodSort;
import com.ui.entity.Goods_Inventory;
import com.ui.entity.Inventory_classification;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 盘点
 * Created by Administrator on 2020/3/2.
 */

public class InventoryActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_set)
    TextView btnSet;
    //分类的数据显示
    PagingListView lv_classification;
    //商品的数据
    PagingListView lv_content;
    //分类的列表
    List<Inventory_classification> inventory_classifications = new ArrayList<>();


    private List<GoodSort> goodsortList = new ArrayList<>();
    GoodSortListAdapter mgoodSortListAdapter;

    //商品的adapter
    private GoodsInventoryListAdapter mgoodsInventoryListadapter;

    List<Goods_Inventory> goods_inventories = new ArrayList<>();

    Button btn_submission;

    private Dialog progressDialog = null;

    private int curPosition = 0;//当前选择的分类


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ButterKnife.bind(this);
        SysUtils.setupUI(this, findViewById(R.id.inventory_activity));
        initToolbar(this);
        setToolbarTitle(getString(R.string.inventory));
        initview();
        lodatas();
    }

    @OnClick({R.id.btn_set})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set://提交记录
                ARouter.getInstance().build(RouterPath.ACTIVITY_INVENTORY_RECORD).navigation();
                break;
        }
    }

    //初始化布局
    private void initview() {
        lv_classification = (PagingListView) findViewById(R.id.lv_classification);
        lv_content = (PagingListView) findViewById(R.id.lv_content);
        btn_submission = (Button) findViewById(R.id.btn_submission);
        btn_submission.setOnClickListener(this);
        btnSet.setVisibility(View.VISIBLE);
        btnSet.setText(getResources().getString(R.string.str374));//盘点记录

        /**
         * 分类的点击事件
         */
        lv_classification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                curPosition = i;
                itmeOnClick(goodsortList.get(i).getId());
                mgoodSortListAdapter.setDefSelect(i);
            }
        });

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                DialogUtils.ShowInventory(InventoryActivity.this, goods_inventories.get(i));
                DialogUtils dialogUtils = new DialogUtils();
                dialogUtils.SetOnAddInventoryStock(new DialogUtils.OnAddInventoryStock() {
                    @Override
                    public void addInventoryStock(String InventoryStock) {
                        AddInventoryStock(InventoryStock, i);
                    }
                });
            }
        });
    }

    //盘点的数据
    public void AddInventoryStock(final String InventoryStock, final int i) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("reality_store", InventoryStock);
        map.put("goods_id", goods_inventories.get(i).getGoods_id());
        map.put("store", goods_inventories.get(i).getStore());
        String uri = SysUtils.getGoodspinbanServiceUrl("take_stock");
        Log.d("print", "打印出来的盘点的库存" + map.toString());
        final CustomRequest r = new CustomRequest(Request.Method.POST, uri, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("print", "打印出来的盘点的库存" + response.toString());
                try {
                    JSONObject ret = SysUtils.didResponse(response);
                    System.out.println("ret=" + ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = null;
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        goods_inventories.get(i).setReality_store(InventoryStock);
//                        mgoodsInventoryListadapter = new GoodsInventoryListAdapter(InventoryActivity.this,goods_inventories);
                        lv_content.setAdapter(mgoodsInventoryListadapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        executeRequest(r);
    }


    //加载盘点的分类
    private void lodatas() {
        showLoading(InventoryActivity.this, "正在加载数据...");
        Map<String, String> map = new HashMap<String, String>();
        map.put("seller_id", KsApplication.getInt("seller_id", 0) + "");
        String uri = SysUtils.getGoodspinbanServiceUrl("cat_getlist");
        CustomRequest r = new CustomRequest(Request.Method.POST, uri, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    hideLoading();
                    JSONObject ret = SysUtils.didResponse(response);
                    System.out.println("ret=" + ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = null;
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        dataObject = ret.getJSONObject("data");
                        JSONArray nav_info = dataObject.getJSONArray("nav_info");
                        for (int i = 0; i < nav_info.length(); i++) {
                            JSONObject jsonObject = nav_info.getJSONObject(i);
                            GoodSort goodSort = new GoodSort(jsonObject.getString("tag_name"), jsonObject.getString("tag_id"), 0);
                            goodsortList.add(goodSort);
                        }
                        mgoodSortListAdapter = new GoodSortListAdapter(InventoryActivity.this, (ArrayList<GoodSort>) goodsortList);
                        lv_classification.setAdapter(mgoodSortListAdapter);
                        itmeOnClick(goodsortList.get(0).getId());
                        mgoodSortListAdapter.setDefSelect(0);
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

    public void itmeOnClick(String tag_id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("tag_id", tag_id);
        map.put("pagelimit", "1000");
        String uri = SysUtils.getGoodspinbanServiceUrl("goods_pb");
        CustomRequest r = new CustomRequest(Request.Method.POST, uri, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("print", "打印出的商品数据" + response.toString());
                JSONObject ret = SysUtils.didResponse(response);
                String status = null;
                goods_inventories.clear();
                try {
                    status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = null;
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        dataObject = ret.getJSONObject("data");
                        JSONArray goods_info = dataObject.getJSONArray("goods_info");
                        for (int j = 0; j < goods_info.length(); j++) {
                            JSONObject jsonObject = goods_info.getJSONObject(j);
                            Goods_Inventory goods_inventory = new Goods_Inventory();
                            goods_inventory.setName(jsonObject.getString("name"));
                            goods_inventory.setCost(jsonObject.getString("cost"));
                            goods_inventory.setStore(jsonObject.getString("store"));
                            goods_inventory.setBncode(jsonObject.getString("bncode"));
                            goods_inventory.setGoods_id(jsonObject.getString("goods_id"));
                            goods_inventory.setReality_store(jsonObject.getString("reality_store"));
                            goods_inventory.setImg_src(jsonObject.getString("img_src"));
                            goods_inventories.add(goods_inventory);
                        }
                        mgoodsInventoryListadapter = new GoodsInventoryListAdapter(InventoryActivity.this, goods_inventories);
                        lv_content.setAdapter(mgoodsInventoryListadapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        executeRequest(r);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submission:
                DialogUtils.SetDialog(InventoryActivity.this);
                DialogUtils dialogUtils = new DialogUtils();
                dialogUtils.SetOnDeterminecancel(new DialogUtils.OnDeterminecancel() {
                    @Override
                    public void Determinecancel() {
                        updatas();
                    }
                });
                break;
        }
    }


    public void updatas() {
        showLoading(InventoryActivity.this, "正在上传数据...");
        String uri = SysUtils.getGoodspinbanServiceUrl("addStoreRecords");
        CustomRequest r = new CustomRequest(Request.Method.POST, uri, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("print", "onResponse: 打印出来的盘点数据" + response.toString());
                JSONObject ret = SysUtils.didResponse(response);
                String status = null;
                hideLoading();
                try {
                    status = ret.getString("status");
                    String message = ret.getString("message");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                        itmeOnClick(goodsortList.get(curPosition).getId());
                    } else {

                    }
                } catch (Exception e) {
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
