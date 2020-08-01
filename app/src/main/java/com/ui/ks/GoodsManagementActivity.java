package com.ui.ks;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.adapter.GoodSortListAdapter;
import com.ui.adapter.GoodsListAdapter;
import com.ui.entity.GoodList_Item_check;
import com.ui.entity.GoodSort;
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

/**
 * 商品管理页面
 */

public class GoodsManagementActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView iv_back,iv_search,iv_popwindow;
    private Button btn_sort,btn_alleditor,btn_addgood,btn_cell,btn_down,btn_dialog_cell,btn_sure;
    private Button btn_stock_warning_num,btn_less_goods_num,btn_drop_goods_num;
    private PopupWindow popupWindow;
    private TextView tv_message;
    private View view;
    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;
    private RelativeLayout stock_warning_layout,less_goods_layout,drop_goods_layout;
    private PagingListView list_sort,list_good;
    private int total=0;
    private int page=1;
    private int NUM_PER_PAGE = 20;//加载的条数
    private boolean loadingMore = false;//是否加载更多
    private ArrayList<GoodSort> goodsortList;
    private ArrayList<Goods_info> goodsinfoList;
    private ArrayList<GoodList_Item_check> goodList_Item_check;
    private GoodSortListAdapter mgoodSortListAdapter;
    private GoodsListAdapter goodsListAdapter;
    public int  cur_pos;// 当前显示的一行
    private boolean isup;//商品是否下架
    private String checkid_str="";
    private String pop_good_stock;
    private String  pop_good_warm;
   private String  pop_marktable;
    private int type=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_management);
        SysUtils.setupUI(this,findViewById(R.id.activity_goods_management));
        initToolbar(this);

        initView();
        initData();
    }

    private void initData() {
        goodsortList=new ArrayList<>();
        goodsinfoList=new ArrayList<>();
        goodList_Item_check=new ArrayList<>();
        getSortlist();
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
                getGoodlist(cur_pos);
            }
        });
        iv_back= (ImageView) findViewById(R.id.iv_back);
        iv_search= (ImageView) findViewById(R.id.iv_search);
        iv_popwindow= (ImageView) findViewById(R.id.iv_popwindow);
        btn_sort= (Button) findViewById(R.id.btn_sort);
        btn_alleditor= (Button) findViewById(R.id.btn_alleditor);
        btn_addgood= (Button) findViewById(R.id.btn_addgood);
        btn_down= (Button) findViewById(R.id.btn_down);
        btn_cell= (Button) findViewById(R.id.btn_cell);
        list_sort= (PagingListView) findViewById(R.id.list_sort);
        list_good= (PagingListView) findViewById(R.id.list_goods);


        iv_back.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        iv_popwindow.setOnClickListener(this);
        btn_sort.setOnClickListener(this);
        btn_alleditor.setOnClickListener(this);
        btn_addgood.setOnClickListener(this);
        btn_down.setOnClickListener(this);
        btn_cell.setOnClickListener(this);
        list_sort.setOnItemClickListener(this);
        list_good.setOnItemClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                Intent search_intent=new Intent(GoodsManagementActivity.this,GoodsInfoSearchActivity.class);
                startActivity(search_intent);
                break;
            case R.id.iv_popwindow:
                select_popwindow();
                break;
            case R.id.btn_sort:
                Intent intentsort=new Intent(GoodsManagementActivity.this,SortManagementActivity.class);
                startActivity(intentsort);
                break;
            case R.id.btn_alleditor:
                if(goodsinfoList!=null&&goodsinfoList.size()>0){
                    goodsListAdapter.isisup(true);
                    btn_sort.setVisibility(View.GONE);
                    btn_alleditor.setVisibility(View.GONE);
                    btn_addgood.setVisibility(View.GONE);
                    btn_down.setVisibility(View.VISIBLE);
                    btn_cell.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_cell:
                bottomchange();
                break;
            case R.id.btn_down:
                downGoods();
                break;
            case R.id.btn_addgood:
                type=2;
                Intent intentaddgood=new Intent(GoodsManagementActivity.this,AddGoodsActivity.class);
                intentaddgood.putExtra("type",1);
                startActivity(intentaddgood);
                break;
        }

    }

    //第一次加载
    private void loadFirst() {
       page = 1;
        getSortlist();
    }

    /**
     * 库存类型弹出
     */
    private void select_popwindow(){
    if(popupWindow==null) {
        view = View.inflate(GoodsManagementActivity.this, R.layout.sort_popwindow_view, null);
        stock_warning_layout = (RelativeLayout) view.findViewById(R.id.stock_warning_layout);
        less_goods_layout = (RelativeLayout) view.findViewById(R.id.less_goods_layout);
        drop_goods_layout = (RelativeLayout) view.findViewById(R.id.drop_goods_layout);
        btn_stock_warning_num= (Button) view.findViewById(R.id.btn_stock_warning_num);//库存预警
        btn_less_goods_num= (Button) view.findViewById(R.id.btn_less_goods_num);//缺货产品
        btn_drop_goods_num= (Button) view.findViewById(R.id.btn_drop_goods_num);//已下架的商品
        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable mColorDrawable = new ColorDrawable(0x20000000);
        //设置弹框的背景
        popupWindow.setBackgroundDrawable(mColorDrawable);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.75f, Animation.RELATIVE_TO_SELF, 0.1f);
        scaleAnimation.setDuration(200);
        view.startAnimation(scaleAnimation);
        //设置弹框添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.drop_goods_layout).getBottom();
                int height_top = view.findViewById(R.id.stock_warning_layout).getTop();
                int weight = view.findViewById(R.id.drop_goods_layout).getLeft();
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height || y < height_top) {
                        if(popupWindow!=null){
                            popupWindow.dismiss();
                            popupWindow = null;
                        }
                    }
                    if (x < weight) {
                        if(popupWindow!=null){
                            popupWindow.dismiss();
                            popupWindow = null;
                        }
                    }
                }
                return true;
            }
        });
        stock_warning_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow!=null){
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                popGoIntent(1);
            }
        });
        less_goods_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow!=null){
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                popGoIntent(2);

            }
        });
        drop_goods_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow!=null){
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                popGoIntent(3);

            }
        });
        popupWindow.showAtLocation(iv_popwindow, Gravity.TOP, 0, 0);
    }
        getselect_popwindowinfo();

}

    /**
     * 点击弹窗，进入不同页面
     * type   1：库存预警商品  2： 缺货商品 3：已下架商品
     * @param type
     */
    private void popGoIntent(int type){
        Intent intent=new Intent(GoodsManagementActivity.this,StockWarningActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }
    /**
     * 获取选择弹窗信息
     */
    private  void getselect_popwindowinfo(){
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("inventory_warn"), null, new Response.Listener<JSONObject>() {
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
                        data=ret.getJSONObject("data");
                         pop_good_stock=data.getString("good_stock");
                         pop_good_warm=data.getString("good_warm");
                         pop_marktable=data.getString("marktable");
                        btn_stock_warning_num.setText(pop_good_stock);
                        btn_less_goods_num.setText(pop_good_warm);
                        btn_drop_goods_num.setText(pop_marktable);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
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
                        goodsortList.clear();
                        goodList_Item_check.clear();
                        data=ret.getJSONObject("data");
                        JSONArray arry=data.getJSONArray("nav_info");
                        if(arry!=null&&arry.length()>0){
                            for(int i=0;i<arry.length();i++){
                                JSONObject jsonObject1=arry.getJSONObject(i);
                                GoodSort goodSort=new GoodSort(jsonObject1.getString("tag_name"),jsonObject1.getString("tag_id"),0);
                                goodsortList.add(goodSort);
                            }
                        }
                        JSONArray goods_info=data.getJSONArray("goods_info");
                        if(goodsinfoList!=null&&goodsinfoList.size()>0){
                            goodsinfoList.clear();
                        }
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
    /**
     * 获取商品列表信息
     */
    private  void getGoodlist(int position){
        if(goodsortList.size()>0) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("tag_id", goodsortList.get(position).getId());
            System.out.println("商品map=" + map);
            CustomRequest customRequest = new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("cat_goods_getlist"), map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                   hideLoading();
                    try {
                        JSONObject ret = SysUtils.didResponse(jsonObject);
                        System.out.println("商品ret=" + ret);
                        String status = ret.getString("status");
                        String message = ret.getString("message");
                        JSONObject data = null;
                        if (!status.equals("200")) {
                            SysUtils.showError(message);
                        } else {
                            goodsinfoList.clear();
                            goodList_Item_check.clear();
                            data = ret.getJSONObject("data");
                            JSONArray goods_info = data.getJSONArray("goods_info");
                            if (goods_info != null && goods_info.length() > 0) {
                                for (int i = 0; i < goods_info.length(); i++) {
                                    JSONObject jsonObject_goods_info = goods_info.getJSONObject(i);
                                    Goods_info goodsInfo = new Goods_info(jsonObject_goods_info.getString("name"), jsonObject_goods_info.getString("marketable"),
                                            jsonObject_goods_info.getString("goods_id"), jsonObject_goods_info.getInt("buy_count"),
                                            Double.parseDouble(jsonObject_goods_info.getString("store")), jsonObject_goods_info.getDouble("price"),
                                            jsonObject_goods_info.getString("img_src"), jsonObject_goods_info.getInt("btn_switch_type"),0,"",false,0,"","");
                                    goodsinfoList.add(goodsInfo);
                                }
                                for (int i = 0; i < goodsinfoList.size(); i++) {
                                    GoodList_Item_check mgoodList_Item_check = new GoodList_Item_check();
                                    mgoodList_Item_check.setChecked(false);
                                    goodList_Item_check.add(mgoodList_Item_check);
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        setView();
                        update_goodlistAdapter();
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
            showLoading(GoodsManagementActivity.this);
        }
    }
    /**
     * 下架商品列表信息
     */
    private  void downGoods(){
        Map<String,String> map=new HashMap<String, String>();
        if(goodsListAdapter.getCheckid().length()<=0){
            SysUtils.showError("请选择需要下架的商品");
            return;
        }
        map.put("goods_id",goodsListAdapter.getCheckid());
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
                        if(goodsListAdapter!=null) {
                            bottomchange();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    getGoodlist(cur_pos);
//                    getSortlist();
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
       showLoading(GoodsManagementActivity.this);
    }

    /**
     * 商品类别适配器
     */
    private void updateAdapter() {
//        list_sort.onFinishLoading(loadingMore, null);

        if(mgoodSortListAdapter == null) {
            mgoodSortListAdapter = new GoodSortListAdapter(GoodsManagementActivity.this,goodsortList);
            list_sort.setAdapter( mgoodSortListAdapter);
            list_sort.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 一定要设置这个属性，否则ListView不会刷新
            mgoodSortListAdapter.setDefSelect(0);


        } else {
            mgoodSortListAdapter.setDefSelect(0);
            mgoodSortListAdapter.notifyDataSetChanged();
        }
        update_goodlistAdapter();
    }

    /**
     * 商品信息适配器
     */
    private void update_goodlistAdapter() {
//        list_sort.onFinishLoading(loadingMore, null);

        if(goodsListAdapter == null) {
            goodsListAdapter = new GoodsListAdapter(GoodsManagementActivity.this,goodsinfoList);
            list_good.setAdapter(goodsListAdapter);
        }else {
            goodsListAdapter.notifyDataSetChanged();
        }
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String tag_id=intent.getStringExtra("tag_id");
            int type=intent.getIntExtra("type",0);
            if(type==2){
                getSortlist();
            }else {
                if (tag_id != null) {
                    for (int i = 0; i < goodsortList.size(); i++) {
                        String tag_id_str = goodsortList.get(i).getId();
                        if (tag_id.equals(tag_id_str)) {
                            cur_pos = i;
                            break;
                        }
                    }
                }
                getGoodlist(cur_pos);
                mgoodSortListAdapter.setDefSelect(cur_pos);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            GoodsManagementActivity.this.unregisterReceiver(broadcastReceiver);
        } catch(Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getSortlist();
        GoodsManagementActivity.this.registerReceiver(broadcastReceiver, new IntentFilter(Global.BROADCAST_GoodsManagementactivity_ACTION));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.list_sort:
                cur_pos = position;// 更新当前行
               if(goodsListAdapter.getCheckid().replace(",","").length()>0){
                   View view1=View.inflate(GoodsManagementActivity.this,R.layout.dialog_sure_delete_layout,null);
                   final Dialog dialog=new Dialog(GoodsManagementActivity.this);
                   dialog.setContentView(view1);
                   btn_sure= (Button) view1.findViewById(R.id.btn_sure);
                   btn_dialog_cell= (Button) view1.findViewById(R.id.btn_cell);
                   tv_message= (TextView) view1.findViewById(R.id.tv_message);
                   tv_message.setText(getString(R.string.str128));
                   btn_sure.setText(getString(R.string.sure));
                   btn_dialog_cell.setText(getString(R.string.cancel));
                   btn_sure.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                          bottomchange();
                           mgoodSortListAdapter.setDefSelect(cur_pos);
                           getGoodlist(cur_pos);
                           dialog.dismiss();
                       }
                   });
                   btn_dialog_cell.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           dialog.dismiss();
                       }
                   });
                   dialog.show();
               }else {
                   mgoodSortListAdapter.setDefSelect(cur_pos);
                   getGoodlist(cur_pos);
               }
                break;
            case R.id.list_goods:
              Intent intent=new Intent(GoodsManagementActivity.this,AddGoodsActivity.class);
                intent.putExtra("type",type);
                intent.putExtra("goods_id",goodsinfoList.get(position).getGoods_id());
                startActivity(intent);
                break;

        }

    }

    /**
     * 商品下架选择
     * @param checkid
     * @param ischeck
     * @return
     */
  public  String checkid(String checkid,boolean ischeck){
      if(ischeck){
         checkid_str+=checkid+",";
      }else {
          if(checkid_str.length()>0){
              if(checkid_str.contains(checkid)){
                  checkid_str=checkid_str.replace(checkid,"");
              }
//              checkid_str=checkid_str.subSequence(0,checkid_str.length()-checkid.length()-1).toString();
          }
      }
      return checkid_str;
  }

    /**
     * 底部按钮状态的变化
     */
    private void bottomchange(){
        goodsListAdapter.isisup(false);
        btn_sort.setVisibility(View.VISIBLE);
        btn_alleditor.setVisibility(View.VISIBLE);
        btn_addgood.setVisibility(View.VISIBLE);
        btn_down.setVisibility(View.GONE);
        btn_cell.setVisibility(View.GONE);
    }
    private void setView() {
        if(goodsinfoList.size() < 1) {
            //没有结果
            list_good.setVisibility(View.GONE);
            include_nowifi.setVisibility(View.GONE);
            include_noresult.setVisibility(View.VISIBLE);
            layout_err.setVisibility(View.VISIBLE);
        } else {
            //有结果
            include_noresult.setVisibility(View.GONE);
            include_nowifi.setVisibility(View.GONE);
            layout_err.setVisibility(View.GONE);
            list_good.setVisibility(View.VISIBLE);
        }
    }

    private void setNoNetwork() {
        //网络不通
        if(goodsinfoList.size() < 1) {
            if(!include_nowifi.isShown()) {
                list_good.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            }
        } else {
            SysUtils.showNetworkError();
        }
    }
}
