package com.ui.ks.MemberManage.presenter;

import android.text.TextUtils;

import com.bean.ResultResponse;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.library.base.mvp.BasePresenter;
import com.library.utils.MessageEventUtil;
import com.ui.entity.AddMemberInfoRequst;
import com.ui.ks.MemberManage.AddEditMemberInfoActivity;
import com.ui.ks.MemberManage.contract.AddEditMemberInfoContract;
import com.ui.ks.MemberManage.model.AddEditMemberInfoModel;
import com.ui.ks.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by lyf on 2020/9/19.
 */

public class AddEditMemberInfoPresenter extends BasePresenter<AddEditMemberInfoActivity> implements AddEditMemberInfoContract.Presenter {
    private static final String TAG = AddEditMemberInfoPresenter.class.getSimpleName();

    private AddEditMemberInfoModel mModel = new AddEditMemberInfoModel();

    public AddEditMemberInfoPresenter(AddEditMemberInfoActivity mView) {
        super(mView);
    }

    /**
     * @Description:添加会员
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public void addMemberInfo() {
        if (TextUtils.isEmpty(mView.getMemberName())) {
            mView.showToast(mView.getResources().getString(R.string.str181));
            return;
        }
        if (TextUtils.isEmpty(mView.getMemberPhone())) {
            mView.showToast(mView.getResources().getString(R.string.str116));
            return;
        }
        mView.showLoading();
        addSubscription(mModel.addMemberInfo(getAddMemberInfoRequst()), new Subscriber<ResultResponse>() {
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
            public void onNext(ResultResponse response) {
                mView.hideLoading();
                EventBus.getDefault().post(MessageEventUtil.getStringMap("MemberListRefresh", ""));
                mView.finish();
            }

        });

    }

    /**
     * @Description:编辑会员
     * @Author:lyf
     * @Date: 2020/9/22
     */
    @Override
    public void editMemberInfo() {
        if (TextUtils.isEmpty(mView.getMemberName())) {
            mView.showToast(mView.getResources().getString(R.string.str181));
            return;
        }
        if (TextUtils.isEmpty(mView.getMemberPhone())) {
            mView.showToast(mView.getResources().getString(R.string.str116));
            return;
        }
        mView.showLoading();
        addSubscription(mModel.editMemberInfo(getEditMemberInfoRequst()), new Subscriber<ResultResponse>() {
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
            public void onNext(ResultResponse response) {
                mView.hideLoading();
                if (response.getResponse() != null
                        && !TextUtils.isEmpty(response.getResponse().getStatus())) {
                    EventBus.getDefault().post(MessageEventUtil.getStringMap("MemberListRefresh", ""));
                }
                mView.finish();
            }

        });
    }

    /**
     * @Description:添加会员请求参数
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public String getAddMemberInfoRequst() {
        String score = TextUtils.isEmpty(mView.getMemberIntegral()) ? "0" : mView.getMemberIntegral();
        String surplus = TextUtils.isEmpty(mView.getMemberBalance()) ? "0" : mView.getMemberBalance();
        String discountRate = TextUtils.isEmpty(mView.getDiscountRate()) ? "1" : mView.getDiscountRate();

        List<AddMemberInfoRequst> list = new ArrayList<>();
        AddMemberInfoRequst requst = new AddMemberInfoRequst();
        requst.setMember_id(mView.getMemberId());
        requst.setMember_name(mView.getMemberName());
        requst.setMobile(mView.getMemberPhone());
        requst.setScore(score);
        requst.setSurplus(surplus);
        requst.setDiscount_rate(discountRate);
        requst.setBirthday(mView.getBirthday());
        requst.setIs_require_pass("no");
        requst.setMember_lv_custom_id("");
        requst.setMember_lv_custom_key("");
        requst.setRemark("");
        requst.setFanxian(TextUtils.isEmpty(mView.getCashBack()) ? "0" : mView.getDiscountRate());
        list.add(requst);
        String requstStr = new Gson().toJson(list);
        return requstStr;
    }
    /**
    *@Description:编辑会员请求参数
    *@Author:lyf
    *@Date: 2020/9/22
    */
    @Override
    public String getEditMemberInfoRequst() {
        String score = TextUtils.isEmpty(mView.getMemberIntegral()) ? "0" : mView.getMemberIntegral();
        String surplus = TextUtils.isEmpty(mView.getMemberBalance()) ? "0" : mView.getMemberBalance();
        String discountRate = TextUtils.isEmpty(mView.getDiscountRate()) ? "1" : mView.getDiscountRate();

        AddMemberInfoRequst requst = new AddMemberInfoRequst();
        requst.setMember_id(mView.getMemberId());
        requst.setMember_name(mView.getMemberName());
        requst.setMobile(mView.getMemberPhone());
        requst.setScore(score);
        requst.setSurplus(surplus);
        requst.setDiscount_rate(discountRate);
        requst.setBirthday(mView.getBirthday());
        requst.setIs_require_pass("no");
        requst.setMember_lv_custom_id("0");
        requst.setMember_lv_custom_key("0");
        requst.setRemark("");
        requst.setFanxian(TextUtils.isEmpty(mView.getCashBack()) ? "0" : mView.getDiscountRate());
        String requstStr = new Gson().toJson(requst);
        return requstStr;
    }
}
