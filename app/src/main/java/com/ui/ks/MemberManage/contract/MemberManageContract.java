package com.ui.ks.MemberManage.contract;

import com.ui.adapter.MemberManageAdapter;
import com.ui.entity.Member;

import retrofit2.http.Field;
import rx.Observable;

/**
 * Created by lyf on 2020/9/19.
 */

public interface MemberManageContract {

    interface View {
        /**
         * @Description:初始化adapter
         * @Author:lyf
         * @Date: 2020/9/19
         */
        MemberManageAdapter initAdapter();

        /**
         * @Description:下拉刷新
         * @Author:lyf
         * @Date: 2020/9/19
         */
        void refresh();

        /**
         * @Description:刷新状态
         * @Author:lyf
         * @Date: 2020/9/19
         */
        void setRefreshing(boolean refreshing);


        /**
         * @Description:跳转会员编辑
         * @Author:lyf
         * @Date: 2020/9/19
         */
        void toGoMemberEdidPage(Member.ResponseBean.DataBean.InfoBean memberBean);

        /**
         * @Description:跳转添加会员页面
         * @Author:lyf
         * @Date: 2020/9/19
         */
        void toGoAddMemberPage();

        /**
         * @Description:跳转会员搜索页面
         * @Author:lyf
         * @Date: 2020/9/20
         */
        void toGoMemberSearchPage();
    }

    interface Presenter {
        /**
         * @Description:初始化adapter
         * @Author:lyf
         * @Date: 2020/9/19
         */
        void initAdapter();

        /**
         * @Description:查询会员列表
         * @Author:lyf
         * @Date: 2020/9/19
         */
        void queryMemberList();

        /**
         * @Description:上拉加载
         * @Author:lyf
         * @Date: 2020/9/19
         */
        void loadMore();

        /**
         * @Description:清空数据
         * @Author:lyf
         * @Date: 2020/9/19
         */
        void clearData();

        /**
         * @Description:下拉刷新
         * @Author:lyf
         * @Date: 2020/9/19
         */
        void onRefresh();


    }

    interface Model {
        /**
         * @Description:查询会员列表
         * @Author:lyf
         * @Date: 2020/9/19
         */
        Observable queryMemberList(String page);
    }
}
