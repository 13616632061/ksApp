package com.ui.ks.MemberManage.contract;

import com.ui.adapter.MemberManageAdapter;
import com.ui.entity.Member;

import rx.Observable;

/**
 * Created by lyf on 2020/9/20.
 */

public interface MemberSearchContract {

    interface View {
        /**
         * @Description:搜索内容
         * @Author:lyf
         * @Date: 2020/9/20
         */
        String getSearchContent();

        /**
         * @Description:搜索类型
         * @Author:lyf
         * @Date: 2020/9/20
         */
        void getSearchTypeCheckedChangeListener();

        /**
         * @Description:初始化adapter
         * @Author:lyf
         * @Date: 2020/9/19
         */
        MemberManageAdapter initAdapter();

        /**
         * @Description:跳转会员编辑
         * @Author:lyf
         * @Date: 2020/9/20
         */
        void toGoMemberEdidPage(Member.ResponseBean.DataBean.InfoBean memberBean);

    }

    interface Presenter {

        /**
         * @Description:初始化adapter
         * @Author:lyf
         * @Date: 2020/9/20
         */
        void initAdapter();

        /**
         * @Description:会员搜索
         * @Author:lyf
         * @Date: 2020/9/20
         */
        void memberSearch();

    }

    interface Model {
        /**
         * @Description:会员搜索
         * @Author:lyf
         * @Date: 2020/9/20
         */
        Observable memberSearch(int type, String searchContent);
    }
}
