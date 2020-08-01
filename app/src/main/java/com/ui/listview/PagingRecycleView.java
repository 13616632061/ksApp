package com.ui.listview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by eddie on 16/4/17.
 */
public class PagingRecycleView extends RecyclerView {
    private boolean isLoading;
    private boolean hasMoreItems;
    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_HEADER = 1;//头部--支持头部增加一个headerView
    public final static int TYPE_FOOTER = 2;//底部--往往是loading_more
    public final static int TYPE_LIST = 3;//代表item展示的模式是list模式
    public final static int TYPE_STAGGER = 4;//代码item展示模式是网格模式

    public PagingRecycleView(Context context) {
        super(context);
        init();
    }

    public PagingRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PagingRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化-添加滚动监听
     * <p/>
     * 回调加载更多方法，前提是
     * <pre>
     *    1、有监听并且支持加载更多：null != mListener && mIsFooterEnable
     *    2、目前没有在加载，正在上拉（dy>0），当前最后一条可见的view是否是当前数据列表的最好一条--及加载更多
     * </pre>
     */
    private void init() {
        isLoading = false;

        super.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }
    public boolean hasMoreItems() {
        return this.hasMoreItems;
    }

    public void onFinishLoading(boolean hasMoreItems) {
        setHasMoreItems(hasMoreItems);
        setIsLoading(false);
    }

    public void setHasMoreItems(boolean hasMoreItems) {
        this.hasMoreItems = hasMoreItems;
//        if(!this.hasMoreItems) {
//            //没有『更多』，移除footer
//        } else {
//            //有『更多』，添加footer
//        }
    }
}
