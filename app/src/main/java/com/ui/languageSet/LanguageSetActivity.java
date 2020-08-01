package com.ui.languageSet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.BaseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ui.ks.R;
import com.ui.ks.ShopActivity;
import com.ui.languageSet.adapter.LanguageSetAdapter;
import com.ui.languageSet.presenter.LanguageSetPresenter;
import com.ui.languageSet.view.ILanguageSetViewImp;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description:语言设置
 * @Author:lyf
 * @Date: 2020/6/1
 */
public class LanguageSetActivity extends BaseActivity implements ILanguageSetViewImp {


    @BindView(R.id.list_language)
    RecyclerView listLanguage;
    private LanguageSetPresenter mPrensenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_set);
        ButterKnife.bind(this);
        initToolbar(this);
        setToolbarTitle(getResources().getString(R.string.language_set));

        mPrensenter = new LanguageSetPresenter(this);
        mPrensenter.initLanguageAdapter();
        mPrensenter.initLanguageData();
    }

    /**
     * @Description:初始化adapter
     * @Author:lyf
     * @Date: 2020/6/1
     */
    @Override
    public LanguageSetAdapter initAdapter() {
        LanguageSetAdapter adapter = new LanguageSetAdapter(R.layout.item_language_set, mPrensenter.getmData());
        listLanguage.setLayoutManager(new LinearLayoutManager(this));
        listLanguage.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPrensenter.onItemClick(position);
            }
        });
        return adapter;
    }

    /**
    *@Description:返回
    *@Author:lyf
    *@Date: 2020/6/1
    */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(this, ShopActivity.class);
        intent.putExtra("curIndex",3);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
