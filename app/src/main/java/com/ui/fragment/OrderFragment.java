package com.ui.fragment;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.apkfuns.logutils.LogUtils;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.ui.adapter.OrderFragmentAdapter;
import com.ui.entity.Order;
import com.ui.global.Global;
import com.ui.ks.CashChargeActivity;
import com.MyApplication.KsApplication;
import com.ui.ks.MyPayCodeActivity;
import com.ui.ks.MyScanCodeActivity;
import com.ui.ks.OpenOrderActivity;
import com.ui.ks.OrderDetailActivity;
import com.ui.ks.OrderSearchActivity;
import com.ui.ks.R;
import com.ui.ks.StatisticsActivity;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.DateUtils;
import com.ui.util.PreferencesService;
import com.ui.util.SelectPicPopupWindow;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;
import com.ui.view.BaseLazyLoadFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单列表页面
 */
public class OrderFragment extends BaseLazyLoadFragment implements SwipeRefreshLayout.OnRefreshListener{
    private int my_position;
    private PagingListView lv_content;
    private SwipeRefreshLayout refresh_header;
    public ArrayList<Order> cat_list;
    private OrderFragmentAdapter adapter;
    private boolean hasInit = false;
    int PAGE = 0;
    int PAGE_hdfk = 0;
    int NUM_PER_PAGE = 20;
    boolean loadingMore = false;
    boolean loadingMore_hdfk = false;
    private boolean isPrepared; //标识是否初始化完成

    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;
    private LinearLayout scroll_layout;//扫描收款码伸缩区域
    private RelativeLayout scancode_layout,paycode_layout,statistics_layout,ordertype_layout;
    private String type = "";

    private String orderId = "";
    private int pay = 1;    //默认已支付
    private int height;//scroll_layout的初始高
    private boolean isFirst = true;// 判断是否第一次进入滑动界面
    private boolean scrollFlag = false;// 表示是否滑动
    private int mCurrentfirstVisibleItem = 0;
    private SparseArray<ItemRecod> recordSp = new SparseArray<ItemRecod>(0);
    private float a1 = 0;//透明度
    private float a2 = 0;//透明度
    public DropDownMenu dropdown_menu;
    private int primaryColor;
    final String[] arr1=new String[]{"全部订单","到店付款","待配送"};

    final String[] strings=new String[]{"全部订单"};
    private boolean jurisdiction = false;
    private MainFragment fragOrder = null;
    private String init_tab = "";
    private int orderPay = 1;
    private String mTextviewArray2[] = {"订单列表", "店铺管理", "商户中心"};
    private SelectPicPopupWindow mSelectPicPopupWindow;
    private ImageView iv_orderfragment_back,iv_orderfragment_search;
    private TextView tv_orderfragment_title,tv_horizontalscroll;
    private ImageView iv_horizontalscroll_cancel;//滚动通知的取消按钮
    private LinearLayout notice_layout;//滚动通知布局
    private RelativeLayout title_scroll_relayout;
    private RelativeLayout order_total_statistics_layout;//订单统计条布局
    private ImageView iv_title_scancode,iv_title_spaycode,iv_title_statistics,iv_title_ordertype;
    private TextView tv_order_date;//订单日期
    private TextView tv_order_total_num;//总订单数
    private TextView tv_order_total_money;//订单总金额
    private  int total=0;//总订单数，初始值为0
    private  int total_hdfk=0;//货到付款和会员订单数，初始值为0
    private  String datetime;//总订单数，初始值为0
    private double total_money;//总订单的金额
    private DecimalFormat df   = new DecimalFormat("######0.00");//小数点两位
    private  String seller_id;
    private String seller_name;
    private String msginform;
    private String tel;
    private View view;
    private PreferencesService service;//偏好设置

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        my_position = getArguments() != null ? getArguments().getInt("position") : 0;
        type = getArguments() != null ? getArguments().getString("type") : "";
    }

    public static OrderFragment newInstance(int position, String type) {
        OrderFragment f = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("type", type);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, container, false);
        layout_err = (View) view.findViewById(R.id.layout_err);
        include_noresult = layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_tv_noresult.setText(getString(R.string.no_order_record));
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
                loadFirst();
            }
        });
        tv_orderfragment_title= (TextView) view.findViewById(R.id.tv_orderfragment_title);
        tv_horizontalscroll= (TextView) view.findViewById(R.id.tv_horizontalscroll);
        scroll_layout= (LinearLayout) view.findViewById(R.id.scroll_layout);
        /**
         * 获取滚动view的初始高度
         */
        scroll_layout.post(new Runnable() {
            @Override
            public void run() {
                height = scroll_layout.getHeight();
            }
        });

        service=new PreferencesService(getActivity());

        scancode_layout= (RelativeLayout) view.findViewById(R.id.scancode_layout);
        paycode_layout= (RelativeLayout) view.findViewById(R.id.paycode_layout);
        statistics_layout= (RelativeLayout) view.findViewById(R.id.statistics_layout);
        ordertype_layout= (RelativeLayout) view.findViewById(R.id.ordertype_layout);
        dropdown_menu = (DropDownMenu) view.findViewById(R.id.dropdown_menu);
        iv_orderfragment_back= (ImageView) view.findViewById(R.id.iv_orderfragment_back);
        iv_orderfragment_search= (ImageView) view.findViewById(R.id.iv_orderfragment_search);
        iv_title_scancode= (ImageView) view.findViewById(R.id.iv_title_scancode);
        iv_title_spaycode= (ImageView) view.findViewById(R.id.iv_title_spaycode);
        iv_title_statistics= (ImageView) view.findViewById(R.id.iv_title_statistics);
        iv_title_ordertype= (ImageView) view.findViewById(R.id.iv_title_ordertype);
       tv_orderfragment_title= (TextView) view.findViewById(R.id.tv_orderfragment_title);
        title_scroll_relayout= (RelativeLayout) view.findViewById(R.id.title_scroll_relayout);
        notice_layout= (LinearLayout) view.findViewById(R.id.notice_layout);
        order_total_statistics_layout= (RelativeLayout) view.findViewById(R.id.order_total_statistics_layout);
        order_total_statistics_layout.setVisibility(View.GONE);
        tv_order_date= (TextView) view.findViewById(R.id.tv_order_date);
        tv_order_total_num= (TextView) view.findViewById(R.id.tv_order_total_num);
        tv_order_total_money= (TextView) view.findViewById(R.id.tv_order_total_money);
        iv_horizontalscroll_cancel= (ImageView) view.findViewById(R.id.iv_horizontalscroll_cancel);
        iv_horizontalscroll_cancel.setVisibility(View.GONE);
        iv_horizontalscroll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice_layout.setVisibility(View.GONE);
                if(cat_list.size() < 1){
                    order_total_statistics_layout.setVisibility(View.GONE);
                }else {
                    order_total_statistics_layout.setVisibility(View.GONE);
                    tv_order_total_num.setText(total+"");
                    tv_order_date.setText(datetime+"");
                    tv_order_total_money.setText("￥"+df.format(total_money));
                }
            }
        });

        iv_orderfragment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        iv_orderfragment_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SysUtils.startAct(getActivity(), new SearchActivity());
//                SysUtils.startAct(getActivity(), new OrderSearchActivity());
                select_popwindow();
            }
        });
        scancode_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scancodeintent=new Intent(getActivity(),MyScanCodeActivity.class);
                scancodeintent.putExtra("type",1);
                startActivity(scancodeintent);
            }
        });
        paycode_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paycodeintent=new Intent(getActivity(),MyPayCodeActivity.class);
                paycodeintent.putExtra("seller_name",seller_name);
                paycodeintent.putExtra("seller_id",seller_id);
                startActivity(paycodeintent);
            }
        });
        statistics_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent statisticsintent=new Intent(getActivity(), StatisticsActivity.class);
//                statisticsintent.putExtra("type",1);
//                startActivity(statisticsintent);
                Intent intentcashchage=new Intent(getActivity(),CashChargeActivity.class);
                startActivity(intentcashchage);
            }
        });
        ordertype_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mSelectPicPopupWindow=new SelectPicPopupWindow(getActivity(), 1, new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        setOrderPay(position + 1);
//                        mSelectPicPopupWindow.dismiss();
//                    }
//                });
//                mSelectPicPopupWindow.showAtLocation(view.findViewById(R.id.scroll_layout) , Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.CAMERA},222);
                        return;
                    }else{
                        Intent intentopen=new Intent(getActivity(),OpenOrderActivity.class);
                        intentopen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentopen);
                    }
                } else {
                    Intent intentopen=new Intent(getActivity(),OpenOrderActivity.class);
                    intentopen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentopen);
                }
            }
        });

        cat_list = new ArrayList<Order>();
        lv_content = (PagingListView) view.findViewById(R.id.lv_content);
        //listview的滚动监听
        lv_content.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                /**
                 * 1 表示滑动 AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 2
                 * 2表示惯性滑动 AbsListView.OnScrollListener.SCROLL_STATE_FLING
                 */
                if (scrollState == 1 || scrollState == 2) {
                    scrollFlag = true;
                    isFirst = false;
                } else {
                    scrollFlag = false;
                }

            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onScroll(final AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!isFirst) {
                    mCurrentfirstVisibleItem = firstVisibleItem;
                    ViewGroup.LayoutParams lp = scroll_layout.getLayoutParams();
                    View firstView = view.getChildAt(0);
                    if (null != firstView) {
                        ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                        if (null == itemRecord) {
                            itemRecord = new ItemRecod();
                        }
                        itemRecord.height = firstView.getHeight();
                        itemRecord.top = firstView.getTop();
                        recordSp.append(firstVisibleItem, itemRecord);
                        int h = getScrollY();
                        int lph = SysUtils.px2dip(getActivity(), h);
                        if (lph < height) {
                            lp.height = height - lph;
                            /**
                             * 根据上滑的距离设置透明度的变化
                             */
                            float alpha = 1f / height;
                            a1 = 1-(lph * alpha);
                            a2=lph * alpha;
                            if(a1<0.9){
                                title_scroll_relayout.setVisibility(View.VISIBLE);
                                tv_orderfragment_title.setVisibility(View.GONE);
                                iv_title_scancode.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        SysUtils.startAct(getActivity(), new MyScanCodeActivity());
                                    }
                                });
                                iv_title_spaycode.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        SysUtils.startAct(getActivity(),new MyPayCodeActivity());
                                    }
                                });
                                iv_title_statistics.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        SysUtils.startAct(getActivity(),new StatisticsActivity());
                                    }
                                });
                                iv_title_ordertype.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mSelectPicPopupWindow=new SelectPicPopupWindow(getActivity(), 1, new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                setOrderPay(position + 1);
                                                mSelectPicPopupWindow.dismiss();
                                            }
                                        });
                                        mSelectPicPopupWindow.showAtLocation(view.findViewById(R.id.lv_content) , Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);

                                    }
                                });
                            }
                            if(a1>0.9){
                                title_scroll_relayout.setVisibility(View.GONE);
                                tv_orderfragment_title.setVisibility(View.VISIBLE);
                            }
                        }
                        scancode_layout.setAlpha(a1);
                        paycode_layout.setAlpha(a1);
                        statistics_layout.setAlpha(a1);
                        ordertype_layout.setAlpha(a1);
                        title_scroll_relayout.setAlpha(a2);
                        scroll_layout.setLayoutParams(lp);
                    }
                }


            }
        });
        lv_content.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if (loadingMore) {
                    //加载更多
                    loadData();
                    loadingMore_hdfk=false;
                    tv_order_total_num.setText(total+"");
                    tv_order_date.setText(datetime+"");
                    tv_order_total_money.setText("￥"+df.format(total_money));
                } else {
                    updateAdapter();
                }
            }
        });
//        adapter = new OrderFragmentAdapter(getActivity(),cat_list);
//        lv_content.setAdapter(adapter);
//        updateAdapter();
        getSellerInfoData();

        //点击
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                int actualPosition = position - lv_content.getHeaderViewsCount();
                if (actualPosition >= 0 && actualPosition < cat_list.size()) {
                    //点击
                    Order data = cat_list.get(actualPosition);

                    Bundle bundle = new Bundle();
                    bundle.putString("order_id", data.getOrderSn());
                    SysUtils.startAct(getActivity(), new OrderDetailActivity(), bundle);
                }
            }
        });

        refresh_header = (SwipeRefreshLayout) view.findViewById(R.id.refresh_header);
        refresh_header.setColorSchemeResources(R.color.ptr_red, R.color.ptr_blue, R.color.ptr_green, R.color.ptr_yellow);
        refresh_header.setOnRefreshListener(this);

        isPrepared = true;
        lazyLoad();

        return view;
    }
    class ItemRecod {
        int height = 0;
        int top = 0;
    }
    /**
     * 获取listview滑动的高度
     *
     * @return
     */
    private int getScrollY() {
        int height = 0;
        for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
            ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
            if(itemRecod!=null) {
                height += itemRecod.height;
            }
        }
        ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
        if (null == itemRecod) {
            itemRecod = new ItemRecod();
        }
        return height - itemRecod.top;
    }
    public void setOrderPay(int pay) {
        this.orderPay = pay;
        KsApplication.selectItem=orderPay-1;
        fragOrder= (MainFragment)getActivity().getSupportFragmentManager().findFragmentByTag(mTextviewArray2[0]);
        if(fragOrder != null) {
            fragOrder.setPay(orderPay);
        }
    }
    @Override
    protected void lazyLoad() {
        if(!isPrepared || !isVisible) {
            return;
        }

        if(!hasInit) {
            refresh_header.post(new Runnable() {
                @Override
                public void run() {
                    setRefreshing(true);
                    loadFirst();
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        loadFirst();

    }

    private void loadFirst() {
        PAGE = 1;
        PAGE_hdfk=1;
        loadData();
        tv_order_total_num.setText(total+"");
        tv_order_date.setText(datetime+"");
        tv_order_total_money.setText("￥"+df.format(total_money));
    }

    private void setRefreshing(boolean refreshing) {
        refresh_header.setRefreshing(refreshing);
    }

    private void loadData() {
        Map<String,String> map = new HashMap<String,String>();
        map.put("page", String.valueOf(PAGE));
        map.put("pagelimit", String.valueOf(NUM_PER_PAGE));
        map.put("type", String.valueOf(my_position));
        map.put("tag", "all");//所有营业统计
        map.put("pay", String.valueOf(1));//1表示已支付

        Log.d("print","打印出来的类型是什么类型"+String.valueOf(my_position));

        String uri = type.equals("1") ? SysUtils.getSellerServiceUrl("orders") : SysUtils.getSellerServiceUrl("dispose");
        System.out.println("uri="+uri);
        System.out.println("map="+map);
        CustomRequest r = new CustomRequest(Request.Method.POST, uri, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                setRefreshing(false);
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = null;

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        System.out.println("mapPAGE="+PAGE);
                        if(PAGE <= 1) {
                            cat_list.clear();
                        }
                        dataObject = ret.getJSONObject("data");
                        JSONArray array = dataObject.getJSONArray("orders_info");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject data = array.optJSONObject(i);

                                Order b = SysUtils.getOrderRow(data);
                                cat_list.add(b);
                            }
                        }
                        total_money=0.00;
                        setView();
                    }

                    if(!hasInit) {
                        hasInit = true;
                    }


                    total = dataObject.getInt("total");
                    datetime=dataObject.getString("datetime");
                    total_money=dataObject.getDouble("total_money");
                    int totalPage = (int)Math.ceil((float)total / NUM_PER_PAGE);
                    loadingMore = totalPage > PAGE;

                    setView();

                    if (loadingMore) {
                        PAGE++;
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    updateAdapter();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setRefreshing(false);
                lv_content.setIsLoading(false);
                setNoNetwork();
                LogUtils.e("请求异常: "+volleyError.getMessage());
            }
        });

        executeRequest(r);
        if(PAGE_hdfk>1||!loadingMore_hdfk){
//            gethdfkorders();
        }
    }

    /**
     * 货到付款
     */
    private void gethdfkorders(){
    Map<String,String> map = new HashMap<String,String>();
    map.put("page", String.valueOf(PAGE_hdfk));
    map.put("pagelimit", String.valueOf(NUM_PER_PAGE));
    map.put("type", String.valueOf(1));
    map.put("tag", "all");//所有营业统计
    map.put("pay", String.valueOf(1));//1表示已支付

    String uri = SysUtils.getSellerServiceUrl("hdfkorders");
    System.out.println("货到付款uri="+uri);
    System.out.println("货到付款map="+map);
    CustomRequest r = new CustomRequest(Request.Method.POST, uri, map, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            setRefreshing(false);

            try {
                JSONObject ret = SysUtils.didResponse(jsonObject);
                System.out.println("货到付款ret="+ret);
                String status = ret.getString("status");
                String message = ret.getString("message");
                JSONObject dataObject = null;

                if (!status.equals("200")) {
                    SysUtils.showError(message);
                } else {
                    dataObject = ret.getJSONObject("data");
                    total+= dataObject.getInt("total");
                    total_hdfk= dataObject.getInt("total");
                    int totalPage = (int)Math.ceil((float)total_hdfk/ NUM_PER_PAGE);
                    System.out.println("货到付款totalPage="+totalPage);
                    loadingMore_hdfk = totalPage > PAGE_hdfk;
                    System.out.println("货到付款loadingMore_hdfk="+loadingMore_hdfk);
                    JSONArray array = dataObject.getJSONArray("orders_info");
                    if (array != null && array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject data = array.optJSONObject(i);

                            Order b = SysUtils.getOrderRow(data);
                            if(cat_list==null){
                                cat_list=new ArrayList<>();
                            }else {
                                if(PAGE_hdfk>=1||!loadingMore_hdfk){
                                    cat_list.add(b);
                                }
                            }
                        }
                    }
                    setView();
                }

                if (loadingMore_hdfk) {
                    PAGE_hdfk++;
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                updateAdapter();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            setRefreshing(false);
            lv_content.setIsLoading(false);
            setNoNetwork();
        }
    });

    executeRequest(r);

}
    private void updateAdapter() {
        for(int i=0;i<cat_list.size()-1;i++){
            for(int j = 0;j <cat_list.size() - 1-i;j++){
                long  isok=  DateUtils. calDateDifferent(cat_list.get(j).getOrderTime(),cat_list.get(j+1).getOrderTime());
                if(isok>0)
                {

                    Collections.swap(cat_list,j,j+1);

//                    String OrderTime=cat_list.get(j).getOrderTime();
//                    int Distribution=cat_list.get(j).getDistribution();
//                    String Payment=cat_list.get(j).getPayment();
//                    String OrderSn=cat_list.get(j).getOrderSn();
//                    double Cost_item=cat_list.get(j).getCost_item();
//
//                    cat_list.get(j).setOrderTime(cat_list.get(j+1).getOrderTime());
//                    cat_list.get(j).setDistribution(cat_list.get(j+1).getDistribution());
//                    cat_list.get(j).setPayment(cat_list.get(j+1).getPayment());
//                    cat_list.get(j).setOrderSn(cat_list.get(j+1).getOrderSn());
//                    cat_list.get(j).setCost_item(cat_list.get(j+1).getCost_item());
//
//                    cat_list.get(j+1).setOrderTime(OrderTime);
//                    cat_list.get(j+1).setDistribution(Distribution);
//                    cat_list.get(j+1).setPayment(Payment);
//                    cat_list.get(j+1).setOrderSn(OrderSn);
//                    cat_list.get(j+1).setCost_item(Cost_item);
            }
            }
        }
            lv_content.onFinishLoading(loadingMore, null);
        if(adapter == null) {
            adapter = new OrderFragmentAdapter(getActivity(),cat_list);
            lv_content.setAdapter(adapter);

        } else {
            adapter.notifyDataSetChanged();
        }
        System.out.println("打印大小"+cat_list.size());
//        adapter.notifyDataSetChanged();
        tv_order_total_num.setText(total+"");
        tv_order_date.setText(datetime+"");
        tv_order_total_money.setText("￥"+df.format(total_money));
    }

    private void setView() {
        if(PAGE <= 1) {
            if(cat_list.size() < 1) {
                //没有结果
                lv_content.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                include_noresult.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            } else {
                //有结果
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.GONE);
                layout_err.setVisibility(View.GONE);
                lv_content.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setNoNetwork() {
        //网络不通
        if(PAGE <= 1 && cat_list.size() < 1) {
            if(!include_nowifi.isShown()) {
                lv_content.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                order_total_statistics_layout.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            }
        } else {
            SysUtils.showNetworkError();
        }
    }


    //更新广播
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean canRefresh = false;
            if(intent.hasExtra("type")) {
                int my_type = intent.getIntExtra("type", 0);
                if(my_type > 0 && my_type == Integer.parseInt(type)) {
                    canRefresh = true;
                }
            }
            if(canRefresh) {
                refresh_header.post(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshing(true);
                        loadFirst();
                    }
                });
            }
        }
    };

    private BroadcastReceiver broadcastCancelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("order_id")) {
                String order_id = intent.getStringExtra("order_id");
                if (!StringUtils.isEmpty(order_id)) {
                    for (int i = 0; i < cat_list.size(); i++) {
                        Order bean = cat_list.get(i);

                        if (bean.getOrderSn().equals(order_id)) {
                            cat_list.remove(i);
//                            adapter.notifyDataSetChanged();
                            updateAdapter();
                            break;
                        }
                    }
                }
            }
        }
    };

    private BroadcastReceiver broadcastAffirmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("order_id")) {
                String order_id = intent.getStringExtra("order_id");
                if (!StringUtils.isEmpty(order_id)) {
                    for (int i = 0; i < cat_list.size(); i++) {
                        Order bean = cat_list.get(i);

                        if (bean.getOrderSn().equals(order_id)) {
                            cat_list.remove(i);
//                            adapter.notifyDataSetChanged();
                            updateAdapter();
                            break;
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        tv_horizontalscroll.setSelected(true);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Global.BROADCAST_REFRESH_ORDER_ACTION));

        if (type.equals("1")) {
            //新订单
            getActivity().registerReceiver(broadcastCancelReceiver, new IntentFilter(Global.BROADCAST_CANCEL_ORDER_ACTION));
            getActivity().registerReceiver(broadcastAffirmReceiver, new IntentFilter(Global.BROADCAST_AFFIRM_ORDER_ACTION));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            getActivity().unregisterReceiver(broadcastReceiver);

            if (type.equals("1")) {
                //新订单
                getActivity().unregisterReceiver(broadcastCancelReceiver);
                getActivity().unregisterReceiver(broadcastAffirmReceiver);
            }
        } catch(Exception e) {

        }
    }

    public void setPay(int my_position ) {
        this.my_position = my_position ;
        if(isPrepared) {
            refresh_header.post(new Runnable() {
                @Override
                public void run() {
                    setRefreshing(true);
                    loadFirst();
                }
            });
        }
    }

    /**
     * 获取商家信息
     */
    private void getSellerInfoData() {

        CustomRequest  r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("sellerinfo"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                setRefreshing(false);

                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");
                    System.out.println("ret="+ret);

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        seller_name=dataObject.getString("seller_name");
                        seller_id=dataObject.getString("seller_id");
                        msginform=dataObject.getString("msginform");
                        tel=dataObject.getString("tel");
                        tv_orderfragment_title.setText(seller_name);
                        tv_horizontalscroll.setText(msginform);
                        service.save_seller_name(seller_name,tel);
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setNoNetwork();
            }
        });
        executeRequest(r);
    }
    /**
     * 库存类型弹出
     */
    private PopupWindow popupWindow;
    private View view_pop;
    private RelativeLayout ordersearch_layout,order_type_layout;
    private void select_popwindow(){
        if(popupWindow==null) {
            view_pop= View.inflate(getActivity(), R.layout.orderfragment_popwindow, null);
            ordersearch_layout = (RelativeLayout) view_pop.findViewById(R.id.ordersearch_layout);
            order_type_layout = (RelativeLayout) view_pop.findViewById(R.id.order_type_layout);
            popupWindow = new PopupWindow(view_pop, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable mColorDrawable = new ColorDrawable(0x20000000);
            //设置弹框的背景
            popupWindow.setBackgroundDrawable(mColorDrawable);
            ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.75f, Animation.RELATIVE_TO_SELF, 0.1f);
            scaleAnimation.setDuration(200);
            view_pop.startAnimation(scaleAnimation);
            //设置弹框添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            view_pop.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int height = view_pop.findViewById(R.id.order_type_layout).getBottom();
                    int height_top = view_pop.findViewById(R.id.ordersearch_layout).getTop();
                    int weight = view_pop.findViewById(R.id.order_type_layout).getLeft();
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
            ordersearch_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(popupWindow!=null){
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                    SysUtils.startAct(getActivity(), new OrderSearchActivity());
                }
            });
            order_type_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(popupWindow!=null){
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                    mSelectPicPopupWindow=new SelectPicPopupWindow(getActivity(), 1, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        setOrderPay(position + 1);
                        mSelectPicPopupWindow.dismiss();
                    }
                });
                mSelectPicPopupWindow.showAtLocation(view.findViewById(R.id.scroll_layout) , Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);

                }
            });
            popupWindow.showAtLocation(iv_orderfragment_search, Gravity.TOP, 0, 0);
        }

    }
}