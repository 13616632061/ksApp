package com.ui.ks.MemberManage.model;

import com.api.ApiRetrofit;
import com.ui.ks.MemberManage.contract.MemberManageContract;
import com.ui.ks.MemberManage.contract.MemberSearchContract;

import retrofit2.http.Field;
import rx.Observable;

/**
 * Created by lyf on 2020/9/20.
 */

public class MemberSearchModel implements MemberSearchContract.Model {

    /**
     * @Description:会员搜索
     * @Author:lyf
     * @Date: 2020/9/20
     */
    @Override
    public Observable memberSearch(int type, String searchContent) {
        if (type == 0) {
            return ApiRetrofit.getInstance().getApiService().memberSearchName(searchContent);
        } else {
            return ApiRetrofit.getInstance().getApiService().memberSearchMobile(searchContent);

        }
    }
}
