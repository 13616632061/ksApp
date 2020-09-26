package com.ui.ks.accountExport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.api.ApiRetrofit;
import com.blankj.utilcode.util.LogUtils;
import com.constant.RouterPath;
import com.library.base.mvp.BaseActivity;
import com.ui.ks.R;
import com.ui.ks.accountExport.contract.AccountExportContract;
import com.ui.ks.accountExport.presenter.AccountExportPresenter;
import com.ui.util.CustomRequest;
import com.ui.util.DateUtils;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Description:数据导出
 * @Author:lyf
 * @Date: 2020/9/26
 */
@Route(path = RouterPath.ACTIVITY_ACCOUNT_EXPORT)
public class AccountExportActivity extends BaseActivity implements AccountExportContract.View, View.OnClickListener {

    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.btn_nearly_seven)
    Button btnNearlySeven;
    @BindView(R.id.btn_nearly_thirty)
    Button btnNearlyThirty;
    @BindView(R.id.layout_nearly_days)
    RelativeLayout layoutNearlyDays;
    @BindView(R.id.iv_drop)
    ImageView ivDrop;
    @BindView(R.id.tv_starttime)
    TextView tvStarttime;
    @BindView(R.id.layout_set_starttime)
    RelativeLayout layoutSetStarttime;
    @BindView(R.id.iv_drop2)
    ImageView ivDrop2;
    @BindView(R.id.tv_endtime)
    TextView tvEndtime;
    @BindView(R.id.layout_set_endtime)
    RelativeLayout layoutSetEndtime;
    @BindView(R.id.et_input_e_mail)
    EditText etInputEMail;
    @BindView(R.id.layout_e_mail)
    RelativeLayout layoutEMail;
    @BindView(R.id.tv_email_explain)
    TextView tvEmailExplain;
    @BindView(R.id.btn_e_mail)
    Button btnEMail;
    @BindView(R.id.btn_native)
    Button btnNative;
    @BindView(R.id.activity_put_report)
    RelativeLayout activityPutReport;

    @Autowired(name = "type")
    int type;

    private static final int REQUEST_CODE = 200;
    private static final int RESULT_CODE = 200;


    private AccountExportPresenter mPresenter;


    @Override
    public int getContentView() {
        return R.layout.activity_put_report;
    }

    @Override
    protected void initView() {
        initTabTitle(getResources().getString(R.string.title_activity_putreport), "");//对账导出
        mPresenter = new AccountExportPresenter(this);
        setEndTime(DateUtils.getCurDate());
    }

    @OnClick({R.id.btn_nearly_seven, R.id.btn_nearly_thirty, R.id.btn_e_mail, R.id.btn_native,
            R.id.layout_set_starttime, R.id.layout_set_endtime, R.id.tv_recently_used_email})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_nearly_seven://近7天
                setStartTime(DateUtils.getNearlyDate(getEndTime(), 7));
                break;
            case R.id.btn_nearly_thirty://近30天
                setStartTime(DateUtils.getNearlyDate(getEndTime(), 30));
                break;
            case R.id.btn_e_mail://导出邮箱
                switch (type) {
                    case 1://销售统计导出邮箱
                        mPresenter.goodSalesDatasendEmail();
                        break;
                    default://对账水流导出邮箱
                        mPresenter.sendEmailReportAccount();
                        break;
                }

                break;
            case R.id.btn_native://导入本地
                switch (type) {
                    case 1://销售统计导出邮箱

                        break;
                    default://对账水流导出邮箱
                        mPresenter.downLoadExcel();
                        break;
                }

                break;
            case R.id.layout_set_starttime://开始时间
                DateUtils.runTime(this, tvStarttime);
                break;
            case R.id.layout_set_endtime://结束时间
                DateUtils.runTime(this, tvEndtime);
                break;
            case R.id.tv_recently_used_email://最近使用过的邮箱
                toGoRecentlyUsedEmailPage();
                break;
        }
    }


    /**
     * @Description:开始时间
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @Override
    public void setStartTime(String starttime) {
        tvStarttime.setText(starttime);
    }

    @Override
    public String getStartTime() {
        return tvStarttime.getText().toString().trim();
    }

    /**
     * @Description:结束时间
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @Override
    public void setEndTime(String endTime) {
        tvEndtime.setText(TextUtils.isEmpty(endTime) ? DateUtils.getCurDate() : endTime);
    }

    @Override
    public String getEndTime() {
        return tvEndtime.getText().toString().trim();
    }

    /**
     * @Description:导出邮箱
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @Override
    public void setEmail(String email) {
        etInputEMail.setText(email);
        etInputEMail.setSelection(TextUtils.isEmpty(email) ? 0 : email.length());
    }

    @Override
    public String getEmail() {
        return etInputEMail.getText().toString().trim();
    }

    /**
     * @Description:跳转最近使用过的邮箱
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @Override
    public void toGoRecentlyUsedEmailPage() {
        ARouter.getInstance().build(RouterPath.ACTIVITY_RECENTLY_USED_EMAIL)
                .navigation(this, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_CODE) {
                    String email = data.getExtras().getString("email");
                    setEmail(email);

                }
                break;
        }
    }
}
