package com.ui.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PicDetailAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments;
	
	public PicDetailAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public PicDetailAdapter(FragmentManager fm, 
							List<Fragment> fragments, 
							Context context) {
		super(fm);
		
		this.fragments = fragments;
	}
	
	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
    
}


