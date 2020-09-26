import 'package:flutter/material.dart';
import 'package:flutter_shop/widget/SearchWidget.dart';

/**
 * 商家topBar
 */
class ShopTopBarWidget extends StatefulWidget {
  @override
  _ShopTopBarWidgetState createState() => _ShopTopBarWidgetState();

}

class _ShopTopBarWidgetState extends State<ShopTopBarWidget> {
  @override
  Widget build(BuildContext context) {
    return Container(
      height: 80,
      alignment: Alignment.bottomCenter,
      child: Row(
        children: <Widget>[
          Icon(Icons.arrow_back_ios),
          SearchWidget(),
          Icon(Icons.collections),
          Icon(Icons.share),
        ],
      ),
    );
  }

}