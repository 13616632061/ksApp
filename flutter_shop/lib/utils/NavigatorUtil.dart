import 'package:flutter/material.dart';

/**
 * 页面跳转路由
 */
class NavigatorUtil {

  static push(context, Widget page) {
    Navigator.push(context, MaterialPageRoute(builder: (context) {
      return page;
    }));
  }
}