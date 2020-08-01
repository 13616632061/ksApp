package com.library.base.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethanhua.skeleton.Skeleton;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/4/24.
 */

public abstract class BaseFragment<T extends BasePresenter> extends LazyLoadFragment {

    protected T mPresenter;
    private View rootView;
    protected Skeleton mStateView;//用于显示加载中、网络异常，空布局、内容布局
    protected Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(setContentViewId(), container, false);
//            mStateView=Skeleton.bind(rootView)
//                    .load(R.layout.layout_img_skeleton)
//                    .show();
            ButterKnife.bind(this, rootView);

            initView(rootView);
            initData();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    /**
     * 当第一次可见的时候，加载数据
     */
    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        loadData();
    }

    /**
     * 得到当前界面的布局文件id(由子类实现)
     *
     * @return
     */
    protected abstract int setContentViewId();

    /**
     * 初始化一些view
     */
    public void initView(View rootView) {
    }

    /**
     * 初始化数据
     */
    public void initData() {
    }

    /**
     * 加载数据
     */
    protected abstract void loadData();


    @Override
    public void onDestroy() {
        super.onDestroy();
        rootView = null;
    }
}
