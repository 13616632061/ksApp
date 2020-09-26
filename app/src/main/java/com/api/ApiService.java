package com.api;

import com.bean.GoodInfoRespone;
import com.bean.InventoryRecordRespone;
import com.bean.ReportLostListRespone;
import com.bean.ResultResponse;
import com.bean.SalesStatisticsRespone;
import com.ui.entity.GoodsSalesStatisticsRespone;
import com.ui.entity.Member;
import com.ui.entity.OutInStoreListResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2019/5/9.
 */

public interface ApiService {

    /**
     * 盘点记录
     *
     * @return
     */
    @GET(ApiConstant.COMMENT_SERVER_URL + ApiConstant.INVENTORY_RECORD)
    Observable<InventoryRecordRespone> getInventoryRecord();

    /**
     * @param Email 邮箱地址
     * @param Batch 盘点记录id
     * @Description:发送邮箱
     * @Author:lyf
     * @Date: 2020/7/12
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.SEND_EMAIL)
    @FormUrlEncoded
    Observable<ResultResponse> sendEmail(@Field("Email") String Email, @Field("batch") String Batch);

    /**
     * @Description:报损列表
     * @Author:lyf
     * @Date: 2020/7/19
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.REPORT_LOSS)
    @FormUrlEncoded
    Observable<ReportLostListRespone> getReportLossList(@Field("startTime") String startTime, @Field("endTime") String endTime);

    /**
     * @param content 搜索内容
     * @param page    分页页码
     * @Description:
     * @Author:lyf
     * @Date: 2020/7/19
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.SEARCH_GOODS)
    @FormUrlEncoded
    Observable<GoodInfoRespone> searchGoods(@Field("search") String content, @Field("page") String page);

    /**
     * @Description:添加报损商品
     * @Author:lyf
     * @Date: 2020/7/22
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.ADD_REPORT_LOSS_GOODS)
    @FormUrlEncoded
    Observable<ResultResponse> addReportLossGoods(@Field("map") String map);


    /**
     * @Description:添加报损商品
     * @Author:lyf
     * @Date: 2020/7/22
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.SEND_REPORT_ACCOUNT)
    @FormUrlEncoded
    Observable<ResultResponse> sendReportAccount(@Field("begintime") String begintime, @Field("endtime") String endtime, @Field("Email") String Email);

    /**
     * @Description:描述
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.DOWN_EXCEL_LOCALD)
    @FormUrlEncoded
    Observable<ResultResponse> downLoadExcel(@Field("begintime") String begintime, @Field("endtime") String endtime);


    /**
     * @Description:商品销售统计
     * @Author:lyf
     * @Date: 2020/8/2
     */
    @GET(ApiConstant.COMMENT_SERVER_URL + ApiConstant.COUNT_GOODS)
    Observable<GoodsSalesStatisticsRespone> getCountGoods();

    /**
     * @Description:当月现金统计
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.MON_ORDER_CASH)
    @FormUrlEncoded
    Observable<SalesStatisticsRespone> curMonthcashStatistics(@Field("date") String date, @Field("page") String page, @Field("pagelimit") String pagelimit);

    /**
     * @Description:当月移动统计
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.MON_ORDER)
    @FormUrlEncoded
    Observable<SalesStatisticsRespone> curMonthLineStatistics(@Field("date") String date, @Field("page") String page, @Field("pagelimit") String pagelimit);


    /**
     * @Description:当月会员统计
     * @Author:lyf
     * @Date: 2020/8/1
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.MON_ORDER)
    @FormUrlEncoded
    Observable<SalesStatisticsRespone> curMonthMemberStatistics(@Field("date") String date, @Field("page") String page, @Field("pagelimit") String pagelimit, @Field("tag") String member);

    /**
     * @Description:查询出入库列表数据
     * @Author:lyf
     * @Date: 2020/9/12
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.DETAIL_LIST)
    @FormUrlEncoded
    Observable<OutInStoreListResponse> queryOutInStoreListData(@Field("starttime") String startTime, @Field("endtime") String endTime, @Field("page") String page);

    /**
     * @Description:会员列表
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.MENBERS_LIST)
    @FormUrlEncoded
    Observable<Member> queryMemberList(@Field("page") String page);

    /**
     * @Description:添加会员
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.MENBERS_ADD)
    @FormUrlEncoded
    Observable<ResultResponse> addMemberInfo(@Field("map") String memberInfo);

    /**
     * @Description:编辑会员
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.MENBERS_EDIT)
    @FormUrlEncoded
    Observable<ResultResponse> editMemberInfo(@Field("map") String memberInfo);

    /**
     * @Description:根据会员名会员搜索
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.MENBERS_SEARCH)
    @FormUrlEncoded
    Observable<Member> memberSearchName(@Field("member_name") String memberName);

    /**
     * @Description:根据手机号会员搜索
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @POST(ApiConstant.COMMENT_SERVER_URL + ApiConstant.MENBERS_SEARCH)
    @FormUrlEncoded
    Observable<Member> memberSearchMobile(@Field("mobile") String mobile);

    /**
     * @Description:商品销售筛选
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @POST(ApiConstant.GOODS_FILTER)
    @FormUrlEncoded
    Observable<ResultResponse> goodsStatisticFilter(@Field("beginTime") String beginTime, @Field("endTime") String endTime);

    /**
     * @Description:商品销售数据导出至邮箱
     * @Author:lyf
     * @Date: 2020/9/19
     */
    @POST(ApiConstant.GOODS_FILTER)
    @FormUrlEncoded
    Observable<ResultResponse> goodSalesDatasendEmail(@Field("beginTime") String begintime, @Field("endTime") String endtime, @Field("Email") String Email);


}
