package com.ui.ks.SalesStatistics;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.constant.RouterPath;
import com.library.base.mvp.BaseActivity;
import com.library.base.mvp.BaseFragment;
import com.ui.fragment.GoodsSalesStatisticsFragment;
import com.ui.fragment.TotalSalesStatisticsFragment;
import com.ui.ks.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description:销售统计
 * @Author:lyf
 * @Date: 2020/8/2
 */
public class SalesStatisticsAcitvity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = SalesStatisticsAcitvity.class.getSimpleName();

    @BindView(R.id.rb_goods_statistics)
    RadioButton rbGoodsStatistics;
    @BindView(R.id.rb_sales_statistics)
    RadioButton rbSalesStatistics;
    @BindView(R.id.frag_layout)
    FrameLayout fragLayout;
    @BindView(R.id.rg_btn)
    RadioGroup rgBtn;
    private FragmentManager fragmentManager;
    //当前正在展示的Fragment
    private BaseFragment showFragment;

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabTitles = new ArrayList<>();
    private GoodsSalesStatisticsFragment goodsSalesStatisticsFragment;
    private TotalSalesStatisticsFragment salesStatisticsFragment;

    @Override
    public int getContentView() {
        return R.layout.activity_sales_statistics;
    }

    @Override
    protected void initView() {
        initTabTitle(getResources().getString(R.string.sales_statistics), getResources().getString(R.string.str355));//销售统计
        initTabTitle();
        initFragment();


    }

    @OnClick({R.id.toolbar_right})
    public void setOnClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right://导出邮箱
                ARouter.getInstance().build(RouterPath.ACTIVITY_ACCOUNT_EXPORT)
                        .withInt("type", 1)
                        .navigation();
                break;
        }
    }

    private void initTabTitle() {
        //商品销售统计
        tabTitles.add(getResources().getString(R.string.goods_salesstatistics));
        //销售额统计
        tabTitles.add(getResources().getString(R.string.str405));
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        //商品销售统计
        goodsSalesStatisticsFragment = new GoodsSalesStatisticsFragment();
        salesStatisticsFragment = new TotalSalesStatisticsFragment();


        rgBtn.setOnCheckedChangeListener(this);
        rbGoodsStatistics.setChecked(true);
//        rgBtn.check(R.id.rb_goods_statistics);

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_goods_statistics:
                showFragment(R.id.frag_layout, goodsSalesStatisticsFragment);
                break;
            case R.id.rb_sales_statistics:
                showFragment(R.id.frag_layout, salesStatisticsFragment);
                break;

        }
    }


    /**
     * viewpager支持Fragment适配器
     */
    public class IndexFragmentAdapter extends FragmentPagerAdapter {

        public IndexFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);//分类标签
        }
    }

    /**
     * 显示隐藏Fragment
     */
    protected void showFragment(int resid, BaseFragment fragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //隐藏正在显示的Fragment
        if (showFragment != null) {
            fragmentTransaction.hide(showFragment);
        }
        //展示需要显示的Fragment对象
        Fragment mFragment = fragmentManager.findFragmentByTag(fragment.getClass().getName());
        if (mFragment != null) {
            fragmentTransaction.show(mFragment);
            showFragment = (BaseFragment) mFragment;
        } else {
            fragmentTransaction.add(resid, fragment, fragment.getClass().getName());
            showFragment = fragment;
        }
        fragmentTransaction.commit();
    }

}
