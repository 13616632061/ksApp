package com.ui.ks.GoodsSearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.base.mvp.BaseActivity;
import com.library.utils.BigDecimalArith;
import com.ui.adapter.GoodsListAdapter;
import com.ui.entity.GoodList_Item_check;
import com.ui.entity.Goods_Sales;
import com.ui.entity.Goods_info;
import com.ui.global.Global;
import com.ui.ks.AddGoodsActivity;
import com.ui.ks.GoodsSearch.adapter.GoodsInfoSearchAdapter;
import com.ui.ks.GoodsSearch.contract.GoodsSearchContract;
import com.ui.ks.GoodsSearch.presenter.GoodsInfoSearchPresenter;
import com.ui.ks.R;
import com.ui.ks.ReportLoss.adapter.GoodsInfoAdapter;
import com.ui.ks.SalesStatistics.SalesStatisticsAcitvity;
import com.ui.util.CustomRequest;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品搜索
 */

public class GoodsInfoSearchActivity extends BaseActivity implements GoodsSearchContract.View {

    private static final String TAG = GoodsInfoSearchActivity.class.getSimpleName();


    @BindView(R.id.et_search_content)
    EditText etSearchContent;
    @BindView(R.id.list)
    RecyclerView list;


    private CaptureFragment captureFragment;
    private GoodsInfoSearchPresenter mPresenter;

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_goods_info_search);
//        ButterKnife.bind(this);
//        SysUtils.setupUI(this, findViewById(R.id.activity_goods_info_search));
//
//        Intent intent = getIntent();
//        if (intent != null) {
//            type = intent.getIntExtra("type", 0);
//        }
//
//        initView();
//    }

    @Override
    public int getContentView() {
        return R.layout.activity_goods_info_search;
    }

    @Override
    protected void initView() {
        initTabTitle(getResources().getString(R.string.str383), "");//搜索
        initScan();
        mPresenter = new GoodsInfoSearchPresenter(this);
        mPresenter.initAdapter();
    }

    @OnClick({R.id.btn_search})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search://搜索
                mPresenter.searchGoodsInfo(getSearchContent());
                break;
        }
    }


//    private void downGoods() {
//        String inputgoodname_src = et_inputgoodname.getText().toString().trim();
//        String url = null;
//        if (TextUtils.isEmpty(inputgoodname_src)) {
//            SysUtils.showError(getString(R.string.str48));
//            return;
//        }
//        Map<String, String> map = new HashMap<String, String>();
//        if (type == 2) {
//            map.put("name", et_inputgoodname.getText().toString().trim());
//            url = SysUtils.getGoodsServiceUrl("count_goods");
//        } else {
//            map.put("search", et_inputgoodname.getText().toString().trim());
//            map.put("page", 1 + "");
//            url = SysUtils.getGoodsServiceUrl("goods_search");
//        }
//        CustomRequest customRequest = new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                hideLoading();
//                try {
//                    JSONObject ret = SysUtils.didResponse(jsonObject);
//                    System.out.println("ret=" + ret);
//                    String status = ret.getString("status");
//                    String message = ret.getString("message");
//                    if (!status.equals("200")) {
//                        SysUtils.showError(message);
//                    } else {
//                        if (type == 2) {
//                            JSONArray data = ret.getJSONArray("data");
//                            goods_sales_list = new ArrayList<>();
//                            for (int i = 0; i < data.length(); i++) {
//                                JSONObject jsonObject1 = data.getJSONObject(i);
//                                String goods_id = jsonObject1.getString("goods_id");
//                                String goods_name = jsonObject1.getString("goods_name");
//                                String cost = jsonObject1.getString("cost");
//                                img_src = jsonObject1.getString("img_src");
//                                double total = jsonObject1.getDouble("total");
//                                int nums = jsonObject1.getInt("nums");
//                                double total_cost = Double.parseDouble(SetEditTextInput.stringpointtwo(cost));
//                                total_cost = BigDecimalArith.mul(Double.parseDouble(cost), nums);
//                                double profit = BigDecimalArith.sub(total, total_cost);
//                                double profit_percent = BigDecimalArith.div(profit, total, 2);
//
//                                Goods_Sales goods_sales = new Goods_Sales(goods_name, nums, total, profit, SetEditTextInput.stringpointtwo((profit_percent * 100) + "") + "%", img_src);
//                                goods_sales_list.add(goods_sales);
//
//                            }
//                            GoodsInfoSearchActivity.this.sendBroadcast(new Intent(Global.BROADCAST_Goods_Sales_StatisticsAcitvity_ACTION));
//                            GoodsInfoSearchActivity.this.sendBroadcast(new Intent(Global.BROADCAST_Goods_Sales_Statistics_SearchActivity_ACTION));
//                            Intent intent = new Intent(GoodsInfoSearchActivity.this, SalesStatisticsAcitvity.class);
//                            intent.putExtra("type", 2);
//                            intent.putParcelableArrayListExtra("goods_sales_list", goods_sales_list);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            JSONObject data = null;
//                            goodsinfoList.clear();
//                            goodList_Item_check.clear();
//                            data = ret.getJSONObject("data");
//                            JSONArray goods_info = data.getJSONArray("goods_info");
//                            System.out.println("分类goods_info=" + goods_info);
//                            if (goods_info != null && goods_info.length() > 0) {
//                                for (int i = 0; i < goods_info.length(); i++) {
//                                    JSONObject jsonObject_goods_info = goods_info.getJSONObject(i);
//                                    Log.d("print", "打印商品数据" + Float.parseFloat(jsonObject_goods_info.getString("store")));
//                                    Goods_info goodsInfo = new Goods_info(jsonObject_goods_info.getString("name"), jsonObject_goods_info.getString("marketable"),
//                                            jsonObject_goods_info.getString("goods_id"), jsonObject_goods_info.getInt("buy_count"),
//                                            Double.parseDouble(jsonObject_goods_info.getString("store")), jsonObject_goods_info.getDouble("price"),
//                                            jsonObject_goods_info.getString("img_src"), jsonObject_goods_info.getInt("btn_switch_type"), 0, "", false, 0, "", "");
//                                    goodsinfoList.add(goodsInfo);
//                                }
//                                for (int i = 0; i < goodsinfoList.size(); i++) {
//                                    GoodList_Item_check mgoodList_Item_check = new GoodList_Item_check();
//                                    mgoodList_Item_check.setChecked(false);
//                                    goodList_Item_check.add(mgoodList_Item_check);
//                                }
//                            }
//                            setView();
//                            update_goodlistAdapter();
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } finally {
////                    setView();
////                    update_goodlistAdapter();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                hideLoading();
//                SysUtils.showNetworkError();
//                setNoNetwork();
//
//            }
//        });
//        executeRequest(customRequest);
//        showLoading(GoodsInfoSearchActivity.this);
//    }


    /**
     * @Description:搜索输入框内容
     * @Author:lyf
     * @Date: 2020/8/21
     */
    @Override
    public String getSearchContent() {
        return etSearchContent.getText().toString().trim();
    }

    /**
     * @Description:初始化适配器
     * @Author:lyf
     * @Date: 2020/8/21
     */
    @Override
    public GoodsInfoSearchAdapter initAdapter() {
        GoodsInfoSearchAdapter adapter = new GoodsInfoSearchAdapter(R.layout.item_search_goods, mPresenter.getmData());
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                goToGoodsDetail(mPresenter.getmData().get(position).getGoods_id());
            }
        });
        return adapter;
    }

    /**
     * @Description:空视图
     * @Author:lyf
     * @Date: 2020/8/15
     */
    @Override
    public View setEmptyView() {
        View view = View.inflate(this, R.layout.layout_empty_view, null);
        return view;
    }

    /**
     * @Description:描述
     * @Author:lyf
     * @Date: 2020/8/21
     */
    @Override
    public void goToGoodsDetail(String goodsId) {
        Intent intent = new Intent(GoodsInfoSearchActivity.this, AddGoodsActivity.class);
        intent.putExtra("type", 3);
        intent.putExtra("goods_id", goodsId);
        startActivity(intent);
    }

    /**
     * @Description:初始化扫码
     * @Author:lyf
     * @Date: 2020/8/21
     */
    @Override
    public void initScan() {
        /**
         * 执行扫面Fragment的初始化操作
         */
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);

        captureFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_scan, captureFragment).commit();
    }


    /**
     * @Description:二维码/条码解析回调函数
     * @Author:lyf
     * @Date: 2020/7/19
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            LogUtils.d(TAG + " scan result: " + result);
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            //扫描成功后，初始化触发再次扫描
            captureFragment.getHandler().sendEmptyMessageDelayed(com.uuzuche.lib_zxing.R.id.restart_preview, 1500);
            mPresenter.searchGoodsInfo(result);
        }

        @Override
        public void onAnalyzeFailed() {
            LogUtils.d(TAG + "  result: onAnalyzeFailed");

        }
    };
}
