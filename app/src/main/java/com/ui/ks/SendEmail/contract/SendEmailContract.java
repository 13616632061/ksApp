package com.ui.ks.SendEmail.contract;

import rx.Observable;

/**
 * Created by lyf on 2020/7/12.
 */

public interface SendEmailContract {

    interface View {
        //获取邮箱地址
        String getEmai();
    }

    interface Presenter {
        //发送至邮箱
        void sendToEmai(String Batch);
    }

    interface Model {
        //发送至邮箱
        Observable sendToEmai(String email, String Batch);
    }
}
