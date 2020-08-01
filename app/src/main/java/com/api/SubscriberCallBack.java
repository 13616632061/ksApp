package com.api;

import com.apkfuns.logutils.LogUtils;
import com.bean.ResultResponse;
import com.blankj.utilcode.util.ToastUtils;
import com.library.utils.CustomToastUtil;

import rx.Subscriber;

/**
 * Created by Administrator on 2019/5/13.
 */

public abstract class SubscriberCallBack<T> extends Subscriber<ResultResponse> {
    private static final String TAG = SubscriberCallBack.class.getSimpleName();

    @Override
    public void onNext(ResultResponse response) {
        if (response == null || response.getResponse() == null || response.getResponse().getStatus() == null) {
            return;
        }
        switch (response.getResponse().getStatus()) {
            case "200"://请求成功
                if (response.getResponse().getData() instanceof String) {
                    onSuccess(response.getResponse().getData().toString());
                } else {
                    onSuccess((T) response.getResponse().getData());
                }
                break;
            default://其他错误
                CustomToastUtil.showShort(response.getResponse().getMessage());
                onFailure(response);
                break;
        }

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        LogUtils.e(e);
        onError();
    }

    protected abstract void onSuccess(T response);

    protected abstract void onSuccess(String response);

    protected abstract void onError();

    protected void onFailure(ResultResponse response) {
    }

}
