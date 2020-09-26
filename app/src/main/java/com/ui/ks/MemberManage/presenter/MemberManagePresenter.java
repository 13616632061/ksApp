package com.ui.ks.MemberManage.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.library.base.mvp.BasePresenter;
import com.ui.adapter.MemberManageAdapter;
import com.ui.entity.Member;
import com.ui.ks.MemberManage.MemberManageActivity;
import com.ui.ks.MemberManage.contract.MemberManageContract;
import com.ui.ks.MemberManage.model.MemberManageModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by lyf on 2020/9/19.
 */

public class MemberManagePresenter extends BasePresenter<MemberManageActivity> implements MemberManageContract.Presenter {
    private static final String TAG = MemberManagePresenter.class.getSimpleName();
    private MemberManageModel mModel = new MemberManageModel();
    private int page = 1;
    private List<Member.ResponseBean.DataBean.InfoBean> mData = new ArrayList<>();
    MemberManageAdapter mAdapter;

    public MemberManagePresenter(MemberManageActivity mView) {
        super(mView);
    }

    public List<Member.ResponseBean.DataBean.InfoBean> getData() {
        return mData;
    }

    /**
     * @Description:初始化adapter
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public void initAdapter() {
        mAdapter = mView.initAdapter();
    }

    /**
     * @Description:查询会员列表
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public void queryMemberList() {
        if (page == 1) {
            mView.showLoading();
        }
        addSubscription(mModel.queryMemberList(page + ""), new Subscriber<Member>() {
            @Override
            public void onCompleted() {
                mView.hideLoading();
                mView.setRefreshing(false);

            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoading();
                mView.setRefreshing(false);
                LogUtils.e(TAG + " onError " + e.toString());

            }

            @Override
            public void onNext(Member response) {
                mView.hideLoading();
                mView.setRefreshing(false);
                if (response != null && response.getResponse() != null
                        && response.getResponse().getData() != null
                        && response.getResponse().getData().getInfo() != null) {
                    clearData();
                    mData.addAll(response.getResponse().getData().getInfo());
                    mAdapter.notifyDataSetChanged();
                    if (page < mView.totalPage(response.getResponse().getData().getNums())) {
                        mAdapter.loadMoreComplete();
                    } else {
                        mAdapter.loadMoreEnd();
                    }
                    LogUtils.i(TAG + " mData " + mData.size());
                }
            }
        });
    }

    /**
     * @Description:上拉加载
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public void loadMore() {
        page++;
        queryMemberList();
    }

    /**
     * @Description:清空数据
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public void clearData() {
        if (page == 1) {
            mData.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @Description:下拉刷新
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public void onRefresh() {
        page = 1;
        mView.setRefreshing(true);
        queryMemberList();
    }
}
