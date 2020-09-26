package com.ui.ks.MemberManage.contract;

import android.widget.TextView;

import rx.Observable;

/**
 * Created by lyf on 2020/9/19.
 */

public interface AddEditMemberInfoContract {

    interface View {
        /**
         * @Description:设置title
         * @Author:lyf
         * @Date: 2020/9/22
         */
        void setTitle();

        /**
         * @Description:初始化会员信息
         * @Author:lyf
         * @Date: 2020/9/22
         */
        void initMemberInfo();

        /**
         * @Description:会员id
         * @Author:lyf
         * @Date: 2020/9/22
         */
        String getMemberId();

        /**
         * @Description:会员名
         * @Author:lyf
         * @Date: 2020/9/19
         */
        String getMemberName();

        void setMemberName(String memberName);

        /**
         * @Description:手机号码
         * @Author:lyf
         * @Date: 2020/9/19
         */
        String getMemberPhone();

        void setMemberPhone(String memberPhone);

        /**
         * @Description:初始余额
         * @Author:lyf
         * @Date: 2020/9/19
         */
        String getMemberBalance();

        void setMemberBalance(String memberBalance);

        /**
         * @Description:初始积分
         * @Author:lyf
         * @Date: 2020/9/19
         */
        String getMemberIntegral();

        void setMemberIntegral(String memberIntegral);

        /**
         * @Description:折扣
         * @Author:lyf
         * @Date: 2020/9/19
         */
        String getDiscountRate();

        void setDiscountRate(String discountRate);

        /**
         * @Description:返现
         * @Author:lyf
         * @Date: 2020/9/21
         */
        String getCashBack();

        void setCashBack(String cashBack);

        /**
         * @Description:会员生日
         * @Author:lyf
         * @Date: 2020/9/21
         */
        String getBirthday();

        void setBirthday(String birthday);

        /**
         * @Description:显示日期
         * @Author:lyf
         * @Date: 2020/9/22
         */
        void showCalendarDate(TextView textView);

        /**
         * @Description:确定点击事件
         * @Author:lyf
         * @Date: 2020/9/22
         */
        void setSureOnClick();

    }

    interface Presenter {
        /**
         * @Description:添加会员
         * @Author:lyf
         * @Date: 2020/9/19
         */
        void addMemberInfo();

        /**
         * @Description:编辑会员
         * @Author:lyf
         * @Date: 2020/9/22
         */
        void editMemberInfo();

        /**
         * @Description:添加会员请求参数
         * @Author:lyf
         * @Date: 2020/9/19
         */
        String getAddMemberInfoRequst();

        /**
         * @Description:编辑会员请求参数
         * @Author:lyf
         * @Date: 2020/9/22
         */
        String getEditMemberInfoRequst();
    }

    interface Model {
        /**
         * @Description:添加会员
         * @Author:lyf
         * @Date: 2020/9/19
         */
        Observable addMemberInfo(String memberInfo);

        /**
         * @Description:编辑会员
         * @Author:lyf
         * @Date: 2020/9/22
         */
        Observable editMemberInfo(String memberInfo);
    }
}
