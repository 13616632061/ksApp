package com.ui.languageSet.presenter;

import android.content.Intent;

import com.base.ActivityManager;
import com.ui.ks.R;
import com.ui.languageSet.LanguageSetActivity;
import com.ui.languageSet.adapter.LanguageSetAdapter;
import com.ui.languageSet.bean.LanguageSetBean;
import com.library.LanguageUtil.LanguageUtil;
import com.library.LanguageUtil.PreferenceLanguageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:语言设置presenter
 * @Author:lyf
 * @Date: 2020/6/1
 */

public class LanguageSetPresenter implements ILanguageSetPresenterImp {

    private LanguageSetActivity mView;
    private List<LanguageSetBean> mData = new ArrayList<>();
    private LanguageSetAdapter mAdapter;

    public LanguageSetPresenter(LanguageSetActivity mView) {
        this.mView = mView;
    }

    public List<LanguageSetBean> getmData() {
        return mData;
    }

    /**
     * @Description:初始化adapter
     * @Author:lyf
     * @Date: 2020/6/1
     */
    @Override
    public void initLanguageAdapter() {
        mAdapter = mView.initAdapter();
    }

    /**
     * @Description:初始化语言数据
     * @Author:lyf
     * @Date: 2020/6/1
     */
    @Override
    public void initLanguageData() {
        String[] nameArray = mView.getResources().getStringArray(R.array.language);
        int[] imgArray = new int[]{R.drawable.cn,R.drawable.en};
        for (int i = 0; i < nameArray.length; i++) {
            LanguageSetBean languageSetBean = new LanguageSetBean();
            languageSetBean.setImg(imgArray[i]);
            languageSetBean.setLanguageName(nameArray[i]);
            mData.add(languageSetBean);
        }
        if (PreferenceLanguageUtils.getInstance().getLanguage() != -1) {
            mData.get(PreferenceLanguageUtils.getInstance().getLanguage()).setSeleteItem(true);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * @Description:语言选择
     * @Author:lyf
     * @Date: 2020/6/1
     */
    @Override
    public void onItemClick(int positon) {
        PreferenceLanguageUtils.getInstance().setLanguage(positon);
        LanguageUtil.setLanguage(mView.getApplicationContext(),LanguageUtil.switchLanguage(positon));
        ActivityManager.getInstance().finishAllActivity();
        Intent intent=new Intent(mView,LanguageSetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mView.startActivity(intent);
//        mView.finish();

    }
}
