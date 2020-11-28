import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:io';
import 'dart:convert';
import 'package:flutter_shop/bean/HomeDataBean.dart';
import 'package:flutter_shop/page/ShopListPage.dart';
import 'package:flutter_shop/utils/NavigatorUtil.dart';

/**
 * 分类导航
 */
class NavClassifiedWidget extends StatefulWidget {
  @override
  _NavClassifiedWidgetState createState() => _NavClassifiedWidgetState();
}

class _NavClassifiedWidgetState extends State<NavClassifiedWidget> {
  List<NavClassifiedBean> datas = List();
  PageController _pageController = PageController();

  @override
  void initState() {
    super.initState();
    initDatas();
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {

      },
      child: Container(
        height: 200,
        child: PageView(
          scrollDirection: Axis.horizontal,
          reverse: false,
          controller: _pageController,
          children: _pageView(),

        ),
      ),
    );
  }

  Widget _item(List<NavClassifiedBean> datas, int index) {
    return GestureDetector(
      onTap: (){
        NavigatorUtil.push(context, ShopListPage());
      },
      child: Container(
        child: Column(
          children: <Widget>[
            Image.asset(datas[index].imagePath, height: 40, width: 40,),
            Text(datas[index].typeName, style: TextStyle(fontSize: 14),)
          ],
        ),
      ),
    );
  }

  Widget _pageViewItem(List<NavClassifiedBean> datas) {
    return Container(
        child: GridView.builder(
            itemCount: datas.length,
            gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 5),
            itemBuilder: (context, index) {
              return _item(datas, index);
            })
    );
  }

  List<Widget> _pageView() {
    List<Widget> list = List();
    int pageCount = datas.length ~/ 10; //取整
    int count = datas.length % 10; //取余
    if (pageCount > 0) {
      for (int i = 0; i < pageCount; i++) {
        list.add(_pageViewItem(datas.sublist(i * 10, (i + 1) * 10)));
      }
    }
    if (count > 0) {
      list.add(
          _pageViewItem(datas.sublist(datas.length - count, datas.length)));
    }
    return list;
  }

  initDatas() async {
    Future<String> loadString = DefaultAssetBundle.of(context).loadString(
        "lib/res/data/data.json");
    loadString.then((String value) {
      setState(() {
        print("value" + value);
        HomeDataBean response = HomeDataBean.fromJson(jsonDecode(value));
        datas.addAll(response.reslut);
      });
    });
  }
}

