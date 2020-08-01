package com.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.ui.adapter.MainBranchPagerAdapter;
import com.ui.ks.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ShoppingMallOrderPageFragment extends BaseFragmentMainBranch {
    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private ImageView iv_back;
    private List<BaseFragmentMainBranch> mBaseFragment;
    private int position;
    private View view;


    @Override
    protected View initView() {

        view = View.inflate(mContext, R.layout.shoppingmallorderpagefragment, null);
        initFragment();
        initview(view);
        initListener();
        Bundle mBundle=getArguments();
        if(mBundle!=null){
            position=mBundle.getInt("position",0);
            if(position==0){
                mRadioGroup.check(R.id.btn_all);
            }else if(position==1){
                mRadioGroup.check(R.id.btn_waitpay);
            }else if(position==2){
                mRadioGroup.check(R.id.btn_waitsenndgoods);
            }else if(position==3){
                mRadioGroup.check(R.id.btn_waitgetgoods);
            }
        }
        return view;
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new AllSupplyOrderFragment());
        mBaseFragment.add(new WaitPaySupplyOderFragment());
        mBaseFragment.add(new WaitSendGoodsSupplyOrderFragment());
        mBaseFragment.add( new WaitGetGoodsSupplyOrderFragment());
        mBaseFragment.add(new AccomplishSupplyOrderFragment());
    }

    private void initview(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.fragment_content);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.mRadioGroup);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);

        mViewPager.setAdapter(new MainBranchPagerAdapter(getFragmentManager(),mBaseFragment));
        mViewPager.setOffscreenPageLimit(5);


    }
    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        /**
         * RadioGroup的选择监听
         */
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_all:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.btn_waitpay:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.btn_waitsenndgoods:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.btn_waitgetgoods:
                        mViewPager.setCurrentItem(3);
                        break;
                    case R.id.btn_accomplish:
                        mViewPager.setCurrentItem(4);
                        break;

                    default:
                        mViewPager.setCurrentItem(0);
                        break;
                }
            }
        });
        mRadioGroup.check(R.id.btn_all);
        //viewpager的左右滑动监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mRadioGroup.check(R.id.btn_all);
                        break;
                    case 1:
                        mRadioGroup.check(R.id.btn_waitpay);
                        break;
                    case 2:
                        mRadioGroup.check(R.id.btn_waitsenndgoods);
                        break;
                    case 3:
                        mRadioGroup.check(R.id.btn_waitgetgoods);
                        break;
                    case 4:
                        mRadioGroup.check(R.id.btn_accomplish );
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
