package com.ui.ks.accountExport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bean.RecentlyUsedEmailBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.constant.RouterPath;
import com.library.base.mvp.BaseActivity;
import com.ui.ks.R;
import com.ui.ks.accountExport.adapter.RecentlyUsedEmailAdapter;

import org.litepal.LitePal;
import org.litepal.parser.LitePalAttr;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description最近使用过的邮箱
 * @Author:lyf
 * @Date: 2020/8/1
 */
@Route(path = RouterPath.ACTIVITY_RECENTLY_USED_EMAIL)
public class RecentlyUsedEmailActivity extends BaseActivity {

    private static final int RESULT_CODE = 200;
    @BindView(R.id.list)
    RecyclerView list;
    private RecentlyUsedEmailAdapter adapter;
    private List<RecentlyUsedEmailBean> data = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_recently_used_email;
    }

    @Override
    protected void initView() {
        initTabTitle(getResources().getString(R.string.str403), "");//最近使用过的邮箱

        data = LitePal.findAll(RecentlyUsedEmailBean.class);
        adapter = new RecentlyUsedEmailAdapter(R.layout.item_recently_used_email, data);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = getIntent();
                intent.putExtra("email", data.get(position).getEmail());
                setResult(RESULT_CODE, intent);
                finish();
            }
        });

    }


}
