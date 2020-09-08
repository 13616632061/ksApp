import 'package:flutter_shop/bean/good_info_bean.dart';

class GoodsInfoListBean {
  List<GoodsInfoBean> goodsInfoList;

  GoodsInfoListBean({this.goodsInfoList});

  factory GoodsInfoListBean.fromJson(List<dynamic> listJson) {
    List<GoodsInfoBean> memberList =
    listJson.map((value) => GoodsInfoBean.fromJson(value)).toList();

    return GoodsInfoListBean(goodsInfoList: memberList);
  }
}