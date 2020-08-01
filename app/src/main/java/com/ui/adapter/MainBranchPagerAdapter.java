package com.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.ui.fragment.BaseFragmentMainBranch;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class MainBranchPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager mFragmentManager;
    private List<BaseFragmentMainBranch> mBaseFragment;



    public MainBranchPagerAdapter( FragmentManager mFragmentManager, List<BaseFragmentMainBranch> mBaseFragment) {
        super(mFragmentManager);
        this.mFragmentManager = mFragmentManager;
        this.mBaseFragment = mBaseFragment;
    }

    @Override
    public Fragment getItem(int position) {

        return mBaseFragment.get(position);
    }

    @Override
    public int getCount() {
        return mBaseFragment.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
