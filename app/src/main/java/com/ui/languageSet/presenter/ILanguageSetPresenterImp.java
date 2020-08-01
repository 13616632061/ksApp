package com.ui.languageSet.presenter;

/**
 * @Description:语言设置presenterImp
 * @Author:lyf
 * @Date: 2020/6/1
 */

public interface ILanguageSetPresenterImp {
    //初始化adapter
    void initLanguageAdapter();

    //初始化语言数据
    void initLanguageData();

    //语言选择
    void onItemClick(int positon);
}
