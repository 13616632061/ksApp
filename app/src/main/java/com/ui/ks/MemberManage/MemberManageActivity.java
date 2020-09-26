package com.ui.ks.MemberManage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.convenientbanner.utils.ScreenUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.constant.RouterPath;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.library.base.mvp.BaseActivity;
import com.library.weight.DividerDecoration;
import com.ui.adapter.MemberManageAdapter;
import com.ui.entity.Member;
import com.ui.ks.MemberDetailsActivity;
import com.ui.ks.MemberManage.contract.MemberManageContract;
import com.ui.ks.MemberManage.presenter.MemberManagePresenter;
import com.ui.ks.R;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.DialogUtils;
import com.ui.util.SysUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description:会员管理
 * @Author:lyf
 * @Date: 2020/9/19
 */
@Route(path = RouterPath.ACTIVITY_MEMBER_MANAGE)
public class MemberManageActivity extends BaseActivity implements MemberManageContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    private static final String TAG = MemberManageActivity.class.getSimpleName();

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    private MemberManagePresenter mPresenter;


    @Override
    public int getContentView() {
        return R.layout.activity_member_manage;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        initTabTitle(getResources().getString(R.string.str178), getResources().getString(R.string.str383));//会员管理
        mPresenter = new MemberManagePresenter(this);
        mPresenter.initAdapter();
        mPresenter.queryMemberList();
        refresh();
    }

    @OnClick({R.id.btn_add, R.id.toolbar_right})
    public void setOnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add://添加
                toGoAddMemberPage();
                break;
            case R.id.toolbar_right://搜索
                toGoMemberSearchPage();
                break;
        }
    }


    /**
     * @Description:初始化adapter
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public MemberManageAdapter initAdapter() {
        MemberManageAdapter adapter = new MemberManageAdapter(R.layout.itme_member_manage, mPresenter.getData());
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new DividerDecoration(this, LinearLayoutManager.VERTICAL, getResources().getColor(R.color.gray_bg), ScreenUtil.dip2px(this, 1), 0, 0));
        adapter.setOnLoadMoreListener(this, list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toGoMemberEdidPage(mPresenter.getData().get(position));
            }
        });
        return adapter;
    }

    /**
     * @Description:下拉刷新
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public void refresh() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.onRefresh();
            }
        });
    }

    /**
     * @Description:刷新状态
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public void setRefreshing(boolean refreshing) {
        refresh.setRefreshing(refreshing);
    }

    /**
     * @Description:跳转会员编辑
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public void toGoMemberEdidPage(Member.ResponseBean.DataBean.InfoBean memberBean) {
        ARouter.getInstance().build(RouterPath.ACTIVITY_ADD_MEMBER)
                .withParcelable("memberBean",memberBean)
                .navigation();
    }

    /**
     * @Description:跳转添加会员页面
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public void toGoAddMemberPage() {
        ARouter.getInstance().build(RouterPath.ACTIVITY_ADD_MEMBER).navigation();
    }

    /**
     * @Description:跳转会员搜索页面
     * @Author:lyf
     * @Date: 2020/9/20
     */
    @Override
    public void toGoMemberSearchPage() {
        ARouter.getInstance().build(RouterPath.ACTIVITY_SEARCH_MEMBER).navigation();
    }

    /**
     * @Description:上拉加载
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadMore();
    }


    /**
     * @Description:EventBus事件处理
     * @Author:lyf
     * @Date: 2020/9/20
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(Map<String, String> map) {
        if (map.containsKey("MemberListRefresh")) {
            mPresenter.onRefresh();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
