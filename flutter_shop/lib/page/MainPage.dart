import 'package:flutter/material.dart';
import 'package:flutter_shop/page/HomePage.dart';
import 'package:flutter_shop/page/OrderPage.dart';
import 'package:flutter_shop/page/MyPage.dart';
import 'package:flutter_shop/res/String/Strings.dart';


class MainPage extends StatefulWidget {
  @override
  _MainPageState createState() {
    return _MainPageState();
  }

}

class _MainPageState extends State<MainPage> {
  List<Widget> pages = [HomePage(), OrderPage(), MyPage()];
  int curIndex = 0;
  var selectNavColor = Colors.orange;
  var unSelectNavColor = Colors.grey;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: pages[curIndex],
        bottomNavigationBar: BottomNavigationBar(
          items: [
            _bottomNavigationBarItem(Icons.home, Strings.home, 0),
            _bottomNavigationBarItem(Icons.list, Strings.order, 1),
            _bottomNavigationBarItem(Icons.person, Strings.my, 2),
          ],
          currentIndex: curIndex,
          onTap: (index) {
            setState(() {
              curIndex = index;
            });
          },)
    );
  }

  /**
   * 底部tabItem
   */
  _bottomNavigationBarItem(IconData icon, String title, int index) {
    return BottomNavigationBarItem(
        icon: Icon(
          icon, color: curIndex == index ? selectNavColor : unSelectNavColor,),
        title: Text(title,
          style: TextStyle(
              color: curIndex == index ? selectNavColor : unSelectNavColor),));
  }
}