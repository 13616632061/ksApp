package com.ui.ks.InventoryRecord;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bigkoo.convenientbanner.utils.ScreenUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.constant.RouterPath;
import com.library.base.mvp.BaseActivity;
import com.library.weight.DividerDecoration;
import com.ui.ks.InventoryRecord.adapter.InventoryRecordAdapter;
import com.ui.ks.InventoryRecord.contract.InventoryRecordContract;
import com.ui.ks.InventoryRecord.prestener.InventoryRecordPrestener;
import com.ui.ks.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description:盘点记录
 * @Author:lyf
 * @Date: 2020/7/8
 */
@Route(path = RouterPath.ACTIVITY_INVENTORY_RECORD)
public class InventoryRecordActivity extends BaseActivity<InventoryRecordPrestener> implements InventoryRecordContract.View {


    @BindView(R.id.list)
    RecyclerView list;

    private InventoryRecordPrestener mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_inventory_record;
    }

    @Override
    protected void initView() {
        initTabTitle(getResources().getString(R.string.str374), "");//盘点记录
        mPresenter = new InventoryRecordPrestener(this);
        mPresenter.initAdapter();
        mPresenter.getInventoryRecord();
    }

    @Override
    public InventoryRecordAdapter initAdapter() {
        InventoryRecordAdapter adapter = new InventoryRecordAdapter(R.layout.item_inventory_record, mPresenter.getmData());
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new DividerDecoration(this, LinearLayoutManager.VERTICAL, getResources().getColor(R.color.gray_bg), ScreenUtil.dip2px(this, 5), 0, 0));
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_send_email://发送邮箱
                        toGoSendEmail(mPresenter.getmData().get(position).getBatch());
                        break;
                }
            }
        });
        return adapter;
    }

    /**
     * 发送邮箱
     *
     * @param Batch 盘点记录id
     */
    @Override
    public void toGoSendEmail(String Batch) {
        ARouter.getInstance().build(RouterPath.ACTIVITY_SEND_EMAIL)
                .withString("Batch", Batch)
                .navigation();
    }


}
