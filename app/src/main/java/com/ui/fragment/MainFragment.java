package com.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.base.BaseFragment;
import com.ui.ks.R;
import com.ui.ks.ShopActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainFragment extends BaseFragment {
    private ShopActivity mainAct;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainAct = (ShopActivity) getActivity();
    }

    public static MainFragment newInstance() {
        MainFragment f = new MainFragment();
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
        private final ArrayList<Fragment> mFragments;

//        private final String[] TITLES = { "全部订单", "到店付款", "待配送" };
        private final String[] TITLES = { "" };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>(getCount());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            mFragments.add((Fragment) object);
            return object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mFragments.remove(object);
            super.destroyItem(container, position, object);
        }

        public List<Fragment> getFragments() {
            return Collections.unmodifiableList(mFragments);
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
            Fragment frag = OrderFragment.newInstance(position + 1, "1");
//            fragmentReferences.put(position, new WeakReference<Fragment>(frag));
            return frag;
        }
    }

    public void initView() {
        adapter = new MyPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        pager.setOffscreenPageLimit(1);
    }

    public void setPay(int pay) {
//        Log.v("ks", "pay main: " + pay + ", count: " + adapter.getCount());
        for(int i = 0; i < adapter.getFragments().size(); i++) {
            OrderFragment frag = (OrderFragment) adapter.getFragments().get(i);
            frag.setPay(pay);
        }
    }
}

