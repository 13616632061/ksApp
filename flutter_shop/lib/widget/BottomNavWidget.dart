import 'package:flutter/material.dart';
import 'package:flutter_shop/res/String/Strings.dart';


class BottomNavWidget extends StatefulWidget {

//  final callBack;

//  BottomNavWidget({this.callBack});

  @override
  _BottomNavWidgetState createState() {
    return _BottomNavWidgetState();
  }


}

class _BottomNavWidgetState extends State<BottomNavWidget> {
  var selectNavColor = Colors.orange;
  var unSelectNavColor = Colors.grey;

  int _curIndex = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      bottomNavigationBar: BottomNavigationBar(
        items: [
          _bottomNavigationBarItem(Icons.home, Strings.home, 0),
          _bottomNavigationBarItem(Icons.list, Strings.order, 1),
          _bottomNavigationBarItem(Icons.person, Strings.my, 2),
        ],
        currentIndex: _curIndex,
        onTap: (int index) {
          setState(() {
            _curIndex = index;
//            widget.callBack(index);
          });
        },
      ),
    );
  }

  /**
   * 底部tabItem
   */
  _bottomNavigationBarItem(IconData icon, String title, int index) {
    return BottomNavigationBarItem(
        icon: Icon(icon,
          color: _curIndex == index ? selectNavColor : unSelectNavColor,
        ),
        title: Text(title,
          style: TextStyle(fontSize: 15,
              color: _curIndex == index ? selectNavColor : unSelectNavColor),));
  }
}