package com.ui.ks.accountExport.model;

import android.os.Environment;
import android.os.Looper;

import com.api.ApiRetrofit;
import com.blankj.utilcode.util.LogUtils;
import com.ui.ks.accountExport.contract.AccountExportContract;
import com.ui.util.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by lyf on 2020/8/1.
 */

public class AccountExportModel implements AccountExportContract.Model {

    /**
     * @Description:对账单导出
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @Override
    public Observable sendReportAccount(String begintime, String endtime, String Email) {
        return ApiRetrofit.getInstance().getApiService().sendReportAccount(begintime, endtime, Email);
    }

    /**
     * @Description:导出对账流水至本地
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @Override
    public Observable downLoadExcel(String begintime, String endtime) {
        return ApiRetrofit.getInstance().getApiService().downLoadExcel(begintime, endtime);
    }

    /**
     * @Description:描述
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @Override
    public Observable saveExcelFile(String downloadUrl) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    URL url = new URL(downloadUrl);
                    //打开连接
                    URLConnection conn = url.openConnection();
                    //打开输入流
                    InputStream is = conn.getInputStream();
                    //获得长度
                    int contentLength = conn.getContentLength();
                    //创建文件夹 MyDownLoad，在存储卡下

                    String dirName = Environment.getExternalStorageDirectory() + "/11yzx-Excel/";
                    File file = new File(dirName);
                    //不存在创建
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    //下载后的文件名
                    String fileName = dirName + DateUtils.getCurDate() + ".xlsx";
                    File file1 = new File(fileName);
                    if (file1.exists()) {
                        file1.delete();
                    }
                    //创建字节流
                    byte[] bs = new byte[1024];
                    int len;
                    OutputStream os = new FileOutputStream(fileName);
                    //写数据
                    while ((len = is.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }
                    subscriber.onNext(true);
                    subscriber.onCompleted();

                    Looper.prepare();
                    Looper.loop();
                    //完成后关闭流
                    os.close();
                    is.close();
                } catch (Exception e) {
                    LogUtils.e("e=" + e.toString());
                    subscriber.onError(e);
                    subscriber.onCompleted();
                }
            }
        });
    }
}
