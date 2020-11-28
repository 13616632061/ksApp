
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_shop/bean/good_info_bean.dart';
import 'package:flutter_shop/res/String/Strings.dart';

import 'add_subtract_widget.dart';

/**
 * 购物车
 */
class ShopCarWidget extends StatefulWidget {
  List<GoodInfo> goodsInfoData=[];


  ShopCarWidget({this.goodsInfoData});

  @override
  _ShopCarWidgetState createState() => _ShopCarWidgetState();
}

class _ShopCarWidgetState extends State<ShopCarWidget> {
  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: <Widget>[
          _topWidget(),
          Expanded(
            child: MediaQuery.removePadding(context: context,
                removeTop: true,
                removeBottom: true,
                child: _goodListWidget(widget.goodsInfoData)),)
        ],
      ),
    );
  }
}



/**
 * 购物车顶部
 */
_topWidget() {
  return Container(
    height: 48,
    color: Colors.white,
    padding: EdgeInsets.only(left: 15,right: 15),
    child: Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Text("已选商品",style: TextStyle(color: Colors.black),),
        //清空
        GestureDetector(
          
          child:Container(
            child:  Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: <Widget>[
                Icon(Icons.delete,color: Colors.grey,),
                Text("清空")
              ],
            ),
          ),
        )
        
      ],
    ),
  );

}

/**
 * 商品列表
 */
_goodListWidget(List<GoodInfo> _goodsInfoData) {
  return Container(
      padding: EdgeInsets.only(left: 10, right: 10),
      child: Column(
        children: <Widget>[
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
//                                              setState(() {
//                                                _goodsInfoData[index]
//                                                    .shopCarNum = value;
//                                                _shopCarNum();
//                                              });
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
