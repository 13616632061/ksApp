import 'package:flutter/material.dart';
import 'package:flutter_shop/widget/SearchWidget.dart';
import 'package:flutter_shop/bean/ShopInfoBean.dart';
import 'package:flutter_shop/utils/ScreenUtils.dart';
import 'package:flutter_shop/widget/SliverAppBarDelegateWidget.dart';
import 'package:flutter_shop/res/String/Strings.dart';
import 'package:flutter_shop/widget/GoodListWidget.dart';


/**
 * 商家详情页
 */
class ShopDetailPage extends StatefulWidget {
  final Reslut reslut;

  ShopDetailPage(this.reslut);

  @override
  _ShopDetailPageState createState() => _ShopDetailPageState();


}

class _ShopDetailPageState extends State<ShopDetailPage>
    with SingleTickerProviderStateMixin {

  ScrollController _scrollController = ScrollController();
  TabController tabController;

  @override
  void initState() {
    super.initState();
    tabController = TabController(length: 2, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: CustomScrollView(
        controller: _scrollController,
        slivers: <Widget>[
          _topBar(),
          _tabBar(),
          _body(),
        ],
      ),
    );
  }

  /**
   * 顶部topBar
   */
  _topBar() {
    return SliverAppBar(
      pinned: true,
      leading: Icon(Icons.arrow_back, color: Colors.white,),
      title: SearchWidget(),
      actions: <Widget>[
        //收藏
        IconButton(
            icon: Icon(Icons.favorite_border, color: Colors.white),
            onPressed: () {

            }),
        //分享
        IconButton(icon: Icon(Icons.share, color: Colors.white),
            onPressed: () {

            }),

      ],
      expandedHeight: 200,
      flexibleSpace: FlexibleSpaceBar(
        background: Container(
          height: 200,
          color: Colors.white,
          child: Stack(
            children: <Widget>[
              //背景图
              Image.network(
                widget.reslut.imagePath, height: 160,
                width: ScreenUtils.width,
                fit: BoxFit.fill,),
              //卡片信息
              _cardWidget()
            ],
          ),
        ),
      ),
    );
  }

  /**
   *tabBa
   */
  _tabBar() {
    return SliverPersistentHeader(
      pinned: true,
      delegate: SliverAppBarDelegateWidget(
        child: TabBar(
          //指示器颜色
          indicatorColor: Colors.orange,
          //指示器大小计算方式，TabBarIndicatorSize.label跟文字等宽,TabBarIndicatorSize.tab跟每个tab等宽
          indicatorSize: TabBarIndicatorSize.label,
          //选中label的Style
          labelStyle: TextStyle(
            fontWeight: FontWeight.bold, fontSize: 15, color: Colors.black,),
          //未选中label的Style
          unselectedLabelStyle: TextStyle(
              fontWeight: FontWeight.normal, fontSize: 15, color: Colors.black),
          controller: this.tabController,
          tabs: <Widget>[
            Tab(text: Strings.good), //商家
            Tab(text: Strings.shopInfo), //商家信息
          ],
        ),
      ),
    );
  }

  _body() {
    return SliverFillRemaining(
        child: TabBarView(
          controller: this.tabController,
          children: <Widget>[
            GoodListWidget(),
            Center(child: Text('Content of Profile')),
          ],
        )
    );
  }

  /**
   * 公告
   */
  _textTips() {
    return Text(
      widget.reslut.tips,
      maxLines: 1,
      overflow: TextOverflow.ellipsis,
      style: TextStyle(
          fontSize: 12, color: Colors.grey),);
  }

  /**
   * 店logo,圆角
   */
  _shopLogo() {
    return Container(
      height: 40,
      width: 40,
      decoration: BoxDecoration(
          borderRadius: BorderRadius.all(
              Radius.circular(5)),
          image: DecorationImage(
            image: NetworkImage(
                widget.reslut.imagePath),
            fit: BoxFit.fill,)

      ),
    );
  }

  /**
   * 店名 销量
   */
  _shopName() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        //店名
        Text(widget.reslut.name,
          maxLines: 1,
          overflow: TextOverflow.ellipsis,
          style: TextStyle(
              color: Colors.black, fontSize: 18),),
        //月售数量
        Text(
          "月售${widget.reslut.saleNum}",
          style: TextStyle(
              color: Colors.black, fontSize: 12),),
      ],);
  }

  /**
   * 商家信息
   */
  _shopInfoMsg() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Container(
          height: 60,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: <Widget>[
              //店名 销量
              _shopName(),
              //店logo,圆角
              _shopLogo()
            ],
          ),
        ),
        //公告
        _textTips()
      ],
    );
  }

  /**
   * 卡片信息
   */
  _cardWidget() {
    return Container(
      margin: EdgeInsets.only(left: 15, right: 15, top: 80, bottom: 15),
      padding: EdgeInsets.all(15),
      decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.all(Radius.circular(5)),
          boxShadow: [
            BoxShadow(
                blurRadius: 5,
                spreadRadius: 1,
                color: Colors.grey
            )
          ]
      ),
      child: _shopInfoMsg(), //商家信息
    );
  }

}

