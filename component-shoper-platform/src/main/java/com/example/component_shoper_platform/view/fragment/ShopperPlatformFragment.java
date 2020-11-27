package com.example.component_shoper_platform.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.component_shoper_platform.R;
import com.example.interface_shoper_platform.constant.ArouteContantPath;
import com.library.base.mvp.BaseFragment;

@Route(path = ArouteContantPath.SHOPPER_PLATFORM_FRAGMENT_PATH)
public class ShopperPlatformFragment extends BaseFragment {


    @Override
    protected int setContentViewId() {
        return R.layout.layout_shopper_platform;
    }

    @Override
    protected void loadData() {

    }
}
