package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.ui.adapter.ChooseGoodsSortListAdapter;
import com.ui.adapter.NearlyShopperGoodsInfoAdapter;
import com.ui.adapter.SupplierShoppingCarAdapter;
import com.ui.entity.GoodSort;
import com.ui.entity.Goods_info;
import com.ui.entity.Goods_info_type;
import com.ui.global.Global;
import com.ui.listview.PagingListView;
import com.ui.listview.PinnedHeaderListView;
import com.ui.util.CustomRequest;
import com.ui.util.DialogUtils;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;
import com.ui.view.MyListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NearlyShopperGoodsActivity extends BaseActivity implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {

    private TextView tv_shoppername,tv_rule,tv_totalprice,tv_close;
    private PagingListView list_sort;
    private EditText et_search;
    private PinnedHeaderListView list_goods;
    private RelativeLayout layout_settleaccounts;
    private ImageView iv_shoppingcar,iv_back,iv_shopperlogo,iv_enshrine,iv_close,iv_shopperbackground;
    private Button btn_shopping_num;
    private ArrayList<GoodSort> goodsortList;//商品类
    private ArrayList<Goods_info> goodsinfoList;//商品信息
    private ArrayList<Goods_info> shoppingcar_goodsinfolist=new ArrayList<>();//购物车商品信息
    private ArrayList<Integer> goodsinfo_lenth;//分类总数
    private ArrayList<Goods_info_type> pruduct_info;
    private ChooseGoodsSortListAdapter chooseGoodsSortListAdapter;
    private NearlyShopperGoodsInfoAdapter nearlyShopperGoodsInfoAdapter;
    private SupplierShoppingCarAdapter supplierShoppingCarAdapter;
    private View view;//购物车的view
    private ViewGroup anim_mask_layout;//动画层
    //底部数据
    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet;
    private int select_num;//选择商品的数量
    private int select_sortgoods_num;//所选分类商品数量
    private double select_shoppingprice;//选择商品的数量
    private MyListView lv_shoppingcar;//购物车listview
    private boolean isScroll = true;
    private String shopperid;
    private String good_name;
    private String good_id;
    private String seller_id;
    private String seller_name;
    private String logo;
    private String homepage;
    private String freight;
    private String collection_seller;
    private DisplayImageOptions options,options1;
    private InputMethodManager imm ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearly_shopper_goods);
        SysUtils.setupUI(NearlyShopperGoodsActivity.this,findViewById(R.id.activity_nearly_shopper_goods));
        initToolbar(this);

        initView();
//        jsonData();
        getShopper_goods();
    }

    private void initView() {
        tv_shoppername= (TextView) findViewById(R.id.tv_shoppername);
        tv_rule= (TextView) findViewById(R.id.tv_rule);
        tv_totalprice= (TextView) findViewById(R.id.tv_totalprice);
        tv_close= (TextView) findViewById(R.id.tv_close);
        list_sort= (PagingListView) findViewById(R.id.list_sort);
        list_goods= (PinnedHeaderListView) findViewById(R.id.list_goods);
        layout_settleaccounts= (RelativeLayout) findViewById(R.id.layout_settleaccounts);
        iv_shoppingcar= (ImageView) findViewById(R.id.iv_shoppingcar);
        iv_shopperbackground= (ImageView) findViewById(R.id.iv_shopperbackground);
        iv_back= (ImageView) findViewById(R.id.iv_back);
        iv_enshrine= (ImageView) findViewById(R.id.iv_enshrine);
        iv_close= (ImageView) findViewById(R.id.iv_close);
        iv_shopperlogo= (ImageView) findViewById(R.id.iv_shopperlogo);
        btn_shopping_num= (Button) findViewById(R.id.btn_shopping_num);
        bottomSheetLayout= (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);
        et_search= (EditText) findViewById(R.id.et_search);


        iv_shoppingcar.setOnClickListener(null);
        layout_settleaccounts.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        tv_close.setOnClickListener(this);
        et_search.addTextChangedListener(this);
        et_search.setOnEditorActionListener(this);

        imm = (InputMethodManager) NearlyShopperGoodsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        if(DialogUtils.isSoftShowing(NearlyShopperGoodsActivity.this)){
            //再次调用软键盘消失
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }



        view = LayoutInflater.from(NearlyShopperGoodsActivity.this).inflate(R.layout.layout_bottom_sheet,(ViewGroup) getWindow().getDecorView(),false);
        lv_shoppingcar=(MyListView) view.findViewById(R.id.lv_product);
        initListener();

        Intent intent=getIntent();
        if(intent!=null){
            shopperid=intent.getStringExtra("shopperid");
            good_name=intent.getStringExtra("good_name");
            good_id=intent.getStringExtra("good_id");
        }

        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo_default)
                .showImageForEmptyUri(R.drawable.logo_default)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(300))//设置图片圆角
                .build();
        options1=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.shopper_background)
                .showImageForEmptyUri(R.drawable.shopper_background)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(0))//设置图片圆角
                .build();
    }
    //监听
private void initListener(){
    list_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            isScroll = false;
            chooseGoodsSortListAdapter.setDefSelect(position);
            int rightSection = 0;
            for (int i = 0; i < position; i++) {
                rightSection += pruduct_info.get(i).getProduct_info().size() + 1;
            }
            list_goods.setSelection(rightSection);
        }
    });
    //商品类别滑动
    list_goods.setOnScrollListener(new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            isScroll = true;
            switch (scrollState) {
                case SCROLL_STATE_TOUCH_SCROLL:
                    new PauseOnScrollListener(ImageLoader.getInstance(), true, true);//滑动时，不加载图片
//                        isScroll = true;
                    break;
                case SCROLL_STATE_FLING:
//                        isScroll= false;
                    break;
                case SCROLL_STATE_IDLE:
//                        isScroll = false;
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (isScroll) {
                if (pruduct_info != null) {
                    if(nearlyShopperGoodsInfoAdapter!=null) {

                        for (int i = 0; i < pruduct_info.size(); i++) {
                            if (i == nearlyShopperGoodsInfoAdapter.getSectionForPosition(firstVisibleItem)) {
                                if (i > visibleItemCount) {
                                    list_sort.smoothScrollToPosition(i);
                                } else {
                                    list_sort.smoothScrollToPosition(0);
                                }
                                chooseGoodsSortListAdapter.setDefSelect(i);
                            } else {

                            }
                        }
                    }
                } else {
                    isScroll = true;
                }
            }
        }
    });
}

    /**
     * 获取商品数据
     */
    private void  getShopper_goods(){
        Map<String,String> map=new HashMap<>();
        map.put("supply_id",shopperid);
        System.out.println("map="+map);
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("supply_goods"),map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                    hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("ret："+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data = ret.getJSONObject("data");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        JSONObject supply_info= data.getJSONObject("supply_info");
                        if(supply_info!=null){
                            pruduct_info=new ArrayList<>();
                            seller_id=supply_info.getString("seller_id");
                            seller_name=supply_info.getString("seller_name");
                            logo=supply_info.getString("logo");
                            homepage=supply_info.getString("homepage");
                            freight=supply_info.getString("freight");
                            collection_seller=supply_info.getString("collection_seller");
                            tv_shoppername.setText(seller_name);
                            ImageLoader.getInstance().displayImage(logo,iv_shopperlogo, options);
                            ImageLoader.getInstance().displayImage(homepage,iv_shopperbackground, options1);
                            if("true".equals(collection_seller)){
                                iv_enshrine.setBackgroundResource(R.drawable.collect);
                            }else {
                                iv_enshrine.setBackgroundResource(R.drawable.share_wx_collect);
                            }
                            JSONArray catagory_infos=data.getJSONArray("catagory_infos");
                            if(catagory_infos!=null){
                                goodsortList=new ArrayList<>();
                                for(int i=0;i<catagory_infos.length();i++){
                                    try {
                                    JSONObject jsonObject1=catagory_infos.getJSONObject(i);
                                    String tag_id=jsonObject1.getString("tag_id");
                                    String tag_name=jsonObject1.getString("tag_name");
                                    GoodSort goodSort=new GoodSort(tag_name,tag_id,0);
                                    JSONArray pro_info=jsonObject1.getJSONArray("pro_info");
                                        goodsortList.add(goodSort);
                                    if(pro_info!=null){
                                        goodsinfoList=new ArrayList<>();
                                        for(int j=0;j<pro_info.length();j++){
                                            JSONObject jsonObject2=pro_info.getJSONObject(j);
                                            String name=jsonObject2.getString("name");
                                            String goods_id=jsonObject2.getString("goods_id");
                                            String price=jsonObject2.getString("price");
                                            String store=jsonObject2.getString("store");
                                            String img_src=jsonObject2.getString("img_src");
                                            String buy_count=jsonObject2.getString("buy_count");
                                            String brief=jsonObject2.getString("brief");
//                                            String collection_goods=jsonObject2.getString("collection_goods");
                                            double min_price=0.00;
                                            int store_int=0;
                                            int buy_count_int=0;
                                            if(!TextUtils.isEmpty(price)){
                                                min_price=Double.parseDouble(SetEditTextInput.stringpointtwo(price));
                                            }else {
                                                min_price=0.00;
                                            }
                                            if(!TextUtils.isEmpty(store)){
                                                store_int=Integer.parseInt(store);
                                            }else {
                                                store_int=0;
                                            }
                                            if(!TextUtils.isEmpty(buy_count)){
                                                buy_count_int=Integer.parseInt(buy_count);
                                            }else {
                                                buy_count_int=0;
                                            }
                                            Goods_info goodsInfo=new Goods_info(name,"",goods_id,buy_count_int,store_int,min_price,img_src,0,0,tag_id,false,0,brief,"");
                                            goodsinfoList.add(goodsInfo);
                                        }
                                        Goods_info_type goods_info_type=new Goods_info_type(tag_id,tag_name,goodsinfoList);
                                        pruduct_info.add(goods_info_type);
                                    }
                                    chooseGoodsSortListAdapter=new ChooseGoodsSortListAdapter(NearlyShopperGoodsActivity.this,goodsortList);
                                    list_sort.setAdapter(chooseGoodsSortListAdapter);
                                    chooseGoodsSortListAdapter.setDefSelect(0);

                                    nearlyShopperGoodsInfoAdapter=new NearlyShopperGoodsInfoAdapter(NearlyShopperGoodsActivity.this,pruduct_info);
                                    list_goods.setAdapter(nearlyShopperGoodsInfoAdapter);
                                    }catch (Exception e){
                                        System.out.println("e="+e.toString());
                                    }
                                }
                            }
                        }
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                    System.out.println("e=="+e.toString());
                } finally {
                    //商品搜索页进入店铺，商品定位
                    if(!TextUtils.isEmpty(good_id)) {
                        for (int x = 0; x < pruduct_info.size(); x++) {
                            for (int j = 0; j < pruduct_info.get(x).getProduct_info().size(); j++) {
                                if (pruduct_info.get(x).getProduct_info().get(j).getGoods_id().equals(good_id)) {
                                    if(x>0){
                                        int p=0;
                                        for(int i=0;i<x;i++){
                                            p+=pruduct_info.get(i).getProduct_info().size();
                                        }
                                        list_goods.setSelection(p+j+x);
                                    }else {
                                        list_sort.setSelection(x);
                                    }
                                }
                            }
                        }
                        }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                SysUtils.showError("网络不给力");
            }
        });
        executeRequest(r);
        showLoading(NearlyShopperGoodsActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_shoppingcar:
                showBottomSheet();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_close:
                et_search.setText("");
                break;
            case R.id.tv_close:
                iv_close.setVisibility(View.GONE);
                tv_close.setVisibility(View.GONE);
                iv_enshrine.setVisibility(View.VISIBLE);
                et_search.setText("");
                if(DialogUtils.isSoftShowing(NearlyShopperGoodsActivity.this)){
                    //再次调用软键盘消失
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                nearlyShopperGoodsInfoAdapter=new NearlyShopperGoodsInfoAdapter(NearlyShopperGoodsActivity.this,pruduct_info);
                list_goods.setAdapter(nearlyShopperGoodsInfoAdapter);
                break;
            case R.id.layout_settleaccounts:
                setShoppingCart();
                break;
        }
    }
private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * type;1增加商品的飞入动画,及购物车数量增加
         * 2订单列表减号
         * 3购物车加减
         */
        int type=intent.getIntExtra("type",0);
        if(type==1) {
            View view = View.inflate(NearlyShopperGoodsActivity.this, R.layout.imageview_point, null);
            int[] startLocation = intent.getIntArrayExtra("startLocation");
            if (startLocation != null) {
                setAnim(view, startLocation);
            }

            setShoppingCar();
        }
        if(type==2){
            setShoppingCar();
        }
        if(type==3){
            nearlyShopperGoodsInfoAdapter.notifyDataSetChanged();
            setShoppingCar();
        }
    }
};
    /**
     * 购物车数据
     */
    private void setShoppingCar(){
        shoppingcar_goodsinfolist.clear();
        for (int i = 0; i < pruduct_info.size(); i++) {
            select_sortgoods_num=0;
            for (int x = 0; x < pruduct_info.get(i).getProduct_info().size(); x++) {
                if (pruduct_info.get(i).getProduct_info().get(x).ischoose()) {
                    shoppingcar_goodsinfolist.add(pruduct_info.get(i).getProduct_info().get(x));
                }
                for(int j=0;j<goodsortList.size();j++){
                    if( pruduct_info.get(i).getProduct_info().get(x).getTag_id()==goodsortList.get(j).getId()){
                        select_sortgoods_num+=pruduct_info.get(i).getProduct_info().get(x).getSelect_num();
                        goodsortList.get(j).setChoose_num(select_sortgoods_num);
                        chooseGoodsSortListAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
        select_num=0;
        select_shoppingprice=0.00;
        if(shoppingcar_goodsinfolist.size()>0){
            for(int i=0;i< shoppingcar_goodsinfolist.size();i++){
                select_num+=shoppingcar_goodsinfolist.get(i).getSelect_num();
                select_shoppingprice+=(shoppingcar_goodsinfolist.get(i).getPrice()*shoppingcar_goodsinfolist.get(i).getSelect_num());
            }
            btn_shopping_num.setVisibility(View.VISIBLE);
            tv_totalprice.setVisibility(View.VISIBLE);
            btn_shopping_num.setText(select_num+"");
            tv_totalprice.setText("￥"+SetEditTextInput.stringpointtwo(select_shoppingprice+""));
            iv_shoppingcar.setBackgroundResource(R.drawable.shopingcar);
            iv_shoppingcar.setOnClickListener(NearlyShopperGoodsActivity.this);
        }else {
            btn_shopping_num.setVisibility(View.GONE);
            tv_totalprice.setVisibility(View.GONE);
            iv_shoppingcar.setBackgroundResource(R.drawable.shoppingcar_gray);
            iv_shoppingcar.setOnClickListener(null);

            if(bottomSheetLayout.isSheetShowing()){
                bottomSheetLayout.dismissSheet();
            }
        }
        chooseGoodsSortListAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onResume() {
        super.onResume();
        NearlyShopperGoodsActivity.this.registerReceiver(broadcastReceiver,new IntentFilter(Global.BROADCAST_NearlyShopperGoodsActivity_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NearlyShopperGoodsActivity.this.unregisterReceiver(broadcastReceiver);
    }
    //创建购物车view
    private void showBottomSheet(){
        bottomSheet = createBottomSheetView();
        if(bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }else {
            bottomSheetLayout.showWithSheetView(bottomSheet);
        }
    }
    //查看购物车布局
    AlertDialog malertDialog_shoppingCar = null;
    private View createBottomSheetView(){
        LinearLayout layout_openorder= (LinearLayout) view.findViewById(R.id.layout_openorder);
         LinearLayout layout_clear= (LinearLayout) view.findViewById(R.id.layout_clear);
        final LinearLayout layout_clear1= (LinearLayout) view.findViewById(R.id.layout_clear1);
        ImageView iv_clear= (ImageView) view.findViewById(R.id.iv_clear);
        TextView tv_clear= (TextView) view.findViewById(R.id.tv_clear);
        layout_clear.setVisibility(View.GONE);
        layout_openorder.setVisibility(View.GONE);
        //清空
        layout_clear1.setVisibility(View.VISIBLE);
        layout_clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(NearlyShopperGoodsActivity.this,R.style.AlertDialog)
                        .setMessage(getString(R.string.str35))
                        .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(shoppingcar_goodsinfolist!=null){
                                    for(int i=0;i<shoppingcar_goodsinfolist.size();i++){
                                        shoppingcar_goodsinfolist.get(i).setSelect_num(0);
                                        shoppingcar_goodsinfolist.get(i).setIschoose(false);
                                    }
                                    for(int j=0;j<goodsortList.size();j++){
                                        goodsortList.get(j).setChoose_num(0);
                                    }
                                    shoppingcar_goodsinfolist.clear();
                                    if(bottomSheetLayout.isSheetShowing()){
                                        bottomSheetLayout.dismissSheet();
                                    }
                                    btn_shopping_num.setVisibility(View.GONE);
                                    tv_totalprice.setVisibility(View.GONE);
                                    iv_shoppingcar.setBackgroundResource(R.drawable.shoppingcar_gray);
                                    iv_shoppingcar.setOnClickListener(null);
                                    chooseGoodsSortListAdapter.notifyDataSetChanged();
                                    nearlyShopperGoodsInfoAdapter.notifyDataSetChanged();
                                }
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
        });
        supplierShoppingCarAdapter = new SupplierShoppingCarAdapter(NearlyShopperGoodsActivity.this,shoppingcar_goodsinfolist);
        lv_shoppingcar.setAdapter(supplierShoppingCarAdapter);
        return view;
    }
    /**
     * @Description: 创建动画层
     * @param
     * @return void
     * @throws
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) NearlyShopperGoodsActivity.this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(NearlyShopperGoodsActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE-1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final ViewGroup parent, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    public void setAnim(final View v, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v, startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
        iv_shoppingcar.getLocationInWindow(endLocation);
        // 计算位移
        int endX = 0 - startLocation[0] + 85;// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标

        TranslateAnimation translateAnimationX = new TranslateAnimation(0,endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationY.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(500);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }
        });

    }
//搜索输入框的输入监听
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
            tv_close.setVisibility(View.VISIBLE);
            iv_enshrine.setVisibility(View.GONE);
        }else {
            iv_close.setVisibility(View.GONE);
            tv_close.setVisibility(View.GONE);
            iv_enshrine.setVisibility(View.VISIBLE);
        }
    }
//输入框键盘的搜索按钮监听
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId== EditorInfo.IME_ACTION_SEARCH){
            String goodsname=et_search.getText().toString().trim();

            ArrayList<Goods_info_type> pruduct_info1 = new ArrayList<>();
            for (int i = 0; i < pruduct_info.size(); i++) {
                ArrayList<Goods_info> goodsinfoList1 = new ArrayList<>();
                boolean ishas=false;
                for (int j = 0; j < pruduct_info.get(i).getProduct_info().size(); j++) {
                    if (pruduct_info.get(i).getProduct_info().get(j).getName().indexOf(goodsname) != -1) {
                        goodsinfoList1.add(pruduct_info.get(i).getProduct_info().get(j));
                        ishas=true;
                    }
                }
                if(ishas){
                    pruduct_info1.add(new Goods_info_type(pruduct_info.get(i).getTag_id(), pruduct_info.get(i).getTag_name(), goodsinfoList1));
                }
            }
            nearlyShopperGoodsInfoAdapter=new NearlyShopperGoodsInfoAdapter(NearlyShopperGoodsActivity.this,pruduct_info1);
            list_goods.setAdapter(nearlyShopperGoodsInfoAdapter);
            if(DialogUtils.isSoftShowing(NearlyShopperGoodsActivity.this)){
                //再次调用软键盘消失
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
        return false;
    }

    /**
     * 加入商家总购物车
     */
    JSONArray jsonArray;
    private void setShoppingCart(){
        Map<String,String> stringStringMap=new HashMap<>();
        ArrayList<Map<String,String>> mapArrayList=new ArrayList<>();
        for(int i=0;i<shoppingcar_goodsinfolist.size();i++){
            Map<String,String> stringMap=new HashMap<>();
            stringMap.put("goods_id",shoppingcar_goodsinfolist.get(i).getGoods_id());
            stringMap.put("goods_name",shoppingcar_goodsinfolist.get(i).getName());
            stringMap.put("goods_price",shoppingcar_goodsinfolist.get(i).getPrice()+"");
            stringMap.put("goods_nums",shoppingcar_goodsinfolist.get(i).getSelect_num()+"");
//            stringMap.put("img_src",shoppingcar_goodsinfolist.get(i).getImageurl());
            mapArrayList.add(stringMap);
        }
        stringStringMap.put("stringStringMap",mapArrayList.toString());
        JSONObject shoppingcar_object=new JSONObject(stringStringMap);
        try {
            String map_str=shoppingcar_object.getString("stringStringMap");
            jsonArray=new JSONArray(map_str.replace("/","|"));
        }catch (Exception e){
            System.out.println("e="+e.toString());
        }
        Map<String,String> map=new HashMap<>();
        map.put("supply_id",shopperid);
        map.put("nums",btn_shopping_num.getText().toString().trim());
        map.put("amount",SetEditTextInput.stringpointtwo(select_shoppingprice+""));
        map.put("commodity",jsonArray+"");
        System.out.println("map="+map);
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("cart_add"),map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("ret："+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
//                    JSONObject data = ret.getJSONObject("data");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        Intent intent=new Intent(NearlyShopperGoodsActivity.this,WholeSaleOrdersActivity.class);
                        intent.putExtra("type",3);
                        startActivity(intent);
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                SysUtils.showError("网络不给力");
            }
        });
        executeRequest(r);
        showLoading(NearlyShopperGoodsActivity.this);
    }
}
