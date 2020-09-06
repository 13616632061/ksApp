class GoodTypeBean {
  int status;
  List<GoodType> reslut;

  GoodTypeBean({this.status, this.reslut});

  GoodTypeBean.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    if (json['reslut'] != null) {
      reslut = new List<GoodType>();
      json['reslut'].forEach((v) {
        reslut.add(new GoodType.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    if (this.reslut != null) {
      data['reslut'] = this.reslut.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class GoodType {
  String id;
  String typeName;
  bool isSelect;

  GoodType({this.id, this.typeName,this.isSelect});

  GoodType.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    typeName = json['typeName'];
    isSelect = json['isSelect'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['typeName'] = this.typeName;
    data['isSelect'] = this.isSelect;
    return data;
  }
}