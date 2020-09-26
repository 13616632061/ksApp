import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_shop/bean/ShopInfoBean.dart';

class ShopInfoListWidget extends StatefulWidget {
  @override
  _ShopInfoListWidgetState createState() => _ShopInfoListWidgetState();

}

class _ShopInfoListWidgetState extends State<ShopInfoListWidget> {
  List<Reslut> datas = List();

  @override
  void initState() {
    super.initState();
    _initData();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: ListView.builder(
          shrinkWrap: true,
          itemCount: datas.length,
          itemBuilder: (context, index) {
            return _item(index);
          }),
    );
  }

  Widget _item(int index) {
    return Container(
      height: 100,
      child: Row(
        children: <Widget>[
          Image.network(datas[index].imagePath, height: 80, width: 80,),
          Container(
            child: Column(
              children: <Widget>[
                Text(datas[index].name),

              ],
            ),
          )
        ],
      ),
    );
  }

  _initData() async {
    Future<String> loadString = DefaultAssetBundle.of(context).loadString(
        "lib/res/data/shopInfo.json");
    loadString.then((String value) {
      setState(() {
        print("value" + value);
        ShopInfoBean response = ShopInfoBean.fromJson(jsonDecode(value));
        datas.addAll(response.reslut);
      });
    });
  }
}