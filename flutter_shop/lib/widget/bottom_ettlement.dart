import 'package:flutter/material.dart';
import 'package:flutter_shop/bean/good_info_bean.dart';
import 'package:flutter_shop/widget/shop_car_widget.dart';

/**
 * 底部去结算widget
 */
class BottomEttlementWidget extends StatefulWidget {
  int shopCarNum;
  List<GoodInfo> goodsInfoData = [];

  BottomEttlementWidget({this.goodsInfoData, this.shopCarNum});

  @override
  _BottomEttlementWidgetState createState() => _BottomEttlementWidgetState();
}

class _BottomEttlementWidgetState extends State<BottomEttlementWidget> {
  @override
  Widget build(BuildContext context) {
    return Container(
      height: 60,
      padding: EdgeInsets.only(left: 10, right: 10),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          Row(
            children: <Widget>[
              GestureDetector(
                onTap: () {
                  showModalBottomSheet(
                      context: context,
                      builder: (context) {
                        return Container(
                          margin: EdgeInsets.only(bottom: 60),
                          child: ShopCarWidget(
                            goodsInfoData: widget.goodsInfoData,
                          ),
                        );
                      });
                },
                child: Stack(
                  children: <Widget>[
                    Icon(
                      Icons.shopping_cart,
                      size: 45,
                      color: Colors.grey,
                    ),
                    Container(
                      margin: EdgeInsets.only(left: 35),
                      height: 15,
                      width: 15,
                      alignment: Alignment.center,
                      decoration: BoxDecoration(
                          color: Colors.red,
                          borderRadius: BorderRadius.circular(15)),
                      child: Text(
                        "${widget.shopCarNum}",
                        style: TextStyle(fontSize: 12, color: Colors.white),
                      ),
                    )
                  ],
                ),
              ),
              Text(
                "未选购商品",
                style: TextStyle(color: Colors.grey, fontSize: 14),
              ),
            ],
          ),
          Container(
            padding: EdgeInsets.only(left: 10, right: 10, top: 5, bottom: 5),
            decoration: BoxDecoration(
                color: Colors.grey,
                borderRadius: BorderRadius.all(Radius.circular(10))),
            child: Text(
              "去结算",
              style: TextStyle(color: Colors.white, fontSize: 14),
            ),
          )
        ],
      ),
    );
  }
}
