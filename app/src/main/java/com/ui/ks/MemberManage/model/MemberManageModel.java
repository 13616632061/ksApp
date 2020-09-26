package com.ui.ks.MemberManage.model;

import com.api.ApiRetrofit;
import com.ui.ks.MemberManage.contract.MemberManageContract;

import rx.Observable;

/**
 * Created by lyf on 2020/9/19.
 */

public class MemberManageModel implements MemberManageContract.Model {


    /**
     * @Description:查询会员列表
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @Override
    public Observable queryMemberList(String page) {
        return ApiRetrofit.getInstance().getApiService().queryMemberList(page);
    }
}
