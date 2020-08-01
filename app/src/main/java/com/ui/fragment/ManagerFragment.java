package com.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.base.BaseFragment;
import com.constant.RouterPath;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ui.adapter.MainStoreViewPagerAdapter;
import com.ui.entity.News;
import com.ui.global.Global;
import com.ui.ks.DeskActivity;
import com.ui.ks.GoodsManagementActivity;
import com.ui.ks.Goods_Sales_StatisticsAcitvity;
import com.ui.ks.H5GoodsPreviewActivity;
import com.ui.ks.InventoryActivity;
import com.ui.ks.MarketingActivity;
import com.ui.ks.MemberManageActivity;
import com.ui.ks.OpenOrderActivity;
import com.ui.ks.Out_In_operationActivity;
import com.ui.ks.R;
import com.ui.ks.ShopActivity;
import com.ui.ks.Webpage_Activity;
import com.ui.util.BitmapCache;
import com.ui.util.BitmapUtils;
import com.ui.util.CustomRequest;
import com.ui.util.RequestManager;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class  ManagerFragment extends BaseFragment {
    private ImageView iv_managmentfragment_back;
    private TextView tv_previews;
    private LinearLayout point_layout;
    private RelativeLayout goods_management_layout,sales_statistics_layout,wholesaleorders_layout,storeinfo_layout,member_layout,offer_layout,Inventory_layout,Report_loss_layout,Report_Marketing_layout,Desk_layout;
    private ArrayList<String> imageView_url=new ArrayList<String>();
    private ArrayList<ImageView>  imageView_list;
    private int preposition=0;//上一次高亮的位置
    private ImageView imageView_point ;

    SliderLayout slider;
    PagerIndicator custom_indicator;

    public ArrayList<News> ad_list;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ad_list = new ArrayList<News>();
    }

    public static ManagerFragment newInstance() {
        ManagerFragment f = new ManagerFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager, container, false);
        initView(view);
        initListener();
        getData();

        return view;
    }

    private void getData() {
        //获取图片url
        //获取到图片，获取失败添加默认图片
        imageView_list=new ArrayList<ImageView>();
        if(imageView_url.size()>0){
            for(int i=0;i<imageView_url.size();i++){
                NetworkImageView networkImageView=new NetworkImageView(getActivity());
                RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
                ImageLoader imageLoader=new ImageLoader(requestQueue,new BitmapCache());
                networkImageView.setDefaultImageResId(R.drawable.mainstoredefaut);
                networkImageView.setErrorImageResId(R.drawable.mainstoredefaut);
                networkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                networkImageView.setImageUrl(imageView_url.get(i),imageLoader);
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
                point_layout.addView(imageView_point);
            }
        }
    }


    private void loadAd(View adView) {
        if(ad_list.size() > 0) {
            //有广告
                RelativeLayout ll = (RelativeLayout) adView.findViewById(R.id.ad_layout);
                slider = (SliderLayout) adView.findViewById(R.id.slider);
                custom_indicator = (PagerIndicator) adView.findViewById(R.id.custom_indicator);

                int width = Global.magicWidth;
                int height = width * 8 / 15;

                //设置高度
                ll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                slider.setPresetTransformer(SliderLayout.Transformer.Default);
                slider.setDuration(Global.magicDuration);
            slider.removeAllSliders();
//        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slider.setCustomIndicator(custom_indicator);
//            slider.setCustomAnimation(new DescriptionAnimation());
            for(int i = 0; i < ad_list.size(); i++) {
                News bean = ad_list.get(i);

                TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView
                        .description(bean.getSubject())
                        .image(bean.getPic_url())
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                News bean = slider.getBundle().getParcelable("data");
                                String u = bean.getLinkurl();

                                if (!StringUtils.isEmpty(u)) {
//                                    Bundle b = new Bundle();
//                                    b.putString("title", bean.getSubject());
//                                    b.putString("url", u);
//                                    SysUtils.startAct(getActivity(), new AdActivity(), b);
                                    Uri uri = Uri.parse(u);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
//                                SysUtils.newsClick(getActivity(), bean);
                            }
                        });

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putParcelable("data", bean);

                slider.addSlider(textSliderView);
            }
        } else {
            //没有广告，尝试移除view
        }
    }


    //监听事件
    private void initListener() {
        //预览
        tv_previews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),H5GoodsPreviewActivity.class);
                startActivity(intent);
            }
        });
        //返回
        iv_managmentfragment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        //商品管理
        goods_management_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),GoodsManagementActivity.class);
                startActivity(intent);
            }
        });
        //商品销售统计
        sales_statistics_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Goods_Sales_StatisticsAcitvity.class);
                startActivity(intent);
            }
        });

        //出入库报表
        storeinfo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Out_In_operationActivity.class);
                startActivity(intent);
            }
        });


        //批发订货
        //网页优惠券
        wholesaleorders_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(),WholeSaleOrdersActivity1.class);
//                startActivity(intent);
                Intent intent=new Intent(getActivity(), Webpage_Activity.class);
                startActivity(intent);
            }
        });
        //会员管理的界面
        member_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), MemberManageActivity.class);
                startActivity(intent);
            }
        });
        //报价
        offer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.CAMERA},222);
                        return;
                    }else{
                        Intent intentopen=new Intent(getActivity(),OpenOrderActivity.class);
                        intentopen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentopen.putExtra("type","0");
                        startActivity(intentopen);
                    }
                } else {
                    Intent intentopen=new Intent(getActivity(),OpenOrderActivity.class);
                    intentopen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentopen.putExtra("type","0");
                    startActivity(intentopen);
                }
            }
        });
        //盘点
        Inventory_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), InventoryActivity.class);
                startActivity(intent);
            }
        });
        //报损
        Report_loss_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(RouterPath.ACTIVITY_REPORT_LOSS).navigation();
            }
        });

        //营销
        Report_Marketing_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),MarketingActivity.class);
                startActivity(intent);
            }
        });

        //桌上点餐
        Desk_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),DeskActivity.class);
                startActivity(intent);
            }
        });

        //设置自动滚动，及滚动监听
//        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                int realposition=position%imageView_url.size();
//                point_layout.getChildAt(preposition).setEnabled(false);
//                point_layout.getChildAt(realposition).setEnabled(true);
//                preposition=realposition;
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                if(state==ViewPager.SCROLL_STATE_DRAGGING){
//                    isDragging=true;
//
//                }else if(state==ViewPager.SCROLL_STATE_SETTLING){
//
//                }else if(state==ViewPager.SCROLL_STATE_IDLE&&isDragging){
////                    handler.removeCallbacksAndMessages(null);
////                    handler.sendEmptyMessageDelayed(0,3000);
//                }
//
//            }
//        });
    }

    public void initView(View view) {
        iv_managmentfragment_back= (ImageView) view.findViewById(R.id.iv_managmentfragment_back);
//        viewpager= (ViewPager) view.findViewById(R.id.viewpager);
        point_layout= (LinearLayout) view.findViewById(R.id.point_layout);
        goods_management_layout= (RelativeLayout) view.findViewById(R.id.goods_management_layout);
        sales_statistics_layout= (RelativeLayout) view.findViewById(R.id.sales_statistics_layout);
        wholesaleorders_layout= (RelativeLayout) view.findViewById(R.id.wholesaleorders_layout);
        storeinfo_layout= (RelativeLayout) view.findViewById(R.id.storeinfo_layout);
        member_layout=(RelativeLayout) view.findViewById(R.id.member_layout);
        Desk_layout=(RelativeLayout) view.findViewById(R.id.Desk_layout);

        offer_layout= (RelativeLayout) view.findViewById(R.id.offer_layout);
        Inventory_layout= (RelativeLayout) view.findViewById(R.id.Inventory_layout);
        Report_loss_layout= (RelativeLayout) view.findViewById(R.id.Report_loss_layout);

        Report_Marketing_layout=(RelativeLayout) view.findViewById(R.id.Report_Marketing_layout);

        tv_previews= (TextView) view.findViewById(R.id.tv_previews);

        imageView_point = new ImageView(getActivity());
        //获取图片url
        getMainStore(view);
    }
    /**
     * 获取轮播图
     */
    private void getMainStore(final View view) {

        CustomRequest r1 = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("open"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject ret = SysUtils.didResponse(response);
                    System.out.println("图片结果：" + ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        JSONArray array = dataObject.optJSONArray("params_img");
//                        JSONArray array = dataObject.getJSONArray("banner");
                        if (array != null && array.length() > 0) {
                            if (imageView_url.size() > 0) {
                                imageView_url.clear();
                                imageView_list.clear();
                                point_layout.removeView(imageView_point);
                            }
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject data = array.optJSONObject(i);

                                News b = new News(0,
                                        1,
                                        0,
                                        data.getString("linkinfo"),
                                        data.getString("linktarget"),
                                        data.getString("link"),
                                        "");
                                ad_list.add(b);
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    loadAd(view);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SysUtils.showError("网络不给力");
                imageView_list=new ArrayList<ImageView>();
                ImageView imageView=new ImageView(getActivity());
                imageView.setBackgroundResource(R.drawable.mainstoredefaut);
                imageView_list.add(imageView);
            }
        });

        executeRequest(r1);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }
}

