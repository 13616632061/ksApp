class ShopInfoBean {
  int status;
  List<Reslut> reslut;

  ShopInfoBean({this.status, this.reslut});

  ShopInfoBean.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    if (json['reslut'] != null) {
      reslut = new List<Reslut>();
      json['reslut'].forEach((v) {
        reslut.add(new Reslut.fromJson(v));
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

class Reslut {
  String id;
  String imagePath;
  String name;
  String distance;
  int saleNum;
  String tips;

  Reslut(
      {this.id,
        this.imagePath,
        this.name,
        this.distance,
        this.saleNum,
        this.tips});

  Reslut.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    imagePath = json['imagePath'];
    name = json['name'];
    distance = json['distance'];
    saleNum = json['saleNum'];
    tips = json['tips'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['imagePath'] = this.imagePath;
    data['name'] = this.name;
    data['distance'] = this.distance;
    data['saleNum'] = this.saleNum;
    data['tips'] = this.tips;
    return data;
  }
}