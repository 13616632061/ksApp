import 'package:flutter/material.dart';

/**
 * 加减
 */
class AddSubtractWidget extends StatefulWidget {
  int nums = 0;
  ValueChanged<int> onChanged;

  AddSubtractWidget({ this.nums, this.onChanged});

  @override
  _AddSubtractWidget createState() => _AddSubtractWidget();

}

class _AddSubtractWidget extends State<AddSubtractWidget> {
  @override
  Widget build(BuildContext context) {
    return Container(
      child: Row(
        children: <Widget>[
          _sub(),
          _num(),
          _add()
        ]
        ,
      )
      ,
    );
  }

  /**
   * 数字
   */
  _num() {
    return Container(
      margin: EdgeInsets.only(left: 5, right: 5),
      child: widget.nums > 0 ? Text(
        "${widget.nums}", style: TextStyle(fontSize: 12),)
          : Container(),);
  }

  /**
   * 加
   */
  _add() {
    return GestureDetector(
      onTap: () {
        setState(() {
          widget.nums++;
          widget.onChanged(widget.nums);
        });
      },
      child: Container(
        height: 20,
        width: 20,
        alignment: Alignment.center,
        decoration: BoxDecoration(
            color: Colors.blue,
            borderRadius: BorderRadius.circular(50)
        ),
        child: Text('+', style: TextStyle(color: Colors.white, fontSize: 16),),
      ),
    );
  }

  /**
   * 减
   */
  _sub() {
    return widget.nums > 0 ? GestureDetector(
      onTap: () {
        setState(() {
          widget.nums--;
          widget.onChanged(widget.nums);
        });
      },
      child: Container(
        height: 20,
        width: 20,
        alignment: Alignment.center,
        decoration: BoxDecoration(
          color: Colors.white,
          border: Border.all(color: Colors.blue),
          borderRadius: BorderRadius.circular(50),
        ),
        child: Text('一', style: TextStyle(color: Colors.blue, fontSize: 10),),
      ),
    ) : Container();
  }
}