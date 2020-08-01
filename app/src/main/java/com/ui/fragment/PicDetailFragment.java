package com.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ui.ks.PicDetailActivity;
import com.ui.ks.PicViewActivity;
import com.ui.ks.R;
import com.ui.view.ProgressWheel;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PicDetailFragment extends Fragment {
	private String picUrl = "";
	boolean hasLoading = false;
	ProgressWheel loading_bar;
	ImageView mZoomView = null;
	private boolean isView = false;
	PhotoViewAttacher mAttacher = null;
	private boolean getData = false;

	DisplayImageOptions imageLoader_options = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		picUrl = getArguments() != null ? getArguments().getString("picUrl") : ""; 
		isView = getArguments() != null ? getArguments().getBoolean("isView") : false; 
	}

	public static PicDetailFragment newInstance(int position, String picUrl, boolean isView) {  
		PicDetailFragment f = new PicDetailFragment();  
		
        Bundle args = new Bundle();  
        args.putString("picUrl", picUrl);   
        args.putBoolean("isView", isView);   
        f.setArguments(args);

        return f;  
    }

	private void setTap() {
		if(isView) {
			PicViewActivity activity = (PicViewActivity) getActivity();
			activity.setInterface();
		} else {
			PicDetailActivity activity = (PicDetailActivity) getActivity();
			activity.setInterface();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pic_item, container, false);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setTap();
			}
		});
		
		mZoomView = (ImageView) view.findViewById(R.id.zoomView);
		loading_bar = (ProgressWheel) view.findViewById(R.id.progress_large);
//		MDTintHelper.setTint(loading_bar, SysUtils.getWidgetColor(getActivity()));

		if(getData) {
			getData();
		}

		return view;
	}
	
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser) {
			if(picUrl.length() > 0 && !hasLoading) {
				if(mZoomView != null) {
					getData();
				} else {
					//还没有初始化，那么在oncreateview中加载数据
					getData = true;
				}
			}
		}
	}

	private void getData() {
//		Picasso.with(getActivity())
//						.load(picUrl)
//						.into(mZoomView, imageLoadedCallback);

		ImageLoader.getInstance()
				.displayImage(picUrl, mZoomView, imageLoader_options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						loading_bar.setProgress(0);
						loading_bar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						loading_bar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						loading_bar.setVisibility(View.GONE);

						//加载完成
						if(mAttacher!=null){
							mAttacher.update();
						}else{
							mAttacher = new PhotoViewAttacher(mZoomView);
							mAttacher.setMaximumScale(2.0f);
							mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
								public void onViewTap(View view, float x, float y) {
									setTap();
								}
							});
						}
						mZoomView.setVisibility(View.VISIBLE);

						hasLoading = true;
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view, int current, int total) {
						loading_bar.setProgress(Math.round(100.0f * current / total));
					}
				});
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
}