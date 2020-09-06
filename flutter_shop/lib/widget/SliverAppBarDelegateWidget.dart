import 'dart:math';

import 'package:flutter/material.dart';

/**
 * 吸顶
 */
class SliverAppBarDelegateWidget extends SliverPersistentHeaderDelegate  {

  final TabBar child;

  SliverAppBarDelegateWidget({@required this.child});

  @override
  Widget build(BuildContext context, double shrinkOffset, bool overlapsContent) {
    return this.child;
  }

  @override
  double get maxExtent => this.child.preferredSize.height;

  @override
  double get minExtent => this.child.preferredSize.height;

  @override
  bool shouldRebuild(SliverPersistentHeaderDelegate oldDelegate) {
    return true;
  }

}