package com.library.base.mvp;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2019/4/24.
 */

public class BasePresenter<V> {

    protected V mView;
    private CompositeSubscription subscription;

    public BasePresenter(V mView) {
      attachView(mView);
    }

    /**
     * Presenter与View建立连接
     * @param mView
     */
    public void attachView(V mView){
        this.mView = mView;
    }

    /**
     * Presenter与View连接断开
     */
    public void deatchView(){
        mView=null;
    }

    /**
     * 添加订阅
     * @param observable
     * @param subscriber
     */
    public void addSubscription(Observable observable, Subscriber subscriber){
        if(subscription==null){
            subscription=new CompositeSubscription();
        }
        subscription.add(observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber));
    }

    /**
     * 取消订阅
     */
    public void onUnsubscription(){
        if(subscription!=null&&subscription.hasSubscriptions()){
            subscription.unsubscribe();
        }
    }
}
