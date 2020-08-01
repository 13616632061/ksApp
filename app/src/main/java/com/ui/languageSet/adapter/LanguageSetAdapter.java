package com.ui.languageSet.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ui.ks.R;
import com.ui.languageSet.bean.LanguageSetBean;

import java.util.List;

/**
 * @Description:语言设置adapter
 * @Author:lyf
 * @Date: 2020/6/1
 */

public class LanguageSetAdapter extends BaseQuickAdapter<LanguageSetBean, BaseViewHolder> {


    public LanguageSetAdapter(int layoutResId, @Nullable List<LanguageSetBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LanguageSetBean item) {
        helper.setImageResource(R.id.img_language, item.getImg());
        helper.setText(R.id.tv_language_name, item.getLanguageName());
        if (item.isSeleteItem()) {
            helper.setGone(R.id.tv_language_selete, true);
        } else {
            helper.setGone(R.id.tv_language_selete, false);

        }
    }
}
