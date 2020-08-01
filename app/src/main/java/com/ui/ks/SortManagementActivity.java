package com.ui.ks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.global.Global;
import com.ui.recyclerviewleftslideremove.DividerDecoration;
import com.ui.adapter.GoodSortAdapter;
import com.ui.entity.GoodSort;
import com.ui.listview.LoadingView;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 分类管理页面
 */
public class SortManagementActivity extends BaseActivity implements View.OnClickListener,GoodSortAdapter.IonSlidingViewClickListener {
    private ImageView iv_back;
    private TextView tv_edit;
    private Button btn_addsort;
    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;
    private  EditText et_accountinfo_input;
    private  InputMethodManager imm;
    private AlertDialog mAlertDialog;
    private ArrayList<GoodSort> goodsortList;
    private GoodSortAdapter goodSortAdapter;
    private RecyclerView list_addsort;
    private int total=0;
    private int page=1;
    private int NUM_PER_PAGE = 20;//加载的条数
    boolean loadingMore = false;//是否加载更多
    private LoadingView loadingView;
    private LinearLayoutManager layoutManager;
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_management);
        SysUtils.setupUI(this,findViewById(R.id.activity_sort_management));
        initToolbar(this);

        initView();
        getData();

        Intent intent=getIntent();
        if(intent!=null){
            type=intent.getIntExtra("type",1);
        }

    }

    private void getData() {
        goodsortList=new ArrayList<>();
//        getSortlist();
////        arrayList=new ArrayList<Map<String, String>>();
//        Map<String, String> map=new HashMap<String, String>();
//        list_addsort.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (goodSortAdapter.menuIsOpen()) {
//                    goodSortAdapter.closeMenu();//关闭菜单
//                }
//                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
//                if (lastVisibleItemPosition + 1 == goodSortAdapter.getItemCount()) {
//
//                    boolean isRefreshing =  refresh_header_addsort.isRefreshing();
//                    if (isRefreshing) {
//                        goodSortAdapter.notifyItemRemoved(goodSortAdapter.getItemCount());
//                        return;
//                    }
//                    if (loadingMore) {
//                        getSortlist();
//                    }else {
//                        goodSortAdapter.notifyItemRemoved(goodSortAdapter.getItemCount());
//                        if(goodsortList.size()>20){
//                            SysUtils.showError("没有更多数据了！");
//                        }
//                    }
//                }
//            }
//        });
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
                if(goodsortList!=null){
                    goodsortList.clear();
                }
                getSortlist();
            }
        });
        iv_back= (ImageView) findViewById(R.id.iv_back);
        tv_edit= (TextView) findViewById(R.id.tv_edit);
        btn_addsort= (Button) findViewById(R.id.btn_addsort);
        list_addsort= (RecyclerView) findViewById(R.id.list_addsort);
       layoutManager = new LinearLayoutManager(this);

        iv_back.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        btn_addsort.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                sendBroadcast(new Intent(Global.BROADCAST_GoodsManagementactivity_ACTION).putExtra("type",2));
                finish();
                break;
            case R.id.tv_edit:
                Intent edititent=new Intent(SortManagementActivity.this,SortEditActivity.class);
                startActivity(edititent);
                break;
            case R.id.btn_addsort:
                addsortDialog();
                    break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if (goodSortAdapter!=null&&goodSortAdapter.menuIsOpen()) {
                goodSortAdapter.closeMenu();//关闭菜单
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
            sendBroadcast(new Intent(Global.BROADCAST_GoodsManagementactivity_ACTION).putExtra("type",2));
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 新增分类弹窗
     */
    private void addsortDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(SortManagementActivity.this);
        View view =View.inflate(SortManagementActivity.this, R.layout.editaccountinfo_dialog,null);
        TextView tv_accountinfo_name=(TextView)view.findViewById(R.id.tv_accountinfo_name);
        et_accountinfo_input=(EditText)view.findViewById(R.id.et_accountinfo_input);
        et_accountinfo_input.requestFocus();
        et_accountinfo_input.setFocusable(true);
        //软键盘显示
        imm = (InputMethodManager) SortManagementActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        TextView tv_editaccount_cancel=(TextView)view.findViewById(R.id.tv_editaccount_cancel);
        TextView tv_editaccount_save=(TextView)view.findViewById(R.id.tv_editaccount_save);
        tv_accountinfo_name.setText(getString(R.string.btn_addsort));//新增分类
        tv_editaccount_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                //再次调用软键盘消失
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        tv_editaccount_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(et_accountinfo_input.getText().toString().trim())){
                    mAlertDialog.dismiss();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    addSort();
                }
            }
        });
        mAlertDialog= dialog.setView(view).show();
        mAlertDialog.show();
    }

    /**
     * 新增分类
     */
    int add_type;
    private void addSort(){
      add_type=1;
        String nav=et_accountinfo_input.getText().toString().trim();
        Map<String,String> map=new HashMap<>();
        map.put("nav",nav);
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("cat_add"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("添加分类ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        add_type=2;
                        Toast.makeText(SortManagementActivity.this,getString(R.string.str354),Toast.LENGTH_SHORT).show();//添加成功
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    getSortlist();
                    updateAdapter();
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
        showLoading(SortManagementActivity.this, getString(R.string.str92));

    }
    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {


        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
            manager.setStackFromEnd(true);
        }

    }
    /**
     * 获取分类信息
     */
    private  void getSortlist(){
    Map<String,String> map=new HashMap<String, String>();
    map.put("page",page+"");
    CustomRequest customRequest=new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("cat_getlist"), map, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                JSONObject ret = SysUtils.didResponse(jsonObject);
                System.out.println("分类ret="+ret);
                String status = ret.getString("status");
                String message = ret.getString("message");
                JSONObject  data=null;
                if(!status.equals("200")){
                    SysUtils.showError(message);
                }else {
////                    if(page<=1){
                        goodsortList.clear();
////                    }
                    data=ret.getJSONObject("data");
                    JSONArray arry=data.getJSONArray("nav_info");
                    if(arry!=null&&arry.length()>0){
                        for(int i=0;i<arry.length();i++){
                            JSONObject jsonObject1=arry.getJSONObject(i);
                            GoodSort goodSort=new GoodSort(jsonObject1.getString("tag_name"),jsonObject1.getString("tag_id"),0);
                            goodsortList.add(goodSort);
                        }
                    }
                    total=data.getInt("total");
                    int totalpage= (int) Math.ceil((float)total/NUM_PER_PAGE);
                    loadingMore=totalpage>page;
                    if(loadingMore){
                        page++;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                setView();
                updateAdapter();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            SysUtils.showNetworkError();
            setNoNetwork();

        }
    });
    executeRequest(customRequest);
}

    @Override
    protected void onResume() {
        super.onResume();
        loadFirst();
    }

    //第一次加载
    private void loadFirst() {
        page = 1;
        if(goodsortList!=null){
            goodsortList.clear();
        }
        getSortlist();
    }
//    @Override
//    public void onRefresh() {
//        loadFirst();
//        if(goodSortAdapter!=null){
//            if (goodSortAdapter.menuIsOpen()) {
//                goodSortAdapter.closeMenu();//关闭菜单
//            }
//        }
//    }
    private void updateAdapter(){
        if(goodSortAdapter==null){
            goodSortAdapter=new GoodSortAdapter(SortManagementActivity.this,goodsortList);
            list_addsort.setLayoutManager(layoutManager);
            list_addsort.setAdapter(goodSortAdapter);
            list_addsort.addItemDecoration(new DividerDecoration(SortManagementActivity.this));
            if(add_type==2){
                add_type=1;
                MoveToPosition(layoutManager,list_addsort,goodsortList.size());
            }else {
                MoveToPosition(layoutManager,list_addsort,0);
            }
//            goodSortAdapter.
        }else {
            if(add_type==2){
                MoveToPosition(layoutManager,list_addsort,goodsortList.size());
            }else {
                MoveToPosition(layoutManager,list_addsort,0);
            }
            goodSortAdapter.notifyDataSetChanged();
        }
        if(total<=NUM_PER_PAGE){
            goodSortAdapter.notifyItemRemoved(goodSortAdapter.getItemCount()+1);
        }
    }
    @Override
    public void onItemClick(View view, int position) {
        if(type==2){
            Intent intent=new Intent();
            intent.putExtra("goodtype",goodsortList.get(position).getName());
            intent.putExtra("goodtype_id",goodsortList.get(position).getId());
            SortManagementActivity.this.setResult(204,intent);
            finish();
        }

    }
    @Override
    public void onDeleteBtnCilck(View view, int position) {
        removeSortlist(position);
        goodSortAdapter.removeData(position);
        goodSortAdapter.notifyDataSetChanged();
    }
    /**
     * 删除分类信息
     */
    private  void removeSortlist(int position){
        Map<String,String> map=new HashMap<String, String>();
        map.put("tag_id",goodsortList.get(position).getId());
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("cat_remove"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("删除分类ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject  data=null;
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    getSortlist();
                    updateAdapter();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SysUtils.showNetworkError();

            }
        });
        executeRequest(customRequest);
    }
    private void setView() {
        if(goodsortList.size() < 1) {
            //没有结果
            list_addsort.setVisibility(View.GONE);
            include_nowifi.setVisibility(View.GONE);
            include_noresult.setVisibility(View.VISIBLE);
            layout_err.setVisibility(View.VISIBLE);
        } else {
            //有结果
            include_noresult.setVisibility(View.GONE);
            include_nowifi.setVisibility(View.GONE);
            layout_err.setVisibility(View.GONE);
            list_addsort.setVisibility(View.VISIBLE);
        }
    }

    private void setNoNetwork() {
        //网络不通
        if (goodsortList.size() < 1) {
            if (!include_nowifi.isShown()) {
                list_addsort.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            }
        } else {
            SysUtils.showNetworkError();
        }
    }
}
