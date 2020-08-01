package com.ui.ks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.base.BaseActivity;
import com.ui.adapter.MainStoreViewPagerAdapter;
import com.ui.util.BitmapCache;
import com.ui.util.BitmapUtils;
import com.ui.util.CustomRequest;
import com.ui.util.LoginUtils;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *店铺管理页面----总店
 */
public class MainStoreActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager viewpager;
    private LinearLayout point_layout;
    private TextView iv_viewpage_text;
    private TextView btn_set,tv_horizontalscroll;
    private ArrayList<ImageView> imageView_list;
    private ArrayList<String>  imageView_url=new ArrayList<String>();;
    private ImageView[]  point;
    private RelativeLayout storemanagement_layout,getmoney_layout,storeinfo_layout,dataanalysis_layout;
    private MainStoreViewPagerAdapter mainStoreViewPagerAdapter;
    private int preposition=0;//上一次高亮的位置
    private boolean isDragging=false;//判断图片是否拖拽
    private boolean isinitscoll=true;//判断图片是否g滚动
    private int isgetmoney;//1有提现功能 0没有提现功能
    private String msginform;

    //自动轮播
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item=0;
            if(imageView_url.size()>1){
                item=viewpager.getCurrentItem()+1;
                viewpager.setCurrentItem(item);
                handler.sendEmptyMessageDelayed(0,3000);
            }else {
                item=viewpager.getCurrentItem();
                viewpager.setCurrentItem(item);
                handler.removeCallbacksAndMessages(null);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_store);

        SysUtils.setupUI(this,findViewById(R.id.activity_main_store));
        initToolbar(this);

        initView();
//        initData();
        getMainStore();
        getSellerInfoData();
//        isketixian();
    }

    private void initData() {
        imageView_list=new ArrayList<ImageView>();
        //添加滚动图片
//        int[] imageView_ids=new int[]{
//                R.drawable.arrow_down, R.drawable.arrow_up, R.drawable.addprice
//        };
        if(imageView_url.size()>0) {
            for (int i = 0; i < imageView_url.size(); i++) {

                com.android.volley.toolbox.NetworkImageView networkImageView=new NetworkImageView(MainStoreActivity.this);
                RequestQueue requestQueue= Volley.newRequestQueue(MainStoreActivity.this);
                ImageLoader imageLoader=new ImageLoader(requestQueue, new BitmapCache());
                networkImageView.setDefaultImageResId(R.drawable.mainstoredefaut);
                networkImageView.setErrorImageResId(R.drawable.mainstoredefaut);
                networkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                networkImageView.setImageUrl(imageView_url.get(i), imageLoader);

                imageView_list.add(networkImageView);
                ImageView imageView_point = new ImageView(this);
                imageView_point.setBackgroundResource(R.drawable.viewpager_point_res);

                //设置滚动图片的左右间距
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(BitmapUtils.Dp2Px(MainStoreActivity.this, 5),
                        BitmapUtils.Dp2Px(MainStoreActivity.this, 5));
                if (i == preposition) {
                    imageView_point.setEnabled(true);
                } else {
                    imageView_point.setEnabled(false);
                    layoutParams.leftMargin = BitmapUtils.Dp2Px(MainStoreActivity.this, 5);
                }
                imageView_point.setLayoutParams(layoutParams);
                point_layout.addView(imageView_point);
            }
            mainStoreViewPagerAdapter = new MainStoreViewPagerAdapter(MainStoreActivity.this, imageView_list, handler);
            viewpager.setAdapter(mainStoreViewPagerAdapter);
            //初始位置在中间位置，并保证为整数倍
//            int item = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageView_url.size();//
//            System.out.println("item：" + item);
//            viewpager.setCurrentItem(item);
            //发消息
            handler.sendEmptyMessageDelayed(0, 3000);
            viewpager.addOnPageChangeListener(this);
        }
    }

    private void initView() {
        viewpager= (ViewPager) findViewById(R.id.viewpager);
        point_layout= (LinearLayout) findViewById(R.id.point_layout);
        iv_viewpage_text= (TextView) findViewById(R.id.iv_viewpage_text);
        storemanagement_layout= (RelativeLayout) findViewById(R.id.storemanagement_layout);
        getmoney_layout= (RelativeLayout) findViewById(R.id.getmoney_layout);
        storeinfo_layout= (RelativeLayout) findViewById(R.id.storeinfo_layout);
        dataanalysis_layout= (RelativeLayout) findViewById(R.id.dataanalysis_layout);
        btn_set= (TextView) findViewById(R.id.btn_set);
        btn_set.setVisibility(View.VISIBLE);
        btn_set.setBackgroundColor(Color.parseColor("#ffff8905"));
        btn_set.setText("退出");
        tv_horizontalscroll= (TextView) findViewById(R.id.tv_horizontalscroll);


        storemanagement_layout.setOnClickListener(this);
        getmoney_layout.setOnClickListener(this);
        storeinfo_layout.setOnClickListener(this);
        dataanalysis_layout.setOnClickListener(this);
        btn_set.setOnClickListener(this);
        isinitscoll=true;
        istonch();

    }

    /**
     * 限制滚动
     */
    private void istonch(){
    viewpager.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(isinitscoll){
                return false;
            }else {
                return true;
            }
        }
    });
}
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.storemanagement_layout:
                Intent intent=new Intent(MainStoreActivity.this,MainBranchSelectActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                break;
            case R.id.getmoney_layout:
                isketixian();
                break;
            case R.id.storeinfo_layout:
                break;
            case R.id.dataanalysis_layout:
                break;
            case R.id.btn_set:
                new MaterialDialog.Builder(MainStoreActivity.this)
                        .theme(SysUtils.getDialogTheme())
                        .content(getString(R.string.are_you_sure_sign_out))
                        .positiveText(getString(R.string.sure))
                        .negativeText(getString(R.string.cancel))
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                LoginUtils.logout(MainStoreActivity.this, 3);
                                SysUtils.showSuccess(getString(R.string.signed_out));

                                finish();
                            }
                        })
                        .show();
                break;
        }

    }

    /**
     * 页面滚动了的时候回调这个方法
     * @param position 当前位置
     * @param positionOffset 滑动页面的百分比
     * @param positionOffsetPixels 在屏幕上滚动的像素
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 页面被选中的时候回调
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        int realposition=position%imageView_url.size();
        point_layout.getChildAt(preposition).setEnabled(false);
        point_layout.getChildAt(realposition).setEnabled(true);
        preposition=realposition;
    }

    /**
     * 页面滚动状态发生变化时回调这个方法
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {

        if(state==ViewPager.SCROLL_STATE_DRAGGING){
            isDragging=true;

        }else if(state==ViewPager.SCROLL_STATE_SETTLING){

        }else if(state==ViewPager.SCROLL_STATE_IDLE&&isDragging){
            handler.removeCallbacksAndMessages(null);
            handler.sendEmptyMessageDelayed(0,3000);
        }

    }

    /**
     * 获取轮播图
     */
    private void getMainStore() {

        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("main_store"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    isinitscoll=true;
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("图片结果："+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        JSONArray array = dataObject.getJSONArray("banner");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                String str_url=SysUtils.getWebUri()+array.get(i);
                                imageView_url.add(str_url);
                            }
                        }
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    initData();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                SysUtils.showError("网络不给力");
                imageView_list=new ArrayList<ImageView>();
                ImageView imageView=new ImageView(MainStoreActivity.this);
                imageView.setBackgroundResource(R.drawable.mainstoredefaut);
                imageView_list.add(imageView);
                mainStoreViewPagerAdapter = new MainStoreViewPagerAdapter(MainStoreActivity.this, imageView_list, handler);
                viewpager.setAdapter(mainStoreViewPagerAdapter);
//                handler.removeCallbacksAndMessages(null);
                isinitscoll=false;
                istonch();
            }
        });

        executeRequest(r);

    }

    /**
     * 获取是有可提现权限
     */
    private void isketixian() {

        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("get_isktx"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    isinitscoll=true;
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("ret："+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        isgetmoney = dataObject.getInt("ketixian");
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    if(isgetmoney==1){
                        Intent intentgetmoney=new Intent(MainStoreActivity.this,GetMoneyActivity.class);
                        intentgetmoney.putExtra("isstore",1);
                        startActivity(intentgetmoney);
                    }else {
                        SysUtils.showError("您没有开启提现功能！");
                    }
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        finish();
    }
    /**
     * 获取商家信息
     */
    private void getSellerInfoData() {

        CustomRequest  r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("sellerinfo"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");
                    System.out.println("ret="+ret);

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        msginform=dataObject.getString("msginform");
                        tv_horizontalscroll.setText(msginform);
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

}
