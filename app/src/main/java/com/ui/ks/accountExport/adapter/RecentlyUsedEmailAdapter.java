package com.ui.ks.accountExport.adapter;

import android.support.annotation.Nullable;

import com.bean.RecentlyUsedEmailBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ui.ks.R;

import java.util.List;

/**
 * Created by lyf on 2020/8/1.
 */

public class RecentlyUsedEmailAdapter extends BaseQuickAdapter<RecentlyUsedEmailBean, BaseViewHolder> {

    public RecentlyUsedEmailAdapter(int layoutResId, @Nullable List<RecentlyUsedEmailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecentlyUsedEmailBean item) {
        //序号
        helper.setText(R.id.tv_num, (helper.getAdapterPosition() + 1) + "");
        //邮箱
        helper.setText(R.id.tv_email, item.getEmail());
    }
}
