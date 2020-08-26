package com.ui.ks.webView;


import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.constant.RouterPath;
import com.ui.ks.R;

/**
*@Description:webview
*@Author:lyf
*@Date: 2020/8/15
*/
@Route(path = RouterPath.ACTIVITY_HTML)
public class HtmlActivity extends BaseHtmlActivity {

    private final static String TAG = HtmlActivity.class.getSimpleName();

    @Autowired(name = "url")
    String url;

    @Override
    public int setContentView() {
        return R.layout.activity_html;
    }

    @Override
    protected void initView() {
        super.initView();
        LogUtils.i(TAG + "url:  " + url);
        load(url);
    }
}
