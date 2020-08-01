package com.ui.ks;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.base.BaseActivity;
import com.ui.adapter.MainBranchPagerAdapter;
import com.ui.fragment.BaseFragmentMainBranch;
import com.ui.fragment.BranchFragment;
import com.ui.util.SysUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 总店分店营业统计页面
 */
public class MainBranchSelectActivity extends BaseActivity {
    private RadioGroup mRadioGroup;
    private List<BaseFragmentMainBranch> mBaseFragment;
    private ImageView iv_goodmanage_search;
    private View btn_main_line;
    private View btn_branch_line;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_branch_select);
        SysUtils.setupUI(MainBranchSelectActivity.this, findViewById(R.id.activity_main_branch_select));
        initToolbar(this);
        initFragment();
        initView();
        initListener();

    }
    /**
     * 滑动radiobutton 的改变
     * @author Administrator
     *
     */
    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mRadioGroup.check(R.id.btn_good);
                    break;
                case 1:
                    mRadioGroup.check(R.id.btn_sort);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }
    private void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_good:
                        mViewPager.setCurrentItem(0);
                        btn_branch_line.setVisibility(View.INVISIBLE);
                        btn_main_line.setVisibility(View.VISIBLE);
                        break;
                    case R.id.btn_sort:
                        mViewPager.setCurrentItem(1);
                        btn_main_line.setVisibility(View.INVISIBLE);
                        btn_branch_line.setVisibility(View.VISIBLE);
                        break;

                    default:
                        mViewPager.setCurrentItem(0);
                        btn_branch_line.setVisibility(View.INVISIBLE);
                        btn_main_line.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        mRadioGroup.check(R.id.btn_good);
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new MainFrament());
        mBaseFragment.add(new BranchFragment());

    }

    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_btn);
        btn_main_line = findViewById(R.id.btn_main_line);
        btn_branch_line = findViewById(R.id.btn_branch_line);
        mViewPager = (ViewPager) findViewById(R.id.fragment_content);

        mViewPager.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setAdapter(new MainBranchPagerAdapter(getSupportFragmentManager(),mBaseFragment));
    }
}
