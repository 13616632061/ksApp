package com.ui.ks.SendEmail.model;

import com.MyApplication.KsApplication;
import com.api.ApiRetrofit;
import com.ui.ks.SendEmail.contract.SendEmailContract;

import rx.Observable;

/**
 * Created by lyf on 2020/7/12.
 */

public class SendEmailModel implements SendEmailContract.Model {
    /**
     * @Description:发送至邮箱
     * @Author:lyf
     * @Date: 2020/7/12
     */
    @Override
    public Observable sendToEmai(String email, String Batch) {
        return ApiRetrofit.getInstance().getApiService().sendEmail(email, Batch);
    }
}
