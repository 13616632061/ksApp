package com.api;


/**
 * Created by Administrator on 2019/5/9.
 */

public class ApiConstant {
    /**
     * 接口根地址
     */
//    public static final String BASE_SERVER_URL = "http://test.zjzccn.com/rpc/service/";
    public static final String COMMENT_SERVER_URL = "rpc/service/?method=";

    //盘点记录
    public static final String INVENTORY_RECORD = "ks.pingban_goods.storeRecords";
    //发送邮箱
    public static final String SEND_EMAIL = "ks.seller.sendEmail";
    //报损列表
    public static final String REPORT_LOSS = "ks.pingban_goods.bad_goods_list";
    //搜索商品
    public static final String SEARCH_GOODS = "ks.goods.goods_search";
    //添加报损商品
    public static final String ADD_REPORT_LOSS_GOODS = "ks.pingban_goods.bad_goods_add";
    //发送对账单
    public static final String SEND_REPORT_ACCOUNT = "ks.seller.export";
    //导出对账流水至本地
    public static final String DOWN_EXCEL_LOCALD = "ks.seller.download";
    //商品销售统计
    public static final String COUNT_GOODS = "ks.pingban_goods.count_goods";
    //现金统计
    public static final String MON_ORDER_CASH = "ks.seller.mon_order_cash";
    //移动统计/会员统计
    public static final String MON_ORDER = "ks.seller.mon_order";
    //建议反馈
    public static final String SUGGESTION_BACK = "http://yizhongyun.mikecrm.com/2dC7asQ";
    //出入库查询列表页
    public static final String DETAIL_LIST = "ks.pingban_seller.detail_list";
}
