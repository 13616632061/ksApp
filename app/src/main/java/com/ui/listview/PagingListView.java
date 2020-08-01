package com.ui.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.ui.ks.R;

import java.util.List;

public class
PagingListView extends ListView {

	public interface Pagingable {
		void onLoadMoreItems();
	}

	private boolean isLoading;
	private boolean hasMoreItems;
	private Pagingable pagingableListener;
	private LoadingView loadingView;
	private View spacerView;
	private boolean hasSpacer = false;
	private boolean hasAddSpacer = false;

    private OnScrollListener onScrollListener;

	public PagingListView(Context context) {
		super(context);
		init();
	}

	public PagingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PagingListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public boolean isLoading() {
		return this.isLoading;
	}

	public void setIsLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	public void setPagingableListener(Pagingable pagingableListener) {
		this.pagingableListener = pagingableListener;
	}

	public void setHasMoreItems(boolean hasMoreItems) {
		this.hasMoreItems = hasMoreItems;
		if(!this.hasMoreItems) {
			//没有『更多』，移除footer
            if(findViewById(R.id.loading_view) != null) {
                removeFooterView(loadingView);
            }
		} else if(findViewById(R.id.loading_view) == null){
			//有『更多』，添加footer
			addFooterView(loadingView);
		}

		if(hasSpacer) {
			if(!hasAddSpacer){
				addFooterView(spacerView);
				hasAddSpacer = true;
			}
		}
	}

	public boolean hasMoreItems() {
		return this.hasMoreItems;
	}

	public void setHasSpacer(boolean hasSpacer) {
		this.hasSpacer = hasSpacer;
	}

	public void onFinishLoading(boolean hasMoreItems, List<? extends Object> newItems) {
		setHasMoreItems(hasMoreItems);
		setIsLoading(false);
	}


	private void init() {
		isLoading = false;
        loadingView = new LoadingView(getContext());
		spacerView = new View(getContext());

//		addFooterView(loadingView);
		super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //Dispatch to child OnScrollListener
                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //Dispatch to child OnScrollListener
                if (onScrollListener != null) {
                    onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }


                int lastVisibleItem = firstVisibleItem + visibleItemCount;
//				lastVisibleItem -= getHeaderViewsCount();

//				Log.v("huigu: ", "last: " + lastVisibleItem + ", total: " + totalItemCount);
//				Log.v("huigu", "first:" + firstVisibleItem + ", visible:" + visibleItemCount + ", total:" + totalItemCount);
                if (!isLoading && hasMoreItems && (lastVisibleItem == totalItemCount)) {
//                    Log.v("food", "load more 1");
                    if (pagingableListener != null) {
//                        Log.v("food", "load more 2");
                        isLoading = true;
                        pagingableListener.onLoadMoreItems();
                    }
                }
            }
        });
	}

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        onScrollListener = listener;
    }
}