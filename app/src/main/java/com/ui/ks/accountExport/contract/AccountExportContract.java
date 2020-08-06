package com.ui.ks.accountExport.contract;

import com.bean.ResultResponse;

import retrofit2.http.Field;
import rx.Observable;

/**
 * Created by lyf on 2020/8/1.
 */

public interface AccountExportContract {

    interface View {
        /**
         * @Description:开始时间
         * @Author:lyf
         * @Date: 2020/8/1
         */
        void setStartTime(String starttime);

        String getStartTime();

        /**
         * @Description:结束时间
         * @Author:lyf
         * @Date: 2020/8/1
         */
        void setEndTime(String endTime);

        String getEndTime();

        /**
         * @Description:导出邮箱
         * @Author:lyf
         * @Date: 2020/8/1
         */
        void setEmail(String email);

        String getEmail();
        /**
        *@Description:跳转最近使用过的邮箱
        *@Author:lyf
        *@Date: 2020/8/1
        */
        void toGoRecentlyUsedEmailPage();
    }

    interface Presenter {
        /**
         * @Description:对账单导出至邮箱
         * @Author:lyf
         * @Date: 2020/8/1
         */
        void sendEmailReportAccount();

        /**
         * @Description:导出对账流水至本地
         * @Author:lyf
         * @Date: 2020/8/1
         */
        void downLoadExcel();

        /**
         * @Description:保存excel文件
         * @Author:lyf
         * @Date: 2020/8/1
         */
        void saveExcelFile(String url);

    }

    interface Model {
        /**
         * @Description:对账单导出
         * @Author:lyf
         * @Date: 2020/8/1
         */
        Observable sendReportAccount(String begintime, String endtime, String Email);

        /**
         * @Description:导出对账流水至本地
         * @Author:lyf
         * @Date: 2020/8/1
         */
        Observable downLoadExcel(String begintime, String endtime);

        /**
         * @Description:保存excel文件
         * @Author:lyf
         * @Date: 2020/8/1
         */
        Observable saveExcelFile(String url);
    }
}
