package com.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.base.BaseFragment;
import com.ui.ks.R;
import com.ui.view.avtivity.ShopActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DisposeFragment extends BaseFragment {
    private ShopActivity mainAct;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainAct = (ShopActivity) getActivity();
    }

    public static DisposeFragment newInstance() {
        DisposeFragment f = new DisposeFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mainAct.updateView("home");

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        pager = (ViewPager) view.findViewById(R.id.pager);

        initView();

        return view;
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

//        private final String[] TITLES = { "全部订单", "到店付款", "待配送" };
        private final String[] TITLES = { " " };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return OrderFragment.newInstance(position + 1, "2");
        }
    }

    public void initView() {
        adapter = new MyPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        pager.setOffscreenPageLimit(3);
    }
}

