package com.ui.ks.MemberManage.presenter;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.library.base.mvp.BasePresenter;
import com.ui.adapter.MemberManageAdapter;
import com.ui.entity.Member;
import com.ui.ks.MemberManage.MemberSearchActivity;
import com.ui.ks.MemberManage.contract.MemberSearchContract;
import com.ui.ks.MemberManage.model.MemberSearchModel;
import com.ui.ks.R;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by lyf on 2020/9/20.
 */

public class MemberSearchPresenter extends BasePresenter<MemberSearchActivity> implements MemberSearchContract.Presenter {

    private static final String TAG = MemberSearchPresenter.class.getSimpleName();
    private MemberSearchModel mModel = new MemberSearchModel();
    private int type = 0;//默认会员名

    private List<Member.ResponseBean.DataBean.InfoBean> mData = new ArrayList<>();
    MemberManageAdapter mAdapter;

    public MemberSearchPresenter(MemberSearchActivity mView) {
        super(mView);
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Member.ResponseBean.DataBean.InfoBean> getData() {
        return mData;
    }

    /**
     * @Description:初始化adapter
     * @Author:lyf
     * @Date: 2020/9/20
     */
    @Override
    public void initAdapter() {
        mAdapter = mView.initAdapter();
    }

    /**
     * @Description:会员搜索
     * @Author:lyf
     * @Date: 2020/9/20
     */
    @Override
    public void memberSearch() {
        if (TextUtils.isEmpty(mView.getSearchContent())) {
            mView.showToast(mView.getResources().getString(R.string.str430));
            return;
        }
        mView.showLoading();
        addSubscription(mModel.memberSearch(type, mView.getSearchContent()), new Subscriber<Member>() {
            @Override
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoading();
                LogUtils.e(TAG + " onError " + e.toString());
            }

            @Override
            public void onNext(Member response) {
                mView.hideLoading();
                if (response != null && response.getResponse() != null
                        && response.getResponse().getData() != null
                        && response.getResponse().getData().getInfo() != null) {
                    mData.clear();
                    mData.addAll(response.getResponse().getData().getInfo());
                    mAdapter.notifyDataSetChanged();
                    LogUtils.i(TAG + " mData " + mData.size());
                }
            }
        });
    }
}
