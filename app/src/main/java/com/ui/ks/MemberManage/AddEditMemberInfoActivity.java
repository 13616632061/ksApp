package com.ui.ks.MemberManage;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.constant.RouterPath;
import com.library.base.mvp.BaseActivity;
import com.ui.entity.Member;
import com.ui.ks.MemberManage.contract.AddEditMemberInfoContract;
import com.ui.ks.MemberManage.presenter.AddEditMemberInfoPresenter;
import com.ui.ks.R;
import com.ui.util.DateUtils;
import com.ui.util.SysUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description:添加/编辑会员
 * @Author:lyf
 * @Date: 2020/9/19
 */
@Route(path = RouterPath.ACTIVITY_ADD_MEMBER)
public class AddEditMemberInfoActivity extends BaseActivity implements AddEditMemberInfoContract.View {


    @BindView(R.id.ed_member_name)
    EditText edMemberName;
    @BindView(R.id.ed_member_phone)
    EditText edMemberPhone;
    @BindView(R.id.ed_member_balance)
    EditText edMemberBalance;
    @BindView(R.id.ed_member_integral)
    EditText edMemberIntegral;
    @BindView(R.id.ed_discount_rate)
    EditText edDiscountRate;
    @BindView(R.id.ed_cash_back)
    EditText edCashBack;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;


    @Autowired(name = "memberBean")
    Member.ResponseBean.DataBean.InfoBean memberBean;

    private AddEditMemberInfoPresenter mPresenter;

    private Calendar mCalendar = Calendar.getInstance(Locale.CHINA);//设置为中国时间
    private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式


    @Override
    public int getContentView() {
        return R.layout.activity_add_member_info;
    }

    @Override
    protected void initView() {
        setTitle();
        mPresenter = new AddEditMemberInfoPresenter(this);

    }

    @OnClick({R.id.btn_save, R.id.tv_birthday})
    public void setOnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save://确定添加
                setSureOnClick();
                break;
            case R.id.tv_birthday://会员生日
                showCalendarDate(tvBirthday);
                break;
        }
    }

    /**
     * @Description:设置title
     * @Author:lyf
     * @Date: 2020/9/22
     */
    @Override
    public void setTitle() {
        if (memberBean == null) {
            initTabTitle(getResources().getString(R.string.str428), "");//添加会员
        } else {
            initTabTitle(getResources().getString(R.string.str433), "");//编辑会员
            initMemberInfo();
        }
    }

    /**
     * @Description:初始化会员信息
     * @Author:lyf
     * @Date: 2020/9/22
     */
    @Override
    public void initMemberInfo() {
        setMemberName(memberBean.getMember_name());
        setMemberPhone(memberBean.getMobile());
        setMemberBalance(memberBean.getSurplus());//初始余额
        setMemberIntegral(memberBean.getScore());
        setDiscountRate(memberBean.getDiscount_rate());
        setCashBack(memberBean.getFanxian());
        setBirthday(memberBean.getBirthday());
    }

    /**
     * @Description:会员id
     * @Author:lyf
     * @Date: 2020/9/22
     */
    @Override
    public String getMemberId() {
        return memberBean == null ? "" : memberBean.getMember_id();
    }

    /**
     * @Description:会员名
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public String getMemberName() {
        return edMemberName.getText().toString().trim();
    }

    @Override
    public void setMemberName(String memberName) {
        edMemberName.setText(memberName);
    }

    /**
     * @Description:手机号码
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public String getMemberPhone() {
        return edMemberPhone.getText().toString().trim();
    }

    @Override
    public void setMemberPhone(String memberPhone) {
        edMemberPhone.setText(memberPhone);
    }

    /**
     * @Description:初始余额
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public String getMemberBalance() {
        return edMemberBalance.getText().toString().trim();
    }

    @Override
    public void setMemberBalance(String memberBalance) {
        edMemberBalance.setText(memberBalance);
    }

    /**
     * @Description:初始积分
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public String getMemberIntegral() {
        return edMemberIntegral.getText().toString().trim();
    }

    @Override
    public void setMemberIntegral(String memberIntegral) {
        edMemberIntegral.setText(memberIntegral);
    }

    /**
     * @Description:折扣
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public String getDiscountRate() {
        return edDiscountRate.getText().toString().trim();
    }

    @Override
    public void setDiscountRate(String discountRate) {
        edDiscountRate.setText(discountRate);
    }

    /**
     * @Description:返现
     * @Author:lyf
     * @Date: 2020/9/21
     */
    @Override
    public String getCashBack() {
        return edCashBack.getText().toString().trim();
    }

    @Override
    public void setCashBack(String cashBack) {
        edCashBack.setText(cashBack);
    }

    /**
     * @Description:会员生日
     * @Author:lyf
     * @Date: 2020/9/21
     */
    @Override
    public String getBirthday() {
        return tvBirthday.getText().toString().trim();
    }

    @Override
    public void setBirthday(String birthday) {
        tvBirthday.setText(birthday);
    }

    /**
     * @Description:显示日期
     * @Author:lyf
     * @Date: 2020/9/22
     */
    @Override
    public void showCalendarDate(TextView textView) {
        final String CurDate = DateUtils.getCurDate().substring(0, 10);
        DatePickerDialog.OnDateSetListener data = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (DateUtils.getDateSpan(CurDate, mDateFormat.format(mCalendar.getTime()), 1) > 0) {
                    textView.setText(CurDate);
                    //开始日期超过当前日期,自动调整为当前日期
                    SysUtils.showError(getString(R.string.str334));
                } else {
                    textView.setText(mDateFormat.format(mCalendar.getTime()));
                }

            }
        };
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(this, data, mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.show();
    }

    /**
     * @Description:确定点击事件
     * @Author:lyf
     * @Date: 2020/9/22
     */
    @Override
    public void setSureOnClick() {
        if (memberBean == null) {
            mPresenter.addMemberInfo();
        } else {
            mPresenter.editMemberInfo();
        }
    }

}
