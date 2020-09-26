import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_shop/bean/BannerBean.dart';
import 'package:flutter_shop/bean/good_info_list_bean.dart';
import 'package:flutter_shop/widget/shop_car_widget.dart';
import 'package:flutter_swiper/flutter_swiper.dart';
import 'package:flutter_shop/utils/ScreenUtils.dart';
import 'package:flutter_shop/bean/GoodTypeBean.dart';
import 'package:flutter_shop/bean/good_info_bean.dart';
import 'package:flutter_shop/res/String/Strings.dart';
import 'package:flutter_shop/widget/LineWidget.dart';

import 'add_subtract_widget.dart';

/**
 * 商品列表
 */
class GoodListWidget extends StatefulWidget {
  @override
  _GoodListWidgetState createState() => _GoodListWidgetState();

}

class _GoodListWidgetState extends State<GoodListWidget> {
  List<BannerInfo> _bannerInfoList = [];
  List<GoodType> _goodTypeData = [];
  SwiperController _swiperController = SwiperController();
  List<GoodInfo> _goodsInfoData = [];
  int curSelectTypeIndex = 0;
  GoodsInfoListBean goodsInfoListBean;

  //购物车数量
  int shopCarNum = 0;

  @override
  void initState() {
    super.initState();
    _getBannerData();
    _getGoodTypeData();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
          children: <Widget>[
            _BannerSwiper(),
            _goodList(),
            ShopCarWidget(
              shopCarNum: shopCarNum,
            )
          ]),
    );
  }

  _BannerSwiper() {
    return Container(
      height: 100.0,
      child: Swiper(
          key: UniqueKey(),
          itemBuilder: (BuildContext context, int index) {
            return Image.network(
              _bannerInfoList[index].pictureUrl, fit: BoxFit.fill,);
          },
          itemCount: _bannerInfoList.length,
          scrollDirection: Axis.horizontal,
          loop: true,
          duration: 300,
          autoplay: true,
          onTap: (index) {
            debugPrint("点击了第:$index个");
          },
          controller: _swiperController,
          autoplayDelay: 3000,
          autoplayDisableOnInteraction: true
      ),
    );
  }

  _goodList() {
    return Expanded(
        child:
        Container(
          child: Row(
            children: <Widget>[
              Expanded(
                flex: 1,
                child: MediaQuery.removePadding(context: context,
                    removeTop: true,
                    child: _goodTypeWidget()),),
              Expanded(
                flex: 4,
                child: MediaQuery.removePadding(context: context,
                    removeTop: true,
                    child: _goodListWidget()),),
            ],
          ),
        ));
  }

  /**
   * 分类列表
   */
  _goodTypeWidget() {
    return Container(
      color: Colors.grey.shade200,
      child: ListView.builder(
//        physics: new NeverScrollableScrollPhysics(),
        key: UniqueKey(),
        itemCount: _goodTypeData.length,
        itemBuilder: (BuildContext context,
            int index) {
          // 列表item
          return GestureDetector(
            onTap: () {
              setState(() {
                curSelectTypeIndex = index;
                _queryCurTypeGoods();
              });
            },
            child: Container(
                height: 60,
                color: curSelectTypeIndex == index ? Colors.white : Colors.grey
                    .shade200,
                alignment: Alignment.center,
                child: Text(_goodTypeData[index].typeName,
                  style: TextStyle(fontSize: 12,
                      color: curSelectTypeIndex == index
                          ? Colors.orange
                          : Colors.black),)),
          );
        },
      ),
    );
  }

  /**
   * 商品列表
   */
  _goodListWidget() {
    return Container(
        padding: EdgeInsets.only(left: 10, right: 10),
        child: Column(
          children: <Widget>[
            _topCurType(),
            Expanded(
                child: Container(
                  child: ListView.builder(
                      key: UniqueKey(),
                      physics: new NeverScrollableScrollPhysics(),
                      itemCount: _goodsInfoData.length,
                      itemBuilder: (context, index) {
                        return Container(
                          height: 120,
                          decoration: BoxDecoration(
                              border: Border(bottom: BorderSide(
                                  color: Colors.grey.shade200))
                          ),
                          child: Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: <Widget>[
                              //商品图片
                              Container(
                                height: 80,
                                width: 80,
                                margin: EdgeInsets.only(top: 20),
                                decoration: BoxDecoration(
                                    borderRadius: BorderRadius.all(
                                        Radius.circular(5)),
                                    image: DecorationImage(
                                        image: NetworkImage(
                                            _goodsInfoData[index]
                                                .photoUrl),
                                        fit: BoxFit.cover)
                                ),
                              ),
                              Expanded(child: Container(
                                margin: EdgeInsets.only(left: 10, top: 10),
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: <Widget>[
                                    //名字title
                                    Text(
                                      _goodsInfoData[index].name,
                                      maxLines: 2,
                                      overflow: TextOverflow.ellipsis,
                                      style: TextStyle(
                                          fontWeight: FontWeight.bold,
                                          fontSize: 14),),
                                    //tips
                                    Padding(
                                      padding: EdgeInsets.only(top: 5),
                                      child: Text(
                                        _goodsInfoData[index].tips,
                                        maxLines: 1,
                                        overflow: TextOverflow.ellipsis,
                                        style: TextStyle(
                                            fontSize: 12,
                                            color: Colors.grey),),),
                                    //月份
                                    Container(
                                      margin: EdgeInsets.only(top: 5),
                                      child: Text("${Strings
                                          .month_selas}${_goodsInfoData[index]
                                          .saleNum}${Strings
                                          .stocks}", style: TextStyle(
                                          fontSize: 12,
                                          color: Colors.grey),),),
                                    //价格
                                    Container(
                                      height: 40,
                                      child: Row(
                                        mainAxisAlignment: MainAxisAlignment
                                            .spaceBetween,
                                        children: <Widget>[
                                          Row(
                                            children: <Widget>[
                                              Text(
                                                "￥${_goodsInfoData[index]
                                                    .price}",
                                                style: TextStyle(fontSize: 14,
                                                    fontWeight: FontWeight.bold,
                                                    color: Colors.orange),),
                                              Text(_goodsInfoData[index]
                                                  .initPrice != null
                                                  ? "￥${ _goodsInfoData[index]
                                                  .initPrice}"
                                                  : "",
                                                style: TextStyle(
                                                    fontSize: 12,
                                                    color: Colors.grey,
                                                    decoration: TextDecoration
                                                        .lineThrough,
                                                    decorationColor: Colors.grey
                                                ),)
                                            ],
                                          ),
                                          //加减
                                          AddSubtractWidget(
                                              nums: _goodsInfoData[index]
                                                  .shopCarNum,
                                              onChanged: (value) {
                                                setState(() {
                                                  _goodsInfoData[index]
                                                      .shopCarNum = value;
                                                  _shopCarNum();
                                                });
                                              }
                                          )

                                        ],
                                      ),
                                    ),
                                  ],

                                ),
                              ))
                            ],

                          ),
                        );
                      }),
                ))
          ],
        ));
  }

  /**
   * 头部当前分类
   */
  _topCurType() {
    return Container(
      height: 60,
      alignment: Alignment.centerLeft,
      decoration: BoxDecoration(
          color: Colors.white,
          border: Border(
              bottom: BorderSide(color: Colors.grey.shade200))
      ),
      child: Text(_goodTypeData[curSelectTypeIndex].typeName,
        style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold),),
    );
  }

  /**
   * 获取banner数据
   */
  _getBannerData() async {
    Future<String> loadString = DefaultAssetBundle.of(context).loadString(
        "lib/res/data/banner.json");
    loadString.then((String value) {
      setState(() {
        BannerBean response = BannerBean.fromJson(jsonDecode(value));
        _bannerInfoList.addAll(response.reslut.bannerInfo);
        print("value" + _bannerInfoList.length.toString());
      });
    });
  }

  /**
   * 获取商品分类数据
   */
  _getGoodTypeData() {
    Future<String> loadString = DefaultAssetBundle.of(context).loadString(
        "lib/res/data/fluitType.json");
    loadString.then((String value) {
      setState(() {
        GoodTypeBean response = GoodTypeBean.fromJson(jsonDecode(value));
        _goodTypeData.addAll(response.reslut);
        print("value" + _goodTypeData.length.toString());
        _getGoodInfoData(_goodTypeData[curSelectTypeIndex].typeName);
      });
    });
  }

  /**
   * 获取商品列表数据
   */
  _getGoodInfoData(String typeName) {
    print("value" + typeName);
    Future<String> loadString = DefaultAssetBundle.of(context).loadString(
        "lib/res/data/fluitGood.json");
    loadString.then((String value) {
      setState(() {
        print("value" + value.toString());
        goodsInfoListBean = GoodsInfoListBean.fromJson(jsonDecode(value));
        _queryCurTypeGoods();
      });
    });
  }

  _queryCurTypeGoods() {
    if (_goodsInfoData != null) {
      _goodsInfoData.clear();
      goodsInfoListBean.goodsInfoList.forEach((element) {
        print("value" + element.typeName);
        print("value" + _goodTypeData[curSelectTypeIndex].typeName);
        print(
            (element.typeName == _goodTypeData[curSelectTypeIndex].typeName));
        if (element.typeName == _goodTypeData[curSelectTypeIndex].typeName) {
          _goodsInfoData.addAll(element.goodInfo);
          print(element.goodInfo[0].shopCarNum);
        }
      });
    }
  }

  /**
   * 购物车数量
   */
  _shopCarNum() {
    shopCarNum = 0;
    _goodsInfoData.forEach((bean) {
      shopCarNum = shopCarNum + bean.shopCarNum;
    });
  }
}