package com.ui.ks;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.base.BaseActivity;
import com.ui.adapter.HomePageFragmentNearlyShopperAdapter;
import com.ui.adapter.MainStoreViewPagerAdapter;
import com.ui.entity.NearlyShopper;
import com.ui.listview.PagingListView;
import com.ui.util.BitmapCache;
import com.ui.util.BitmapUtils;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingSortNearlyShopperActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back,iv_search,imageView_point;
    private TextView tv_title;
    private ViewPager viewpager;
    private LinearLayout point_layout;
    private PagingListView lv_content;
    private ArrayList<String> imageView_url=new ArrayList<String>();
    private int preposition=0;//上一次高亮的位置
    private boolean isDragging=false;//判断图片是否拖拽
    private boolean isinitscoll=true;//判断图片是否g滚动
    private ArrayList<ImageView>  imageView_list;
    private MainStoreViewPagerAdapter mainStoreViewPagerAdapter;
    private ArrayList<NearlyShopper> nearlyShoppers_list;
    private String cat_id;
    private String cat_name;

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
        setContentView(R.layout.activity_shopping_sort_nearly_shopper);
        SysUtils.setupUI(ShoppingSortNearlyShopperActivity.this,findViewById(R.id.activity_shopping_sort_nearly_shopper));
        initToolbar(this);

        initView();
        initListener();
    }

    private void initView() {
        iv_back= (ImageView) findViewById(R.id.iv_back);
        iv_search= (ImageView) findViewById(R.id.iv_search);
        tv_title= (TextView) findViewById(R.id.tv_title);
        viewpager= (ViewPager) findViewById(R.id.viewpager);
        point_layout= (LinearLayout) findViewById(R.id.point_layout);
        lv_content= (PagingListView) findViewById(R.id.lv_content);

        iv_back.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        isinitscoll=true;

        Intent intent=getIntent();
        if(intent!=null){
            cat_id=intent.getStringExtra("cat_id");
            cat_name=intent.getStringExtra("cat_name");
            tv_title.setText(cat_name);
        }
        getData(cat_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                break;
        }

    }
    //设置广告图
    private void setAdvertisementPhoto() {
        //获取到图片，获取失败添加默认图片
        if(imageView_url.size()>0){
            for(int i=0;i<imageView_url.size();i++){
                imageView_point = new ImageView(this);
                NetworkImageView networkImageView=new NetworkImageView(this);
                RequestQueue requestQueue= Volley.newRequestQueue(this);
                com.android.volley.toolbox.ImageLoader imageLoader=new com.android.volley.toolbox.ImageLoader(requestQueue,new BitmapCache());
                networkImageView.setDefaultImageResId(R.drawable.mainstoredefaut);
                networkImageView.setErrorImageResId(R.drawable.mainstoredefaut);
                networkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                networkImageView.setImageUrl(imageView_url.get(i),imageLoader);
                //将图片放进集合，创建适配器
                imageView_list.add(networkImageView);
                //添加图片的小圆点
                imageView_point.setBackgroundResource(R.drawable.viewpager_point_res);

                //设置滚动图片的左右间距
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(BitmapUtils.Dp2Px(this, 5),
                        BitmapUtils.Dp2Px(this, 5));
                if (i == preposition) {
                    imageView_point.setEnabled(true);
                } else {
                    imageView_point.setEnabled(false);
                    layoutParams.leftMargin = BitmapUtils.Dp2Px(this, 5);
                }
                imageView_point.setLayoutParams(layoutParams);
                // ImageViews集合中的图片个数在[2,3]时会存在问题，递归再次填充一遍
//                point_layout.removeView(imageView_point);
                point_layout.addView(imageView_point);
            }
        }
        //将图片放进集合，创建适配器
        mainStoreViewPagerAdapter = new MainStoreViewPagerAdapter(ShoppingSortNearlyShopperActivity.this, imageView_list, handler);
        viewpager.setAdapter(mainStoreViewPagerAdapter);
        //发消息
        handler.sendEmptyMessageDelayed(0, 3000);
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
    //初始化监听
    private void initListener() {
        //设置自动滚动，及滚动监听
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int realposition = position % imageView_url.size();
                point_layout.getChildAt(preposition).setEnabled(false);
                point_layout.getChildAt(realposition).setEnabled(true);
                preposition = realposition;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    isDragging = true;

                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {

                } else if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) {
                    handler.removeCallbacksAndMessages(null);
                    handler.sendEmptyMessageDelayed(0, 3000);
                }

            }
        });
    }
    /**
     * 获取数据
     */
    private void getData(String cat_id) {
        Map<String,String> map=new HashMap<>();
            map.put("cat_id",cat_id);
        System.out.println("map="+map);
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("supplier"), map, new Response.Listener<JSONObject>() {
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
                        //广告轮播图
                        JSONArray params_img = dataObject.getJSONArray("params_img");
                        imageView_list=new ArrayList<ImageView>();
                        if (params_img != null && params_img.length() > 0) {
                            imageView_url.clear();
                            imageView_list.clear();
                            point_layout.removeView(imageView_point);
                            for (int i=0; i < params_img.length();i++) {
                                JSONObject jsonObject_cat=params_img.getJSONObject(i);
                                String str_url=jsonObject_cat.getString("link");
                                imageView_url.add(str_url);
                            }
                        }
                        //商品类
                        JSONArray seller_list= dataObject.getJSONArray("seller_list");
                        if(nearlyShoppers_list!=null){
                            nearlyShoppers_list.clear();
                        }
                        nearlyShoppers_list=new ArrayList<>();
                        if(seller_list!=null&&seller_list.length()>0){
                            for(int i=0;i<seller_list.length();i++){
                                JSONObject jsonObject_seller_list=seller_list.getJSONObject(i);
                                String range=jsonObject_seller_list.getString("range");
                                String seller_id=jsonObject_seller_list.getString("seller_id");
                                String seller_name=jsonObject_seller_list.getString("seller_name");
                                String intro=jsonObject_seller_list.getString("intro");
                                String logo=jsonObject_seller_list.getString("logo");
                                NearlyShopper nearlySeller_list=new NearlyShopper(seller_id,logo,seller_name,range,intro);
                                nearlyShoppers_list.add(nearlySeller_list);
                            }
                            lv_content.setAdapter(new HomePageFragmentNearlyShopperAdapter(ShoppingSortNearlyShopperActivity.this,nearlyShoppers_list));

                        }
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                    System.out.println("e="+e.toString());
                } finally {
                    setAdvertisementPhoto();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                SysUtils.showError("网络不给力");
                imageView_list=new ArrayList<ImageView>();
                ImageView imageView=new ImageView(ShoppingSortNearlyShopperActivity.this);
                imageView.setBackgroundResource(R.drawable.mainstoredefaut);
                imageView_list.add(imageView);
                mainStoreViewPagerAdapter = new MainStoreViewPagerAdapter(ShoppingSortNearlyShopperActivity.this, imageView_list, handler);
                viewpager.setAdapter(mainStoreViewPagerAdapter);
                isinitscoll=false;
                istonch();
            }
        });
        executeRequest(r);
    }
}
