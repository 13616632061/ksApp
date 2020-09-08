import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_shop/bean/BannerBean.dart';
import 'package:flutter_shop/bean/good_info_list_bean.dart';
import 'package:flutter_swiper/flutter_swiper.dart';
import 'package:flutter_shop/utils/ScreenUtils.dart';
import 'package:flutter_shop/bean/GoodTypeBean.dart';
import 'package:flutter_shop/bean/good_info_bean.dart';

/**
 * 商品列表
 */
class GoodListWidget extends StatefulWidget {
  @override
  _GoodListWidgetState createState() => _GoodListWidgetState();

}

class _GoodListWidgetState extends State<GoodListWidget> {
  List<BannerInfo> _bannerInfoList = List();
  List<GoodType> _goodTypeData = List();
  SwiperController _swiperController = SwiperController();
  List<GoodsInfoBean> _goodsInfoData = List();

  @override
  void initState() {
    super.initState();
    _getBannerData();
    _getGoodTypeData();
    _getGoodInfoData();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
          children: <Widget>[
            _BannerSwiper(),
            _goodList()
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
                  child: _goodTypeWidget()),
              Expanded(
                  flex: 4,
                  child: _goodListWidget(0)),
            ],
          ),
        ));
  }

  _goodTypeWidget() {
    return ListView.builder(
//        physics: new NeverScrollableScrollPhysics(),
        itemCount: _goodTypeData.length,
        itemBuilder: (BuildContext context,
            int index) {
          // 列表item
          return Container(
              height: 60,
              child: Text(_goodTypeData[index].typeName,
                style: TextStyle(fontSize: 12),));
        });
  }

  _goodListWidget(int position) {
    return Container(
        child: Column(
          children: <Widget>[
            Container(
              height: 60,
              alignment: Alignment.centerLeft,
              color: Colors.white,
              child: Text(_goodsInfoData[position].typeName),
            ),
            Expanded(child:
            Container(
              child: ListView.builder(
                  itemCount: _goodsInfoData[position].goodInfo.length,
                  itemBuilder: (context, index) {
                    return Container(
                      child: Row(
                        children: <Widget>[
                          Container(
                            child: Image.network(
                                _goodsInfoData[position].goodInfo[index]
                                    .photoUrl, fit: BoxFit.fill),
                            height: 80,
                            width: 80,
                          ),
                          Container(
                            child: Column(
                              children: <Widget>[
                                Text(
                                  _goodsInfoData[position].goodInfo[index].name,
                                  maxLines: 2,
                                  style: TextStyle(fontWeight: FontWeight.bold,
                                      fontSize: 14),),
                                Text(
                                  _goodsInfoData[position].goodInfo[index].tips,
                                  maxLines: 1, overflow: TextOverflow.ellipsis,
                                  style: TextStyle(fontSize: 12),),
                              ],
                              crossAxisAlignment: CrossAxisAlignment.start,
                            ),
                          )
                        ],
                      ),
                    );
                  }),
            ))
          ],
        ));
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
  _getGoodTypeData() async {
    Future<String> loadString = DefaultAssetBundle.of(context).loadString(
        "lib/res/data/fluitType.json");
    loadString.then((String value) {
      setState(() {
        GoodTypeBean response = GoodTypeBean.fromJson(jsonDecode(value));
        _goodTypeData.addAll(response.reslut);
        print("value" + _goodTypeData.length.toString());
      });
    });
  }

  /**
   * 获取商品列表数据
   */
  _getGoodInfoData() async {
    DefaultAssetBundle.of(context)
        .loadStructuredData(
        "lib/res/data/fluitGood.json", (value) {
      setState(() {
        print("value" + value.toString());
        GoodsInfoListBean goodsInfoListBean = GoodsInfoListBean.fromJson(
            jsonDecode(value));
        _goodsInfoData.addAll(goodsInfoListBean.goodsInfoList);
      });
    });
  }
}