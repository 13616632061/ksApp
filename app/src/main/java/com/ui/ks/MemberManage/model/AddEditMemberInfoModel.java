package com.ui.ks.MemberManage.model;

import com.api.ApiRetrofit;
import com.ui.ks.MemberManage.contract.AddEditMemberInfoContract;

import rx.Observable;

/**
 * Created by lyf on 2020/9/19.
 */

public class AddEditMemberInfoModel implements AddEditMemberInfoContract.Model {


    /**
     * @Description:添加会员
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public Observable addMemberInfo(String memberInfo) {
        return ApiRetrofit.getInstance().getApiService().addMemberInfo(memberInfo);
    }
    /**
    *@Description:编辑会员
    *@Author:lyf
    *@Date: 2020/9/22
    */
    @Override
    public Observable editMemberInfo(String memberInfo) {
        return ApiRetrofit.getInstance().getApiService().editMemberInfo(memberInfo);
    }
}
