package com.library.api;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.library.BuildConfig;
import com.library.api.cookie.CookieJarImpl;
import com.library.api.cookie.PersistentCookieStore;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {
    private static final String TAG = HttpManager.class.getCanonicalName();

    //超时时间
    private static final int DEFAULT_TIMEOUT = 20;
    //缓存大小100M
    private static final int CACHE_SIZE = 100 * 1024 * 1024;

    private static volatile HttpManager mInstance;

    private Cache mCache = null;
    private File mHttpCacheDirectory;
    private OkHttpClient mDefaultOkHttpClient;
    private Retrofit mDefaultRetrofit;
    private PersistentCookieStore mPersistentCookieStore;
    private static IHttpErrorHandle mHttpErrorHandle;

    private HttpManager() {

    }

    public static HttpManager getInstance() {
        if (mInstance == null) {
            synchronized (HttpManager.class) {
                if (mInstance == null) {
                    mInstance = new HttpManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context, String baseUrl) {
        if (mHttpCacheDirectory == null) {
            mHttpCacheDirectory = new File(context.getCacheDir(), "sf_saas_cache");
        }

        if (mPersistentCookieStore == null) {
            mPersistentCookieStore = new PersistentCookieStore(context);
        }

        if (TextUtils.isEmpty(baseUrl) || baseUrl.equals("null")) {
            baseUrl = getBaseUrl();
        }

        try {
            if (mCache == null) {
                mCache = new Cache(mHttpCacheDirectory, CACHE_SIZE);
            }
        } catch (Exception e) {
            LogUtils.wTag(TAG, "Could not create http cache", e);
        }
        SSLUtils.SSLParams sslParams = SSLUtils.getSslSocketFactory();

        if (mDefaultOkHttpClient == null) {
            mDefaultOkHttpClient = new OkHttpClient.Builder()
                    .cookieJar(new CookieJarImpl(mPersistentCookieStore))
                    .cache(mCache)
                    .sslSocketFactory(sslParams.mSSLSocketFactory, sslParams.mTrustManager)
                    .retryOnConnectionFailure(true)//失败重连
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                    // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                    .build();
        }

        if (BuildConfig.DEBUG) {
            mDefaultOkHttpClient = mDefaultOkHttpClient.newBuilder()
                    .hostnameVerifier(SSLUtils.UnSafeHostnameVerifier)
                    .build();
        }

        if (mDefaultRetrofit == null) {
            mDefaultRetrofit = new Retrofit.Builder()
                    .client(mDefaultOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(baseUrl)
                    .build();
        }
    }

    public void setBaseUrl(String baseUrl) {
        if (mDefaultRetrofit != null) {
            mDefaultRetrofit = mDefaultRetrofit.newBuilder().baseUrl(baseUrl).build();
        }
    }

    public String getBaseUrl() {
        return mDefaultRetrofit.baseUrl().url().getPath();
    }

    public int getBaseUrlPathSize() {
        return mDefaultRetrofit.baseUrl().pathSize();
    }

    public void addCookie(URI url, Cookie cookie) {
        HttpUrl httpUrl = HttpUrl.get(url);
        if (httpUrl != null) {
            List<Cookie> cookieList = new ArrayList<>();
            cookieList.add(cookie);
            mDefaultOkHttpClient.cookieJar().saveFromResponse(httpUrl, cookieList);
        } else {
            LogUtils.wTag(TAG, "添加cookie失败，httpUrl = null");
        }
    }

    public List<Cookie> getCookie(HttpUrl url) {
        return mDefaultOkHttpClient.cookieJar().loadForRequest(url);
    }

    public void clearCookie(HttpUrl url) {
        mDefaultOkHttpClient.cookieJar().loadForRequest(url).clear();
    }

    public void clearAllCookie() {
        if (mPersistentCookieStore != null) {
            mPersistentCookieStore.removeAllCookie();
        }
    }

    public <T> T createService(final Class<T> service) {
        if (service == null) {
            throw new IllegalArgumentException("Api service is null");
        }
        return mDefaultRetrofit.create(service);
    }

    public void setHttpErrorHandle(IHttpErrorHandle errorHandle) {
        mHttpErrorHandle = errorHandle;
    }

    /**
     * 添加拦截器，最终会添加到Okhttp里，注意该方法与  {@link HttpManager#addNetworkInterceptor(Interceptor interceptor)}的区别.
     *
     * @param interceptor
     */
    public void addInterceptor(Interceptor interceptor) {
        mDefaultOkHttpClient = mDefaultOkHttpClient.newBuilder().addInterceptor(interceptor).build();
        mDefaultRetrofit = mDefaultRetrofit.newBuilder().client(mDefaultOkHttpClient).build();
    }

    public void addNetworkInterceptor(Interceptor interceptor) {
        mDefaultOkHttpClient = mDefaultOkHttpClient.newBuilder().addNetworkInterceptor(interceptor).build();
        mDefaultRetrofit = mDefaultRetrofit.newBuilder().client(mDefaultOkHttpClient).build();
    }

    public List<Interceptor> getAllInterceptor() {
        if (mDefaultOkHttpClient != null) {
            return mDefaultOkHttpClient.newBuilder().interceptors();
        }
        return null;
    }

    public List<Interceptor> getAllNetworkInterceptor() {
        if (mDefaultOkHttpClient != null) {
            return mDefaultOkHttpClient.newBuilder().networkInterceptors();
        }
        return null;
    }

    /**
     * 线程调度器
     *
     * @return
     */
    private static ObservableTransformer schedulersTransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {

                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private static ObservableTransformer exceptionTransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable observable) {
                return observable.onErrorResumeNext(new Function<Throwable, ObservableSource>() {
                    @Override
                    public ObservableSource apply(Throwable throwable) throws Exception {
                        if (mHttpErrorHandle != null) {
                            return Observable.error(mHttpErrorHandle.handleException(throwable));
                        } else {
                            return Observable.error(new Throwable("mHttpErrorHandle = null"));
                        }
                    }
                });
            }
        };
    }

    public <T> void requestSubscribe(Observable observable, final IHttpCallback<T> callback) {
        observable.compose(schedulersTransformer())
                .compose(exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        callback.onStart();
                    }
                })
                .subscribe(new Consumer<HttpResponse<T>>() {
                    @Override
                    public void accept(HttpResponse<T> saasHttpResponse) throws Exception {
                        if (saasHttpResponse.isSuccess()) {
                            callback.onSuccess(saasHttpResponse.getObj());
                        } else {
                            callback.onFailure(saasHttpResponse.getErrorCode(), saasHttpResponse.getErrorMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof HttpError) {
                            callback.onThrowable((HttpError) throwable);
                        } else {
                            callback.onThrowable(new HttpError("暂未知异常", throwable));
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
    }

    /**
     * 处理重复请求的Observable
     *
     * @param observable
     * @param retryCount
     * @param callback
     * @param <T>
     */
    public <T> void requestSubscribeWithRetry(Observable observable, int retryCount, final IHttpCallback<T> callback) {
        if (retryCount > 0) {
            observable.retry(retryCount);
        }

        requestSubscribe(observable, callback);
    }

    /**
     * 获取网络请求的原始数据
     *
     * @param call
     * @param callback
     */
    public void requestCall(Call<ResponseBody> call, final IHttpCallback<String> callback) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        callback.onSuccess(response.body().string());
                    } catch (IOException e) {
                        LogUtils.w(e.getMessage());
                        callback.onFailure(String.valueOf(response.code()), e.getMessage());
                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        callback.onFailure(String.valueOf(response.code()), response.errorBody().string());
                    } catch (IOException e) {
                        LogUtils.w(e.getMessage());
                        callback.onFailure(String.valueOf(response.code()), e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onThrowable(new HttpError(t));
            }
        });
    }
}
