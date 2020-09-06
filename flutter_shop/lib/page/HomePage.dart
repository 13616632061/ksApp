import 'package:flutter/material.dart';
import 'package:flutter_shop/widget/SearchWidget.dart';
import 'package:flutter_shop/widget/NavClassifiedWidget.dart';
import 'package:flutter_shop/widget/ShopInfoListWidget.dart';
import 'package:flutter_shop/bean/ShopInfoBean.dart';
import 'dart:convert';
import 'package:flutter_shop/widget/LineWidget.dart';
import 'package:flutter_shop/utils/NavigatorUtil.dart';
import 'package:flutter_shop/page/ShopDetailPage.dart';


/**
 *首页
 */
class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() {
    return _HomePageState();
  }

}

class _HomePageState extends State<HomePage> {
  ScrollController _scrollController = ScrollController();
  List<Reslut> datas = List();

  @override
  void initState() {
    super.initState();
    _initData();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
        child: Column(
          children: <Widget>[
            _TopWidget(),
            Expanded(
              child: CustomScrollView(
                controller: _scrollController,
                shrinkWrap: true,
                slivers: <Widget>[
                //分类
                  SliverFixedExtentList(
                    itemExtent: 200,
                    delegate: SliverChildBuilderDelegate((BuildContext context,
                        int index) {
                      // 列表item
                      return NavClassifiedWidget();
                    },
                      // 列表count
                      childCount: 1,
                    ),
                  ),
                  //商家列表
                  SliverFixedExtentList(
                    itemExtent: 101,
                    delegate: SliverChildBuilderDelegate((BuildContext context,
                        int index) {
                      // 列表item
                      return _item(index);
                    },
                      // 列表count
                      childCount: datas.length,
                    ),
                  )
                ],
              ),
            )

          ],
        ));
  }

  _TopWidget() {
    return Container(
      height: 80,
      color: Colors.orange,
      padding: EdgeInsets.only(bottom: 10),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.end,
        children: <Widget>[
          Container(
            margin: EdgeInsets.only(left: 10),
            child: Row(
              children: <Widget>[
                Icon(Icons.location_on, color: Colors.white,),
                Text("深圳市", style: TextStyle(color: Colors.white),),
                Icon(Icons.arrow_drop_down, color: Colors.white),
              ],
            ),
          )
          ,
          Expanded(child: SearchWidget())
        ],
      )
      ,
    );
  }

  Widget _item(int index) {
    return GestureDetector(
      behavior: HitTestBehavior.opaque,
      onTap: () {
        NavigatorUtil.push(context, ShopDetailPage(datas[index]));
      },
      child: Container(
        padding: EdgeInsets.only(left: 15, right: 15),
        height: 101,
        child: Column(
          children: <Widget>[
            Container(
              child: Row(
                children: <Widget>[
                  Image.network(
                    datas[index].imagePath, height: 80,
                    width: 80,
                    fit: BoxFit.cover,),
                  Expanded(child: Container(
                    height: 100,
                    padding: EdgeInsets.only(left: 10),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.spaceAround,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children
                          :
                      <Widget>[
                        //店名
                        Text(
                          datas[index]
                              .
                          name,
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                          style: TextStyle(color: Colors.black,
                              fontSize: 14,
                              fontWeight: FontWeight.bold),
                        ),

                        Container(
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: <Widget>[
                              //月售数量
                              Text(
                                "月售${datas[index].saleNum}",
                                style: TextStyle(fontSize: 12),),
                              //距离
                              Text(datas[index].distance,
                                  style: TextStyle(fontSize: 12)),
                            ],
                          ),
                        ),
                        //公告
                        Container(
                            padding: EdgeInsets.only(left: 5, right: 5),
                            decoration: BoxDecoration(
                                color: Colors.orange,
                                borderRadius: BorderRadius.all(
                                    Radius.circular(5))
                            ),
                            child: Text(
                              datas[index].tips,
                              maxLines: 1,
                              overflow: TextOverflow.ellipsis,
                              style: TextStyle(
                                  color: Colors.white, fontSize: 12),
                            )
                        ),
                      ],
                    ),
                  )
                  ),
                ],
              ),
            ),
            //分割线
            LineWidget()
          ],
        ),
      ),
    );
  }

  _initData() async {
    Future<String> loadString = DefaultAssetBundle.of(context).loadString(
        "lib/res/data/shopInfo.json");
    loadString.then((String value) {
      setState(() {
        ShopInfoBean response = ShopInfoBean.fromJson(jsonDecode(value));
        datas.addAll(response.reslut);
        print("value" + datas.length.toString());
      });
    });
  }
}