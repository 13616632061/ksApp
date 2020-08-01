package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseActivity;
import com.ui.fragment.ShoppingMallHomePageFragment;
import com.ui.fragment.ShoppingMallMyPageFragment;
import com.ui.fragment.ShoppingMallOrderPageFragment;
import com.ui.fragment.ShoppingMallShoppingCarFragment;
import com.ui.global.Global;
import com.ui.util.SysUtils;

import java.util.ArrayList;

public class WholeSaleOrdersActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout layout_homepage,layout_orders,layout_myinfo,layout_choppingcar;
    private ImageView iv_homepage,iv_orders,iv_myinfo,iv_choppingcar;
    private TextView tv_homepage,tv_orders,tv_myinfo,tv_choppingcar;
    private int type;
    private int currentIndex = 0;//控制当前需要显示第几个Fragment
    private ArrayList<Fragment> fragmentArrayList;//用List来存储Fragment,List的初始化没有写
    private Fragment mCurrentFrgment;//显示当前Fragment
    private int position;
    private  ShoppingMallOrderPageFragment  mShoppingMallOrderPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesaleorders);
        SysUtils.setupUI(WholeSaleOrdersActivity.this,findViewById(R.id.activity_wholesaleorders));
        initToolbar(this);

        initFragment();
        initView();
    }

    private void initFragment() {
        fragmentArrayList=new ArrayList<>();
        fragmentArrayList.add(new ShoppingMallHomePageFragment() );
        mShoppingMallOrderPageFragment=new ShoppingMallOrderPageFragment();
        fragmentArrayList.add(mShoppingMallOrderPageFragment);
        fragmentArrayList.add(new ShoppingMallMyPageFragment() );
        fragmentArrayList.add(new ShoppingMallShoppingCarFragment() );
    }

    private void changeTab(int index) {
                  currentIndex = index;
                 FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                  //判断当前的Fragment是否为空，不为空则隐藏
                 if (null != mCurrentFrgment) {
                          ft.hide(mCurrentFrgment);
                     }
                //先根据Tag从FragmentTransaction事物获取之前添加的Fragment
              Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentArrayList.get(currentIndex).getClass().getName());

                if (null == fragment) {
                        //如fragment为空，则之前未添加此Fragment。便从集合中取出
                       fragment = fragmentArrayList.get(index);
                     }
               mCurrentFrgment = fragment;

                //判断此Fragment是否已经添加到FragmentTransaction事物中
            if (!fragment.isAdded()) {
                        ft.add(R.id.fragment, fragment, fragment.getClass().getName());
                      } else {
                          ft.show(fragment);
                      }
                  ft.commit();
              }
    private void initView() {
        layout_homepage= (RelativeLayout) findViewById(R.id.layout_homepage);
        layout_orders= (RelativeLayout) findViewById(R.id.layout_orders);
        layout_myinfo= (RelativeLayout) findViewById(R.id.layout_myinfo);
        layout_choppingcar= (RelativeLayout) findViewById(R.id.layout_choppingcar);
        iv_homepage= (ImageView) findViewById(R.id.iv_homepage);
        iv_orders= (ImageView) findViewById(R.id.iv_orders);
        iv_myinfo= (ImageView) findViewById(R.id.iv_myinfo);
        iv_choppingcar= (ImageView) findViewById(R.id.iv_choppingcar);
        tv_homepage= (TextView) findViewById(R.id.tv_homepage);
        tv_orders= (TextView) findViewById(R.id.tv_orders);
        tv_myinfo= (TextView) findViewById(R.id.tv_myinfo);
        tv_choppingcar= (TextView) findViewById(R.id.tv_choppingcar);

        layout_homepage.setOnClickListener(this);
        layout_orders.setOnClickListener(this);
        layout_myinfo.setOnClickListener(this);
        layout_choppingcar.setOnClickListener(this);
//
        Intent intent=getIntent();
        type=intent.getIntExtra("type",0);
        if(type==1){
            changeTab(0);
            setBootomPress(Color.parseColor("#ffff8905"),Color.parseColor("#000000"),Color.parseColor("#000000"),Color.parseColor("#000000"),
                    R.drawable.home,R.drawable.order_gray,R.drawable.cart_gray,R.drawable.my_gray );
        }else if(type==2){
            changeTab(2);
            setBootomPress(Color.parseColor("#000000"),Color.parseColor("#000000"),Color.parseColor("#000000"),Color.parseColor("#ffff8905"),
                    R.drawable.home_gray,R.drawable.order_gray,R.drawable.cart_gray,R.drawable.my );
        }else if(type==3){
            changeTab(3);
            setBootomPress(Color.parseColor("#000000"),Color.parseColor("#000000"),Color.parseColor("#ffff8905"),Color.parseColor("#000000"),
                    R.drawable.home_gray,R.drawable.order_gray,R.drawable.cart,R.drawable.my_gray );
        }else if(type==4){
            setBootomPress(Color.parseColor("#000000"),Color.parseColor("#ffff8905"),Color.parseColor("#000000"),Color.parseColor("#000000"),
                    R.drawable.home_gray,R.drawable.order,R.drawable.cart_gray,R.drawable.my_gray );
            position=intent.getIntExtra("position",0);
            Bundle mBundle=new Bundle();
            mBundle.putInt("position",position);
            mShoppingMallOrderPageFragment.setArguments(mBundle);
            changeTab(1);
        }
//禁止左右滑动
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_homepage:
                setBootomPress(Color.parseColor("#ffff8905"),Color.parseColor("#000000"),Color.parseColor("#000000"),Color.parseColor("#000000"),
                        R.drawable.home,R.drawable.order_gray,R.drawable.cart_gray,R.drawable.my_gray );
                changeTab(0);
                break;
            case R.id.layout_orders:
                setBootomPress(Color.parseColor("#000000"),Color.parseColor("#ffff8905"),Color.parseColor("#000000"),Color.parseColor("#000000"),
                        R.drawable.home_gray,R.drawable.order,R.drawable.cart_gray,R.drawable.my_gray );
                changeTab(1);
                break;
            case R.id.layout_myinfo:
                setBootomPress(Color.parseColor("#000000"),Color.parseColor("#000000"),Color.parseColor("#000000"),Color.parseColor("#ffff8905"),
                        R.drawable.home_gray,R.drawable.order_gray,R.drawable.cart_gray,R.drawable.my );
                changeTab(2);
                break;
            case R.id.layout_choppingcar:
                setBootomPress(Color.parseColor("#000000"),Color.parseColor("#000000"),Color.parseColor("#ffff8905"),Color.parseColor("#000000"),
                        R.drawable.home_gray,R.drawable.order_gray,R.drawable.cart,R.drawable.my_gray );
                changeTab(3);
                break;
        }
    }

    /**
     * 底栏状态变化
     */
    private void setBootomPress(int type1,int type2,int type3,int type4,int type5,int type6,int type7,int type8){
        tv_homepage.setTextColor(type1);
        tv_orders.setTextColor(type2);
        tv_choppingcar.setTextColor(type3);
        tv_myinfo.setTextColor(type4);
        iv_homepage.setBackgroundResource(type5);
        iv_orders.setBackgroundResource(type6);
        iv_choppingcar.setBackgroundResource(type7);
        iv_myinfo.setBackgroundResource(type8);

    }
private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        int type=intent.getIntExtra("type",0);
        if(type==1){
            finish();
        }
    }
};
    @Override
    protected void onResume() {
        super.onResume();
        WholeSaleOrdersActivity.this.registerReceiver(broadcastReceiver,new IntentFilter(Global.BROADCAST_WholeSaleOrdersActivity_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WholeSaleOrdersActivity.this.unregisterReceiver(broadcastReceiver);
    }
}
