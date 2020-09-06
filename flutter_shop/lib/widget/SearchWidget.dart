import 'package:flutter/material.dart';

class SearchWidget extends StatefulWidget {
  @override
  _SearchWidgetState createState() => _SearchWidgetState();

}

class _SearchWidgetState extends State<SearchWidget> {
  @override
  Widget build(BuildContext context) {
    return Container(
      height: 25,
      margin: EdgeInsets.only(left: 10,
          right: 15),
      decoration: BoxDecoration(
          color
              : Colors.white,
          borderRadius: BorderRadius.all
            (Radius.circular(15))
      ),
      child: Container(
        padding: EdgeInsets.only(left: 15),
        child: Row(
          children: <Widget>[
            Icon(Icons.search, color: Colors.grey.shade400, size: 20,),
            Text("搜索", style: TextStyle(
                color: Colors.grey.shade400, fontSize: 12),)
          ],
        ),

      ),
    );
  }

}