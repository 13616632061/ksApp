package com.ui.ks;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.adapter.HomePageFragmentNearlyShopperAdapter;
import com.ui.adapter.SearchHistoryRecordAdapter;
import com.ui.db.DBHelper;
import com.ui.entity.NearlyShopper;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShoppingHomePageSearchActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {

    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;

    private static final String[] m={"商家","商品"};
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private int type=0;//默认商家
    private TextView tv_search,tv_clear;
    private EditText et_input;
    private PagingListView list_content,list_history;
    private ImageView iv_back,iv_close;
    private RelativeLayout layout_history;
    private ArrayList<NearlyShopper> nearlyShoppers_list;
    private ArrayList<String> historyrecord_list;
    private SearchHistoryRecordAdapter searchHistoryRecordAdapter;
    private String searchhistoryrecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_home_page_search);
        SysUtils.setupUI(ShoppingHomePageSearchActivity.this,findViewById(R.id.activity_shopping_home_page_search));
        initToolbar(this);

        initView();
        initData();
    }

    private void initData() {
        Intent intent=getIntent();
        if(intent!=null){
            searchhistoryrecord=intent.getStringExtra("searchhistoryrecord");
            et_input.setText(searchhistoryrecord);
        }
        getSearchHistoryRecord();
    }

    private void initView() {
        layout_err = (View)findViewById(R.id.layout_err);
        include_noresult = layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_tv_noresult.setText("没有记录哦");
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
            }
        });
        tv_search= (TextView) findViewById(R.id.tv_search);
        tv_clear= (TextView) findViewById(R.id.tv_clear);
        et_input= (EditText) findViewById(R.id.et_input);
        list_content= (PagingListView) findViewById(R.id.list_content);
        list_history= (PagingListView) findViewById(R.id.list_history);
        iv_back= (ImageView) findViewById(R.id.iv_back);
        iv_close= (ImageView) findViewById(R.id.iv_close);
        layout_history= (RelativeLayout) findViewById(R.id.layout_history);


        tv_search.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_clear.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        list_history.setOnItemClickListener(this);
        list_content.setOnItemClickListener(this);
        et_input.addTextChangedListener(this);

        spinner= (Spinner) findViewById(R.id.spinner);
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

//        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                String input=et_input.getText().toString().trim();
                getSearchData(input);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_clear:
                clearSearchHistoryRecordDialog();
                break;
            case R.id.iv_close:
                et_input.setText("");
                iv_close.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.list_history:
                String input=historyrecord_list.get(position);
                et_input.setText(input);
                getSearchData(input);
                break;
            case R.id.list_content:
                if(type==0){
                    Intent intent=new Intent(ShoppingHomePageSearchActivity.this,NearlyShopperGoodsActivity.class);
                    intent.putExtra("shopperid",nearlyShoppers_list.get(position).getShopperid());
                    startActivity(intent);

                }else if(type==1) {
                    Intent intent = new Intent(ShoppingHomePageSearchActivity.this, NearlyShopperGoodsActivity.class);
                    intent.putExtra("shopperid", nearlyShoppers_list.get(position).getShopperid());
                    intent.putExtra("good_name", nearlyShoppers_list.get(position).getShoppername());
                    intent.putExtra("good_id", nearlyShoppers_list.get(position).getShoppernotes());
                    startActivity(intent);
                }
                break;
        }
    }
//搜索输入框输入变化
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String goodsname=s.toString().trim();
        if(goodsname.length()>0){
            iv_close.setVisibility(View.VISIBLE);
        }else {
            iv_close.setVisibility(View.GONE);
        }
    }

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener{

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            if(m[arg2].equals("商家")){
                type=0;
            }else {
                type=1;
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /**
     * 清空历史记录dialog
     */
    AlertDialog malertDialog_shoppingCar = null;
    private void clearSearchHistoryRecordDialog(){
    AlertDialog.Builder alertDialog=new AlertDialog.Builder(ShoppingHomePageSearchActivity.this,R.style.AlertDialog)
            .setMessage(getString(R.string.str35))
            .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clearSearchHistoryRecord();
                    getSearchHistoryRecord();
                    malertDialog_shoppingCar.dismiss();
                }
            })
            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    malertDialog_shoppingCar.dismiss();
                }
            });
    malertDialog_shoppingCar=alertDialog.show();
    malertDialog_shoppingCar.show();
}
    /**
     *记录搜索记录
     * @param name
     */
    private void addSearchHistoryRecord(String name){
        DBHelper dbHelper = DBHelper.getInstance(ShoppingHomePageSearchActivity.this);
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM searchhistoryrecords";
        Cursor cursor = sqlite.rawQuery(sql, null);
        System.out.println("cursor.getCount()="+cursor.getCount());
        if(cursor.getCount()>9){{
            sqlite.execSQL("delete from  searchhistoryrecords  where  name=(select name from searchhistoryrecords limit 1)");
        }}
        String sql_add = "SELECT * FROM searchhistoryrecords WHERE name = ?";
        Cursor cursor_add= sqlite.rawQuery(sql_add, new String[] {name});
        if(!cursor_add.moveToFirst()){
            sqlite.execSQL("insert into searchhistoryrecords (name) values(?)",
                    new Object[]{name});
        }
    }
    /**
     *清空搜索记录
     */
    private void clearSearchHistoryRecord(){
        DBHelper dbHelper = DBHelper.getInstance(ShoppingHomePageSearchActivity.this);
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = "DELETE FROM  searchhistoryrecords ";
        sqlite.execSQL(sql);
    }
    /**
     * 读取搜索历史记录
     */
    private void getSearchHistoryRecord(){
        if(historyrecord_list!=null){
            historyrecord_list.clear();
        }
        historyrecord_list=new ArrayList<>();
        DBHelper dbHelper = DBHelper.getInstance(ShoppingHomePageSearchActivity.this);
        SQLiteDatabase sqlite= dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM searchhistoryrecords";
        Cursor cursor = sqlite.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                historyrecord_list.add(name);
            } while (cursor.moveToNext());
        }
        Collections.reverse(historyrecord_list);
        searchHistoryRecordAdapter=new SearchHistoryRecordAdapter(ShoppingHomePageSearchActivity.this,historyrecord_list);
        list_history.setAdapter( searchHistoryRecordAdapter);

        cursor.close();
        sqlite.close();
    }
    /**
     * 获取数据
     */
    private void getSearchData(String input) {
        if(TextUtils.isEmpty(input)){
            Toast.makeText(ShoppingHomePageSearchActivity.this,getString(R.string.str48),Toast.LENGTH_SHORT).show();
            return;
        }
        addSearchHistoryRecord(input);//加入历史记录
        Map<String,String> map=new HashMap<>();
        if(type==0){
            map.put("seller",input);
        }else {
            map.put("goods",input);
        }
        System.out.println("map="+map);
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("supplier_search"),map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("搜索结果："+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data = ret.getJSONObject("data");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        JSONArray result_info=data.getJSONArray("result_info");
                        if(nearlyShoppers_list!=null){
                            nearlyShoppers_list.clear();
                        }
                        if(type==0) {
                            if (result_info != null) {
                                nearlyShoppers_list = new ArrayList<>();
                                for (int i = 0; i < result_info.length(); i++) {
                                    JSONObject jsonObject1 = result_info.getJSONObject(i);
                                    String range = jsonObject1.getString("range");
                                    String seller_id = jsonObject1.getString("seller_id");
                                    String seller_name = jsonObject1.getString("seller_name");
                                    String logo = jsonObject1.getString("logo");
                                    String intro = jsonObject1.getString("intro");
                                    NearlyShopper nearlySeller_list = new NearlyShopper(seller_id, logo, seller_name, range, intro);
                                    nearlyShoppers_list.add(nearlySeller_list);
                                }
                                layout_history.setVisibility(View.GONE);
                                list_content.setVisibility(View.VISIBLE);
                                list_content.setAdapter(new HomePageFragmentNearlyShopperAdapter(ShoppingHomePageSearchActivity.this, nearlyShoppers_list));
                            }
                        }else if(type==1){
                            if (result_info != null) {
                                nearlyShoppers_list = new ArrayList<>();
                                for (int i = 0; i < result_info.length(); i++) {
                                    JSONObject jsonObject1 = result_info.getJSONObject(i);
                                    String goods_id= jsonObject1.getString("goods_id");
                                    String seller_id = jsonObject1.getString("seller_id");
                                    String name = jsonObject1.getString("name");
                                    String img_src = jsonObject1.getString("img_src");
                                    String price = jsonObject1.getString("price");
                                    if(price!=null){
                                        price=SetEditTextInput.stringpointtwo(price);
                                    }else {
                                        price="0.00";
                                    }
                                    NearlyShopper nearlySeller_list = new NearlyShopper(seller_id, img_src, name, "￥"+price,goods_id);
                                    nearlyShoppers_list.add(nearlySeller_list);
                                }
                                layout_history.setVisibility(View.GONE);
                                list_content.setVisibility(View.VISIBLE);
                                list_content.setAdapter(new HomePageFragmentNearlyShopperAdapter(ShoppingHomePageSearchActivity.this, nearlyShoppers_list));
                            }
                        }
                        setView();
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                SysUtils.showError("网络不给力");
            }
        });
        executeRequest(r);
    }
    private void setView() {
            if(nearlyShoppers_list.size() < 1) {
                //没有结果
                list_content.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                include_noresult.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            } else {
                //有结果
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                layout_err.setVisibility(View.GONE);
                list_content.setVisibility(View.VISIBLE);
            }
    }

    private void setNoNetwork() {
        //网络不通
        if(nearlyShoppers_list.size() < 1) {
            if(!include_nowifi.isShown()) {
                list_content.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            }
        } else {
            SysUtils.showNetworkError();
        }
    }
}
