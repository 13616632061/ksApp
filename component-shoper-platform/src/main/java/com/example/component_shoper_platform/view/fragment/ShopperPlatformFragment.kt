package com.example.component_shoper_platform.view.fragment

import com.example.component_shoper_platform.R
import com.example.component_shoper_platform.p.ShopperPlatformFragmentPresenter
import com.library.base.mvp.BaseFragment

class ShopperPlatformFragment : BaseFragment<ShopperPlatformFragmentPresenter>() {
    override fun setContentViewId(): Int {
        return R.layout.layout_shopper_platform;
    }

    override fun loadData() {

    }

}