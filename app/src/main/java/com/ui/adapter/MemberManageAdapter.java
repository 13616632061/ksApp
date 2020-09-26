package com.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ui.entity.Member;
import com.ui.ks.MemberManage.MemberManageActivity;
import com.ui.ks.R;
import com.ui.util.SetEditTextInput;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 首页订单列表适配器
 * Created by Administrator on 2016/12/24.
 */

public class MemberManageAdapter extends BaseQuickAdapter<Member.ResponseBean.DataBean.InfoBean, BaseViewHolder> {


    public MemberManageAdapter(int layoutResId, @Nullable List<Member.ResponseBean.DataBean.InfoBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Member.ResponseBean.DataBean.InfoBean item) {
        helper.setText(R.id.tv_number, (helper.getAdapterPosition() + 1) + "");
        helper.setText(R.id.tv_member_name, item.getMember_name());
        helper.setText(R.id.tv_member_phone, item.getMobile());
        helper.setText(R.id.tv_member_integral, item.getScore());
        helper.setText(R.id.tv_member_balance, new DecimalFormat("######0.00")
                .format(Double.parseDouble(TextUtils.isEmpty(item.getSurplus()) ? "0.00" : item.getSurplus())));
    }

}
