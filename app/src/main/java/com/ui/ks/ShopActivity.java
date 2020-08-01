package com.ui.ks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.MyApplication.KsApplication;
import com.base.BaseActivity;
import com.ui.fragment.DisposeFragment;
import com.ui.fragment.MainFragment;
import com.ui.fragment.ManagerFragment;
import com.ui.fragment.ProfileFragment;
import com.ui.fragment.ReportFragment;
import com.ui.global.Global;
import com.ui.util.LoginUtils;
import com.ui.util.SysUtils;
import com.ui.view.FragmentTabHost;

public class ShopActivity extends BaseActivity implements OnTabChangeListener {
    private String init_tab = "";

    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {MainFragment.class,ManagerFragment.class,ReportFragment.class,ProfileFragment.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.selector_btn_main,R.drawable.selector_btn_main2,R.drawable.selector_btn_report,R.drawable.selector_btn_profile};
    //Tab选项卡的文字
    private int mTextviewArray[] = {R.string.order_list, R.string.store_management, R.string.business_statistics, R.string.merchant_center};


    //定义数组来存放Fragment界面
    private Class fragmentArray2[] = {MainFragment.class,DisposeFragment.class,ProfileFragment.class};
    //定义数组来存放按钮图片
    private int mImageViewArray2[] = {R.drawable.selector_btn_main,R.drawable.selector_btn_main2,R.drawable.selector_btn_profile};
    //Tab选项卡的文字
    private String mTextviewArray2[] = {"订单列表", "店铺管理", "商户中心"};


    private String current_tab = "";

    private boolean jurisdiction = false;

    private int orderPay = 1;


    private MainFragment fragOrder = null;

    private int curIndex=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(LoginUtils.isShopper()){
            super.onCreate(savedInstanceState, 4);
        }else {
            super.onCreate(savedInstanceState, 1);
        }
        setContentView(R.layout.activity_shop);

        setTintColor(1);

        initView();
    }

    /**
     * 初始化组件
     */
    private void initView(){
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);
        KsApplication.selectItem=0;//每次订单列表页面刷新，订单类型都重新初始化


        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.setOnTabChangedListener(this);

        //得到fragment的个数
        int count = jurisdiction ? fragmentArray2.length : fragmentArray.length;

        for(int i = 0; i < count; i++){
            if (jurisdiction) {
                TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray2[i]).setIndicator(getTabItemView(i));
                mTabHost.addTab(tabSpec, fragmentArray2[i], null);

            } else {
                TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]+"").setIndicator(getTabItemView(i));
                mTabHost.addTab(tabSpec, fragmentArray[i], null);

            }
        }
        curIndex=getIntent().getIntExtra("curIndex",0);
        mTabHost.setCurrentTab(curIndex);

    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_home_image);
        TextView textView = (TextView) view.findViewById(R.id.iv_home_text);

        if (jurisdiction) {
            imageView.setImageResource(mImageViewArray2[index]);
            textView.setText(mTextviewArray2[index]);
        } else {
            imageView.setImageResource(mImageViewArray[index]);
            textView.setText(mTextviewArray[index]);
        }

        return view;
    }




    public void setOrderPay(int pay) {
        this.orderPay = pay;
        fragOrder= (MainFragment)getSupportFragmentManager().findFragmentByTag(mTextviewArray2[0]);
        if(fragOrder != null) {
            fragOrder.setPay(orderPay);
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        if(tabId.equals("店铺管理")) {
//            updateView("main2");
        } else if(tabId.equals("营业统计")) {
//            updateView("report");
        } else if(tabId.equals("商户中心")) {
//            updateView("profile");
        } else if(tabId.equals("订单列表")) {
//            updateView("main");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainFragment fragOrder = (MainFragment)getSupportFragmentManager().findFragmentByTag(mTextviewArray2[0]);
                }
            }, 1000);

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
         finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(current_tab.equals("main")) {
            getMenuInflater().inflate(R.menu.menu_search, menu);
        } else if(current_tab.equals("profile")) {
            getMenuInflater().inflate(R.menu.menu_refresh, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(current_tab.equals("main")) {
            if (id == R.id.menu_search) {
                SysUtils.startAct(ShopActivity.this, new SearchActivity());
                return true;
            }
        } else if(current_tab.equals("profile")) {
            if (id == R.id.menu_refresh) {
                sendBroadcast(new Intent(Global.BROADCAST_REFRESH_PROFILE_ACTION)
                        .putExtra("type", 2));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle b = data.getExtras();
            //城市定位
            if(b != null && b.containsKey("refreshReport")) {
                sendBroadcast(new Intent(Global.BROADCAST_REFRESH_SHOP_REPORT_ACTION));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
