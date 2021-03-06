package com.ui.ks.accountExport.presenter;

import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.api.SubscriberCallBack;
import com.bean.RecentlyUsedEmailBean;
import com.bean.ResultResponse;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.library.base.mvp.BasePresenter;
import com.ui.entity.GoodsSalesStatisticsRespone;
import com.ui.ks.R;
import com.ui.ks.accountExport.AccountExportActivity;
import com.ui.ks.accountExport.contract.AccountExportContract;
import com.ui.ks.accountExport.model.AccountExportModel;
import com.ui.util.DateUtils;
import com.ui.util.StringUtils;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import rx.Subscriber;

/**
 * Created by lyf on 2020/8/1.
 */

public class AccountExportPresenter extends BasePresenter<AccountExportActivity> implements AccountExportContract.Presenter {


    private AccountExportModel mModel;

    public AccountExportPresenter(AccountExportActivity mView) {
        super(mView);
        mModel = new AccountExportModel();
    }


    /**
     * @Description:对账单导出
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @Override
    public void sendEmailReportAccount() {
        String starttime = mView.getStartTime();
        String endtime = mView.getEndTime();
        String e_mail = mView.getEmail();
        if (TextUtils.isEmpty(starttime)) {
            mView.showToast(mView.getResources().getString(R.string.str399));//开始时间不能为空
            return;
        }
        if (TextUtils.isEmpty(endtime)) {
            mView.showToast(mView.getResources().getString(R.string.str400));//结束时间不能为空
            return;
        }
        if (TextUtils.isEmpty(e_mail)) {
            mView.showToast(mView.getResources().getString(R.string.str401));//邮箱不能为空
            return;
        }

        mView.showLoading();
        addSubscription(mModel.sendReportAccount(starttime, endtime, e_mail), new Subscriber<ResultResponse>() {
            @Override
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultResponse response) {
                //保存最近使用过的邮箱
                RecentlyUsedEmailBean recentlyUsedEmailBean = new RecentlyUsedEmailBean();
                recentlyUsedEmailBean.setEmail(e_mail);
                recentlyUsedEmailBean.save();
                if (response != null && response.getResponse() != null
                        && "200".equals(response.getResponse().getStatus())){
                    mView.showToast(mView.getResources().getString(R.string.errcode_success));//发送邮箱成功

                }
            }
        });
    }

    /**
     * @Description:导出对账流水至本地
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @Override
    public void downLoadExcel() {
        String starttime = mView.getStartTime();
        String endtime = mView.getEndTime();
        if (TextUtils.isEmpty(starttime)) {
            mView.showToast(mView.getResources().getString(R.string.str399));//开始时间不能为空
            return;
        }
        if (TextUtils.isEmpty(endtime)) {
            mView.showToast(mView.getResources().getString(R.string.str400));//结束时间不能为空
            return;
        }
        mView.showLoading();
        addSubscription(mModel.downLoadExcel(starttime, endtime), new SubscriberCallBack<ResultResponse>() {

            @Override
            protected void onSuccess(ResultResponse response) {

            }

            @Override
            protected void onSuccess(String response) {
                if (!TextUtils.isEmpty(response)) {
                    saveExcelFile(response);
                }
            }

            @Override
            protected void onError() {
                mView.hideLoading();
            }

        });
    }

    /**
     * @Description:保存excel文件
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @Override
    public void saveExcelFile(String url) {

        addSubscription(mModel.saveExcelFile(url), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e("saveExcelFile: " + e.toString());
            }

            @Override
            public void onNext(Boolean bool) {
                LogUtils.i("saveExcelFile: " + bool);
                if (bool) {
                    mView.showToast(mView.getResources().getString(R.string.str402));//对账单已成功导入至本地
                }

            }
        });

    }
    /**
    *@Description:商品销售数据导出至邮箱
    *@Author:lyf
    *@Date: 2020/9/26
    */
    @Override
    public void goodSalesDatasendEmail() {
        mView.showLoading();
        String starttime = mView.getStartTime();
        String endtime = mView.getEndTime();
        String e_mail = mView.getEmail();
        if (TextUtils.isEmpty(starttime)) {
            mView.showToast(mView.getResources().getString(R.string.str399));//开始时间不能为空
            return;
        }
        if (TextUtils.isEmpty(endtime)) {
            mView.showToast(mView.getResources().getString(R.string.str400));//结束时间不能为空
            return;
        }
        if (TextUtils.isEmpty(e_mail)) {
            mView.showToast(mView.getResources().getString(R.string.str401));//邮箱不能为空
            return;
        }
        addSubscription(mModel.goodSalesDatasendEmail(starttime,endtime,e_mail), new Subscriber<ResultResponse>() {
            @Override
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoading();

            }

            @Override
            public void onNext(ResultResponse response) {
                mView.hideLoading();
                //保存最近使用过的邮箱
                RecentlyUsedEmailBean recentlyUsedEmailBean = new RecentlyUsedEmailBean();
                recentlyUsedEmailBean.setEmail(e_mail);
                recentlyUsedEmailBean.save();
                if (response != null && response.getResponse() != null
                        && "200".equals(response.getResponse().getStatus())){
                    mView.showToast(mView.getResources().getString(R.string.errcode_success));//发送邮箱成功

                }
            }


        });
    }


}
