package com.ui.ks.SendEmail.presenter;

import android.text.TextUtils;
import android.widget.Toast;

import com.api.SubscriberCallBack;
import com.bean.ResultResponse;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.library.base.mvp.BasePresenter;
import com.library.utils.CustomToastUtil;
import com.ui.ks.R;
import com.ui.ks.SendEmail.SendEmailActivity;
import com.ui.ks.SendEmail.contract.SendEmailContract;
import com.ui.ks.SendEmail.model.SendEmailModel;

import rx.Subscriber;

/**
 * Created by lyf on 2020/7/12.
 */

public class SendEmailPresenter extends BasePresenter<SendEmailActivity> implements SendEmailContract.Presenter {

    private SendEmailModel mModel;

    public SendEmailPresenter(SendEmailActivity mView) {
        super(mView);
        mModel = new SendEmailModel();
    }

    /**
     * @Description:发送至邮箱
     * @Author:lyf
     * @Date: 2020/7/12
     */
    @Override
    public void sendToEmai(String Batch) {
        String email = mView.getEmai();
        //邮箱地址为空
        if (TextUtils.isEmpty(email)) {
            CustomToastUtil.showShort(mView.getString(R.string.str377));
            return;
        }
        //是否正确的游戏地址
        if (!RegexUtils.isEmail(email)) {
            CustomToastUtil.showShort(mView.getString(R.string.str379));
            return;
        }
        mView.showLoading();
        addSubscription(mModel.sendToEmai(email, Batch), new Subscriber<ResultResponse>() {

            @Override
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoading();
            }

            @Override
            public void onNext(ResultResponse responseBean) {
                CustomToastUtil.showShort(mView.getString(R.string.errcode_success));
                mView.finish();
            }

//            @Override
//            protected void onSuccess(ResultResponse.ResponseBean response) {
//                mView.hideLoading();
//                CustomToastUtil.showShort(mView.getString(R.string.errcode_success));
//                mView.finish();
//            }
//
//            @Override
//            protected void onError() {
//                mView.hideLoading();
//            }


        });
    }
}
