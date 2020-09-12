package com.api;

import com.MyApplication.KsApplication;
import com.apkfuns.logutils.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.GsonBuilder;
import com.library.app.LibAplication;
import com.ui.util.SysUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2019/5/9.
 */

public class ApiRetrofit {
    private static final String TAG = ApiRetrofit.class.getSimpleName();
    private static ApiRetrofit mApiRetrofit;
    private final Retrofit mRetrofit;
    private OkHttpClient mClient;
    private ApiService mApiService;


    //缓存配置
    private Interceptor mCacheInterceptor = chain -> {

        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        cacheBuilder.maxAge(0, TimeUnit.SECONDS);
        cacheBuilder.maxStale(365, TimeUnit.DAYS);
        CacheControl cacheControl = cacheBuilder.build();

        Request request = chain.request();
        if (!NetworkUtils.isConnected()) {
            request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        if (NetworkUtils.isConnected()) {
            int maxAge = 0; // read from cache
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public ,max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    };

    /**
     * 请求访问quest和response拦截器
     */
    private Interceptor mLogInterceptor = chain -> {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        LogUtils.e(TAG + "----------Request Start----------------");
        LogUtils.e(TAG + "| Request  " + request.toString());
        LogUtils.e(TAG + "----------Request headers----------------");
        LogUtils.e(TAG + "| " + request.headers().toString());
        LogUtils.e(TAG + "----------Request response----------------");
        LogUtils.e(TAG + "| Response:" + response);
        LogUtils.e(TAG + "----------Request End:" + duration + "毫秒----------");
        LogUtils.e(TAG + "| Response:" + content);
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    };

    /**
     * 增加头部信息的拦截器
     */
    private Interceptor mHeaderInterceptor = chain -> {
        Request.Builder builder = chain.request().newBuilder();
//        builder.addHeader("vsn", "1.0");
//        builder.addHeader("format", "json");
//        builder.addHeader("seller_token", KsApplication.getString("token", ""));
        return chain.proceed(builder.build());
    };

    /**
     * @Description:公共参数
     * @Author:lyf
     * @Date: 2020/7/11
     */
    private Interceptor mBaseParams() {
        HttpBaseParamsLoggingInterceptor baseParamsLoggingInterceptor = new HttpBaseParamsLoggingInterceptor
                .Builder()
                .addParam("vsn", "1.0")
                .addParam("format", "json")
                .addParam("seller_token", KsApplication.getString("token", ""))
                .build();
        return baseParamsLoggingInterceptor;
//
//        Request request = chain.request();
//        Request.Builder requestBuilder = request.newBuilder();
//        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
//        httpUrlBuilder.addQueryParameter("vsn", "1.0");
//        httpUrlBuilder.addQueryParameter("format", "json");
//        httpUrlBuilder.addQueryParameter("seller_token", KsApplication.getString("token", ""));
//        requestBuilder.url(httpUrlBuilder.build());
//        request = requestBuilder.build();
//        return chain.proceed(requestBuilder.build());
    }

    public ApiRetrofit() {
        //cache url
        File httpCacheDirectory = new File(LibAplication.getContext().getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        //        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT);
        //        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//请求/响应行 + 头 + 体

        mClient = new OkHttpClient.Builder()
                .addInterceptor(mHeaderInterceptor)//添加头部信息拦截器
                .addInterceptor(mLogInterceptor)//添加log拦截器
                .addInterceptor(mBaseParams())//添加公共参数拦截器
                .cache(cache)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(SysUtils.getWebUri())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//支持RxJava
                .client(mClient)
                .build();

        mApiService = mRetrofit.create(ApiService.class);
    }

    public static ApiRetrofit getInstance() {
        if (mApiRetrofit == null) {
            synchronized (Object.class) {
                if (mApiRetrofit == null) {
                    mApiRetrofit = new ApiRetrofit();
                }
            }
        }
        return mApiRetrofit;
    }

    public ApiService getApiService() {
        return mApiService;
    }
}
