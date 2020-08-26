package com.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.LogUtils;
import com.library.base.mvp.BaseFragment;
import com.ui.ks.R;

import butterknife.BindView;

/**
 * 商品销售统计
 * Created by lyf on 2020/8/8.
 */

public class TotalSalesStatisticsFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.rb_cur_month)
    RadioButton rbCurMonth;
    @BindView(R.id.rb_pre_month)
    RadioButton rbPreMonth;
    @BindView(R.id.rg_btn)
    RadioGroup rgBtn;
    @BindView(R.id.frag_layout)
    FrameLayout fragLayout;

    private FragmentManager fragmentManager;
    //当前正在展示的Fragment
    private BaseFragment showFragment;
    private SalesStatisticsFragment curMonthSalesStatisticsFragment;
    private SalesStatisticsFragment lastMonthSalesStatisticsFragment;

    @Override
    protected int setContentViewId() {
        return R.layout.total_sales_statistics_layout;
    }

    @Override
    protected void loadData() {
        initFragment();
    }

    private void initFragment() {
        fragmentManager = getChildFragmentManager();

        curMonthSalesStatisticsFragment = new SalesStatisticsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("month", 0);
        curMonthSalesStatisticsFragment.setArguments(bundle);

        lastMonthSalesStatisticsFragment = new SalesStatisticsFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("month", -1);
        lastMonthSalesStatisticsFragment.setArguments(bundle1);

        //商品销售统计
        rgBtn.setOnCheckedChangeListener(this);
        rbCurMonth.setChecked(true);


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_cur_month:
                LogUtils.i("SalesStatisticsFragment   rb_cur_month");
                showFragment(R.id.frag_layout, curMonthSalesStatisticsFragment, "rb_cur_month");
                break;
            case R.id.rb_pre_month:
                LogUtils.i("SalesStatisticsFragment   rb_pre_month");
                showFragment(R.id.frag_layout, lastMonthSalesStatisticsFragment, "rb_pre_month");
                break;

        }
    }


    /**
     * 显示隐藏Fragment
     */
    protected void showFragment(int resid, BaseFragment fragment, String tag) {
        LogUtils.i("SalesStatisticsFragment " + tag);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //隐藏正在显示的Fragment
        if (showFragment != null) {
            fragmentTransaction.hide(showFragment);
        }
        //展示需要显示的Fragment对象
        Fragment mFragment = fragmentManager.findFragmentByTag(fragment.getClass().getName() + tag);
        if (mFragment != null) {
            fragmentTransaction.show(mFragment);
            showFragment = (BaseFragment) mFragment;
        } else {
            fragmentTransaction.add(resid, fragment, fragment.getClass().getName() + tag);
            showFragment = fragment;
        }
        fragmentTransaction.commit();
    }
}
