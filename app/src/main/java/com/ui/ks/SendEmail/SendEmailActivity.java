package com.ui.ks.SendEmail;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.BarUtils;
import com.constant.RouterPath;
import com.library.base.mvp.BaseActivity;
import com.ui.ks.R;
import com.ui.ks.SendEmail.contract.SendEmailContract;
import com.ui.ks.SendEmail.presenter.SendEmailPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Description:发送邮箱
 * @Author:lyf
 * @Date: 2020/7/12
 */
@Route(path = RouterPath.ACTIVITY_SEND_EMAIL)
public class SendEmailActivity extends BaseActivity implements SendEmailContract.View {


    @BindView(R.id.et_email)
    EditText etEmail;

    @Autowired(name = "Batch")
    String Batch;//盘点记录id

    private SendEmailPresenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_send_email;
    }

    @Override
    protected void initView() {
        initTabTitle(getString(R.string.str375), "");
        mPresenter = new SendEmailPresenter(this);
    }

    @OnClick({R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send://发送
                mPresenter.sendToEmai(Batch);
                break;
        }
    }

    /**
     * @Description:获取邮箱地址
     * @Author:lyf
     * @Date: 2020/7/12
     */
    @Override
    public String getEmai() {
        return etEmail.getText().toString().trim();
    }
}
