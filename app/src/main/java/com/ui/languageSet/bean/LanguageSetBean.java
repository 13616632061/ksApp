package com.ui.languageSet.bean;

/**
*@Description:语言设置实体
*@Author:lyf
*@Date: 2020/6/1
*/

public class LanguageSetBean {

    private int img;//语言国家图标
    private String languageName;//语言名字
    private boolean seleteItem;//选择设置的语言


    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public boolean isSeleteItem() {
        return seleteItem;
    }

    public void setSeleteItem(boolean seleteItem) {
        this.seleteItem = seleteItem;
    }
}
