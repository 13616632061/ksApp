package com.ui.ks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.adapter.GoodsListAdapter;
import com.ui.entity.GoodList_Item_check;
import com.ui.entity.Goods_Sales;
import com.ui.entity.Goods_info;
import com.ui.global.Global;
import com.ui.ks.SalesStatistics.SalesStatisticsAcitvity;
import com.ui.listview.PagingListView;
import com.library.utils.BigDecimalArith;
import com.ui.util.CustomRequest;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 商品搜索页面
 */

public class GoodsInfoSearchActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {

    private ImageView iv_back,iv_cell,iv_search;
    private EditText et_inputgoodname;
    private PagingListView list_search_reaslt;
    private ArrayList<Goods_info> goodsinfoList;
    private ArrayList<GoodList_Item_check> goodList_Item_check;
    private GoodsListAdapter goodsListAdapter;
    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info_search);
        SysUtils.setupUI(this,findViewById(R.id.activity_goods_info_search));
        initToolbar(this);

        Intent intent=getIntent();
        if(intent!=null){
            type=intent.getIntExtra("type",0);
        }

        initView();
    }


    private void initView() {
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
                downGoods();
            }
        });
        iv_back= (ImageView) findViewById(R.id.iv_back);
        iv_cell= (ImageView) findViewById(R.id.iv_cell);
        iv_search= (ImageView) findViewById(R.id.iv_search);
        et_inputgoodname= (EditText) findViewById(R.id.et_inputgoodname);
        list_search_reaslt= (PagingListView) findViewById(R.id.list_search_reaslt);

        iv_back.setOnClickListener(this);
        iv_cell.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        et_inputgoodname.setOnClickListener(this);
        list_search_reaslt.setOnItemClickListener(this);
        et_inputgoodname.addTextChangedListener(this);

        autoUpKeyboard();
        goodsinfoList=new ArrayList<>();
        goodList_Item_check=new ArrayList<>();

        et_inputgoodname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    downGoods();
                }
                return false;
            }
        });
    }

    /**
     * 自动弹出软键盘
     */
    private void autoUpKeyboard() {
        et_inputgoodname.requestFocus();
        et_inputgoodname.setFocusable(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                InputMethodManager inputManager = (InputMethodManager)et_inputgoodname.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_inputgoodname, 0);
            }
        }, 300);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cell:
                et_inputgoodname.setText("");
                break;
            case R.id.iv_search:
                downGoods();
                break;
            case R.id.et_inputgoodname:
                break;
        }
    }
    /**
     * 商品搜索
     */
    private ArrayList<Goods_Sales> goods_sales_list;
    private  String img_src;
    private  void downGoods(){
        String inputgoodname_src=et_inputgoodname.getText().toString().trim();
        String url = null;
        if(TextUtils.isEmpty(inputgoodname_src)){
            SysUtils.showError(getString(R.string.str48));
            return;
        }
        Map<String,String> map=new HashMap<String, String>();
        if(type==2){
            map.put("name",et_inputgoodname.getText().toString().trim());
            url=SysUtils.getGoodsServiceUrl("count_goods");
        }else {
            map.put("search",et_inputgoodname.getText().toString().trim());
            map.put("page",1+"");
            url=SysUtils.getGoodsServiceUrl("goods_search");
        }
        CustomRequest customRequest=new CustomRequest(Request.Method.POST,url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        if (type == 2) {
                            JSONArray data=ret.getJSONArray("data");
                            goods_sales_list=new ArrayList<>();
                            for(int i=0;i<data.length();i++){
                                JSONObject jsonObject1=data.getJSONObject(i);
                                String goods_id=jsonObject1.getString("goods_id");
                                String goods_name=jsonObject1.getString("goods_name");
                                String cost=jsonObject1.getString("cost");
                                img_src=jsonObject1.getString("img_src");
                                double total=jsonObject1.getDouble("total");
                                int nums=jsonObject1.getInt("nums");
                                double total_cost=Double.parseDouble(SetEditTextInput.stringpointtwo(cost));
                                total_cost= BigDecimalArith.mul(Double.parseDouble(cost),nums);
                                double profit=BigDecimalArith.sub(total,total_cost);
                                double profit_percent=BigDecimalArith.div(profit,total,2);

                                Goods_Sales goods_sales=new Goods_Sales(goods_name,nums,total,profit,SetEditTextInput.stringpointtwo((profit_percent*100)+"")+"%",img_src);
                                goods_sales_list.add(goods_sales);

                            }
                            GoodsInfoSearchActivity.this.sendBroadcast(new Intent(Global.BROADCAST_Goods_Sales_StatisticsAcitvity_ACTION));
                            GoodsInfoSearchActivity.this.sendBroadcast(new Intent(Global.BROADCAST_Goods_Sales_Statistics_SearchActivity_ACTION));
                            Intent intent=new Intent( GoodsInfoSearchActivity.this,SalesStatisticsAcitvity.class);
                            intent.putExtra("type",2);
                            intent.putParcelableArrayListExtra("goods_sales_list",goods_sales_list);
                            startActivity(intent);
                            finish();
                        } else {
                            JSONObject  data=null;
                            goodsinfoList.clear();
                            goodList_Item_check.clear();
                            data = ret.getJSONObject("data");
                            JSONArray goods_info = data.getJSONArray("goods_info");
                            System.out.println("分类goods_info=" + goods_info);
                            if (goods_info != null && goods_info.length() > 0) {
                                for (int i = 0; i < goods_info.length(); i++) {
                                    JSONObject jsonObject_goods_info = goods_info.getJSONObject(i);
                                    Log.d("print","打印商品数据"+ Float.parseFloat(jsonObject_goods_info.getString("store")));
                                    Goods_info goodsInfo = new Goods_info(jsonObject_goods_info.getString("name"), jsonObject_goods_info.getString("marketable"),
                                            jsonObject_goods_info.getString("goods_id"), jsonObject_goods_info.getInt("buy_count"),
                                            Double.parseDouble(jsonObject_goods_info.getString("store")), jsonObject_goods_info.getDouble("price"),
                                            jsonObject_goods_info.getString("img_src"), jsonObject_goods_info.getInt("btn_switch_type"), 0, "", false, 0, "", "");
                                    goodsinfoList.add(goodsInfo);
                                }
                                for (int i = 0; i < goodsinfoList.size(); i++) {
                                    GoodList_Item_check mgoodList_Item_check = new GoodList_Item_check();
                                    mgoodList_Item_check.setChecked(false);
                                    goodList_Item_check.add(mgoodList_Item_check);
                                }
                            }
                            setView();
                            update_goodlistAdapter();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
//                    setView();
//                    update_goodlistAdapter();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                SysUtils.showNetworkError();
                setNoNetwork();

            }
        });
        executeRequest(customRequest);
        showLoading(GoodsInfoSearchActivity.this);
    }
    /**
     * 商品信息适配器
     */
    private void update_goodlistAdapter() {
//        list_sort.onFinishLoading(loadingMore, null);

        if(goodsListAdapter == null) {
            goodsListAdapter = new GoodsListAdapter(GoodsInfoSearchActivity.this,goodsinfoList);
            list_search_reaslt.setAdapter(goodsListAdapter);
        }else {
            goodsListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * listview的点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(GoodsInfoSearchActivity.this,AddGoodsActivity.class);
        intent.putExtra("type",3);
        intent.putExtra("goods_id",goodsinfoList.get(position).getGoods_id());
        startActivity(intent);
    }

    /**
     * 监听输入框输入的变化
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String goodsname=s.toString().trim();
        if(goodsname.length()>0){
            iv_cell.setVisibility(View.VISIBLE);
        }else {
            iv_cell.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    private void setView() {
        if(goodsinfoList.size() < 1) {
            //没有结果
            list_search_reaslt.setVisibility(View.GONE);
            include_nowifi.setVisibility(View.GONE);
            include_noresult.setVisibility(View.VISIBLE);
            layout_err.setVisibility(View.VISIBLE);
        } else {
            //有结果
            include_noresult.setVisibility(View.GONE);
            include_nowifi.setVisibility(View.GONE);
            layout_err.setVisibility(View.GONE);
            list_search_reaslt.setVisibility(View.VISIBLE);
        }
    }

    private void setNoNetwork() {
        //网络不通
        if(goodsinfoList.size() < 1) {
            if(!include_nowifi.isShown()) {
                list_search_reaslt.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            }
        } else {
            SysUtils.showNetworkError();
        }
    }

}
