import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_shop/bean/BannerBean.dart';
import 'package:flutter_swiper/flutter_swiper.dart';
import 'package:flutter_shop/utils/ScreenUtils.dart';
import 'package:flutter_shop/bean/GoodTypeBean.dart';

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

  @override
  void initState() {
    super.initState();
    _getBannerData();
    _getGoodTypeData();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child:  Column(
          children: <Widget>[
            _BannerSwiper(),
//              Expanded(child: _goodList(),)
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
    return
//      _goodTypeWidget();
      Expanded(
          child:
      Container(
          child: Row(
            children: <Widget>[
              Expanded(
                  flex: 1,
                  child: _goodTypeWidget()),
              Expanded(
                  flex: 4,
                  child: _goodTypeWidget()),
            ],
            ),
          ));
  }

  _goodTypeWidget() {
    return  ListView.builder(
        physics: new NeverScrollableScrollPhysics(),
        itemCount: _goodTypeData.length,
        itemBuilder: (BuildContext context,
              int index) {
            // 列表item
            return Container(
                height: 60,
              child: Text(_goodTypeData[index].typeName,
                style: TextStyle(fontSize: 12),));
            });
//      SliverFixedExtentList(
//          itemExtent: 60,
//          delegate: SliverChildBuilderDelegate((BuildContext context,
//              int index) {
//            // 列表item
//            return Container(
////                height: 60,
//              child: Text(
//                _goodTypeData[index].typeName,
//                style: TextStyle(fontSize: 12),),
//            );
//          },
//            // 列表count
//            childCount: _goodTypeData.length,
//          ));
  }

  _goodListWidget() {
    return
//      Expanded(
//        child:
      Container(
          child: SliverFixedExtentList(
              itemExtent: 60,
              delegate: SliverChildBuilderDelegate((BuildContext context,
                  int index) {
                // 列表item
                return Container(
                  height: 60,
                  child: Text(
                    _goodTypeData[index].typeName,
                    style: TextStyle(fontSize: 12),),
                );
              },
                // 列表count
                childCount: _goodTypeData.length,
//            )
              )));
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
}