package com.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.ui.adapter.MainStoreViewPagerAdapter;
import com.ui.adapter.HomePageFragmentNearlyShopperAdapter;
import com.ui.entity.NearlyShopper;
import com.ui.entity.ShopperSortInfo;
import com.ui.global.Global;
import com.ui.ks.MoreShoppingSortActivity;
import com.ui.ks.NearlyShopperGoodsActivity;
import com.ui.ks.R;
import com.ui.ks.ShoppingHomePageSearchActivity;
import com.ui.ks.ShoppingSortNearlyShopperActivity;
import com.ui.util.BitmapCache;
import com.ui.util.BitmapUtils;
import com.ui.util.CustomRequest;
import com.ui.util.NoDoubleClickUtils;
import com.ui.util.SysUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ShoppingMallHomePageFragment extends BaseFragmentMainBranch implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ViewPager viewpager;
    private LinearLayout point_layout;
    private ArrayList<String>  imageView_url=new ArrayList<String>();;
    private ArrayList<ImageView>  imageView_list;
    private MainStoreViewPagerAdapter mainStoreViewPagerAdapter;
    private ImageView imageView_point,imageView1,imageView2,imageView3,imageView4,imageView5,iv_back ;
    private TextView textView1,textView2,textView3,textView4,textView5;
    private RelativeLayout layout1,layout2,layout3,layout4,layout5,layout_search;
    private ListView lv_nearlyshopper;
    private EditText et_search;

    private int preposition=0;//上一次高亮的位置
    private boolean isDragging=false;//判断图片是否拖拽
    private boolean isinitscoll=true;//判断图片是否g滚动
    private DisplayImageOptions options;
    private ArrayList<NearlyShopper> nearlyShoppers_list;
    private ArrayList<ShopperSortInfo> shopperSortInfos;

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
    protected View initView() {
        View view=View.inflate(mContext, R.layout.shoppingmallhomepagefragment,null);
        initview(view);
        initListener();
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        isinitscoll=true;

        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.picture_default)
                .showImageForEmptyUri(R.drawable.picture_default)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(100))//设置图片圆角
                .build();
//获取数据
        getAdvertisementPhoto();


    }
    /***
     * 动态设置listview的高度 item 总布局必须是linearLayout
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
                + 80;
        listView.setLayoutParams(params);
    }
    private void initview(View view){
        viewpager= (ViewPager) view.findViewById(R.id.viewpager);
        lv_nearlyshopper= (ListView) view.findViewById(R.id.lv_nearlyshopper);
        point_layout= (LinearLayout) view.findViewById(R.id.point_layout);
        layout1= (RelativeLayout) view.findViewById(R.id.layout1);
        layout2= (RelativeLayout) view.findViewById(R.id.layout2);
        layout3= (RelativeLayout) view.findViewById(R.id.layout3);
        layout4= (RelativeLayout) view.findViewById(R.id.layout4);
        layout5= (RelativeLayout) view.findViewById(R.id.layout5);
        layout_search= (RelativeLayout) view.findViewById(R.id.layout_search);
        imageView1= (ImageView) view.findViewById(R.id.imageView1);
        imageView2= (ImageView) view.findViewById(R.id.imageView2);
        imageView3= (ImageView) view.findViewById(R.id.imageView3);
        imageView4= (ImageView) view.findViewById(R.id.imageView4);
//        imageView5= (ImageView) view.findViewById(R.id.imageView5);
        iv_back= (ImageView) view.findViewById(R.id.iv_back);
        textView1= (TextView) view.findViewById(R.id.textView1);
        textView2= (TextView) view.findViewById(R.id.textView2);
        textView3= (TextView) view.findViewById(R.id.textView3);
        textView4= (TextView) view.findViewById(R.id.textView4);
//        textView5= (TextView) view.findViewById(R.id.textView5);
        et_search= (EditText) view.findViewById(R.id.et_search);
        et_search.setInputType(InputType.TYPE_NULL);

        lv_nearlyshopper.setOnItemClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);

    }
    //设置广告图
    private void setAdvertisementPhoto() {
        //获取到图片，获取失败添加默认图片
        if(imageView_url.size()>0) {
            for (int i = 0; i < imageView_url.size(); i++) {
                imageView_point = new ImageView(getActivity());
                NetworkImageView networkImageView = new NetworkImageView(getActivity());
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                com.android.volley.toolbox.ImageLoader imageLoader = new com.android.volley.toolbox.ImageLoader(requestQueue, new BitmapCache());
                networkImageView.setDefaultImageResId(R.drawable.mainstoredefaut);
                networkImageView.setErrorImageResId(R.drawable.mainstoredefaut);
                networkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                networkImageView.setImageUrl(imageView_url.get(i), imageLoader);
                //将图片放进集合，创建适配器
                imageView_list.add(networkImageView);
                //添加图片的小圆点
                imageView_point.setBackgroundResource(R.drawable.viewpager_point_res);

                //设置滚动图片的左右间距
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(BitmapUtils.Dp2Px(getActivity(), 5),
                        BitmapUtils.Dp2Px(getActivity(), 5));
                if (i == preposition) {
                    imageView_point.setEnabled(true);
                } else {
                    imageView_point.setEnabled(false);
                    layoutParams.leftMargin = BitmapUtils.Dp2Px(getActivity(), 5);
                }
                imageView_point.setLayoutParams(layoutParams);
                // ImageViews集合中的图片个数在[2,3]时会存在问题，递归再次填充一遍
//                point_layout.removeView(imageView_point);
                point_layout.addView(imageView_point);
            }
            //将图片放进集合，创建适配器
            mainStoreViewPagerAdapter = new MainStoreViewPagerAdapter(getActivity(), imageView_list, handler);
            viewpager.setAdapter(mainStoreViewPagerAdapter);
            //发消息
            handler.sendEmptyMessageDelayed(0, 3000);
        }
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
    private void initListener(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mContext.sendBroadcast(new Intent(Global.BROADCAST_WholeSaleOrdersActivity_ACTION).putExtra("type",1));
            }
        });
        //设置自动滚动，及滚动监听
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int realposition=position%imageView_url.size();
                point_layout.getChildAt(preposition).setEnabled(false);
                point_layout.getChildAt(realposition).setEnabled(true);
                preposition=realposition;

            }

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
        });
//        layout_search.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Toast.makeText(getActivity(),"跳转到搜索页面",Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NoDoubleClickUtils.isDoubleClick()){
                    return;
                }
                Intent intent=new Intent(mContext,ShoppingHomePageSearchActivity.class);
                startActivity(intent);
            }
        });

    }
    /**
     * 获取数据
     */
    private void getAdvertisementPhoto() {
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getGoodsServiceUrl("supplier"),null, new Response.Listener<JSONObject>() {
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
                        JSONArray cat= dataObject.getJSONArray("cat");
                        if(shopperSortInfos!=null){
                            shopperSortInfos.clear();
                        }
                        shopperSortInfos=new ArrayList<>();
                        if(cat!=null&&cat.length()>0){
                            for(int i=0;i<cat.length();i++){
                                JSONObject jsonObject_cat=cat.getJSONObject(i);
                                String cat_id=jsonObject_cat.getString("cat_id");
                                String cat_name=jsonObject_cat.getString("cat_name");
                                String cat_icon=jsonObject_cat.getString("icon");
                                ShopperSortInfo shopperSortInfo=new ShopperSortInfo(cat_id,cat_name,cat_icon);
                                shopperSortInfos.add(shopperSortInfo);
                            }
                            if(shopperSortInfos.size()>0&&shopperSortInfos.size()<=1){
                                ImageLoader.getInstance().displayImage(shopperSortInfos.get(0).getCat_icon(), imageView1, options);
                                textView1.setText(shopperSortInfos.get(0).getCat_name());
                            }
                            if(shopperSortInfos.size()>0&&shopperSortInfos.size()<=2){
                                ImageLoader.getInstance().displayImage(shopperSortInfos.get(0).getCat_icon(), imageView1, options);
                                ImageLoader.getInstance().displayImage(shopperSortInfos.get(1).getCat_icon(), imageView2, options);
                                textView1.setText(shopperSortInfos.get(0).getCat_name());
                                textView2.setText(shopperSortInfos.get(1).getCat_name());
                            }
                            if(shopperSortInfos.size()>0&&shopperSortInfos.size()<=3){
                                ImageLoader.getInstance().displayImage(shopperSortInfos.get(0).getCat_icon(), imageView1, options);
                                ImageLoader.getInstance().displayImage(shopperSortInfos.get(1).getCat_icon(), imageView2, options);
                                ImageLoader.getInstance().displayImage(shopperSortInfos.get(2).getCat_icon(), imageView3, options);
                                textView1.setText(shopperSortInfos.get(0).getCat_name());
                                textView2.setText(shopperSortInfos.get(1).getCat_name());
                                textView3.setText(shopperSortInfos.get(2).getCat_name());
                            }
                            if(shopperSortInfos.size()>=4){
                                ImageLoader.getInstance().displayImage(shopperSortInfos.get(0).getCat_icon(), imageView1, options);
                                ImageLoader.getInstance().displayImage(shopperSortInfos.get(1).getCat_icon(), imageView2, options);
                                ImageLoader.getInstance().displayImage(shopperSortInfos.get(2).getCat_icon(), imageView3, options);
                                ImageLoader.getInstance().displayImage(shopperSortInfos.get(3).getCat_icon(), imageView4, options);
                                textView1.setText(shopperSortInfos.get(0).getCat_name());
                                textView2.setText(shopperSortInfos.get(1).getCat_name());
                                textView3.setText(shopperSortInfos.get(2).getCat_name());
                                textView4.setText(shopperSortInfos.get(3).getCat_name());
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
                            lv_nearlyshopper.setAdapter(new HomePageFragmentNearlyShopperAdapter(getActivity(),nearlyShoppers_list));
                            setListViewHeightBasedOnChildren(lv_nearlyshopper);

                        }
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    setAdvertisementPhoto();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                SysUtils.showError("网络不给力");
                imageView_list=new ArrayList<ImageView>();
                ImageView imageView=new ImageView(getActivity());
                imageView.setBackgroundResource(R.drawable.mainstoredefaut);
                imageView_list.add(imageView);
                mainStoreViewPagerAdapter = new MainStoreViewPagerAdapter(getActivity(), imageView_list, handler);
                viewpager.setAdapter(mainStoreViewPagerAdapter);
                isinitscoll=false;
                istonch();
            }
        });
        executeRequest(r);
    }
//附近商家的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(mContext,NearlyShopperGoodsActivity.class);
        intent.putExtra("shopperid",nearlyShoppers_list.get(position).getShopperid());
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout1:
                if(shopperSortInfos!=null&&shopperSortInfos.size()>0){
                Intent intent=new Intent(getActivity(),ShoppingSortNearlyShopperActivity.class);
                intent.putExtra("cat_id",shopperSortInfos.get(0).getCat_id());
                intent.putExtra("cat_name",shopperSortInfos.get(0).getCat_name());
                startActivity(intent);
            }
                break;
            case R.id.layout2:
                if(shopperSortInfos!=null&&shopperSortInfos.size()>1){
                    Intent intent=new Intent(getActivity(),ShoppingSortNearlyShopperActivity.class);
                    intent.putExtra("cat_id",shopperSortInfos.get(1).getCat_id());
                    intent.putExtra("cat_name",shopperSortInfos.get(1).getCat_name());
                    startActivity(intent);
                }
                break;
            case R.id.layout3:
                if(shopperSortInfos!=null&&shopperSortInfos.size()>2){
                    Intent intent=new Intent(getActivity(),ShoppingSortNearlyShopperActivity.class);
                    intent.putExtra("cat_id",shopperSortInfos.get(2).getCat_id());
                    intent.putExtra("cat_name",shopperSortInfos.get(2).getCat_name());
                    startActivity(intent);
                }
                break;
            case R.id.layout4:
                if(shopperSortInfos!=null&&shopperSortInfos.size()>3){
                    Intent intent=new Intent(getActivity(),ShoppingSortNearlyShopperActivity.class);
                    intent.putExtra("cat_id",shopperSortInfos.get(3).getCat_id());
                    intent.putExtra("cat_name",shopperSortInfos.get(3).getCat_name());
                    startActivity(intent);
                }
                break;
            case R.id.layout5:
                if(shopperSortInfos!=null){
                    Intent intent=new Intent(getActivity(),MoreShoppingSortActivity.class);
                    intent.putParcelableArrayListExtra("shopperSortInfos",shopperSortInfos);
                    startActivity(intent);
                }

                break;
        }
    }
}
