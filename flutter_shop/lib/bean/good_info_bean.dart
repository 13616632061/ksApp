class GoodsInfoBean {
  String id;
  String typeName;
  List<GoodInfo> goodInfo;

  GoodsInfoBean({this.id, this.typeName, this.goodInfo});

  GoodsInfoBean.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    typeName = json['typeName'];
    if (json['goodInfo'] != null) {
      goodInfo = new List<GoodInfo>();
      json['goodInfo'].forEach((v) {
        goodInfo.add(new GoodInfo.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['typeName'] = this.typeName;
    if (this.goodInfo != null) {
      data['goodInfo'] = this.goodInfo.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class GoodInfo {
  String goodId;
  String name;
  String photoUrl;
  String price;
  String initPrice;
  String tips;
  String saleNum;
  int shopCarNum;
  List<SpecificationsDetail> specificationsDetail;

  GoodInfo({this.goodId,
    this.name,
    this.photoUrl,
    this.price,
    this.initPrice,
    this.tips,
    this.saleNum,
    this.shopCarNum=0,
    this.specificationsDetail});

  GoodInfo.fromJson(Map<String, dynamic> json) {
    goodId = json['goodId'];
    name = json['name'];
    photoUrl = json['photoUrl'];
    price = json['price'];
    initPrice = json['initPrice'];
    tips = json['tips'];
    saleNum = json['saleNum'];
    shopCarNum = 0;
    if (json['specificationsDetail'] != null) {
      specificationsDetail = new List<SpecificationsDetail>();
      json['specificationsDetail'].forEach((v) {
        specificationsDetail.add(new SpecificationsDetail.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['goodId'] = this.goodId;
    data['name'] = this.name;
    data['photoUrl'] = this.photoUrl;
    data['price'] = this.price;
    data['initPrice'] = this.initPrice;
    data['tips'] = this.tips;
    data['saleNum'] = this.saleNum;
    data['shopCarNum'] = this.shopCarNum;
    if (this.specificationsDetail != null) {
      data['specificationsDetail'] =
          this.specificationsDetail.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class SpecificationsDetail {
  String specificationsTypeName;
  List<SpecificationsInfo> specificationsInfo;

  SpecificationsDetail({this.specificationsTypeName, this.specificationsInfo});

  SpecificationsDetail.fromJson(Map<String, dynamic> json) {
    specificationsTypeName = json['specificationsTypeName'];
    if (json['specificationsInfo'] != null) {
      specificationsInfo = new List<SpecificationsInfo>();
      json['specificationsInfo'].forEach((v) {
        specificationsInfo.add(new SpecificationsInfo.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['specificationsTypeName'] = this.specificationsTypeName;
    if (this.specificationsInfo != null) {
      data['specificationsInfo'] =
          this.specificationsInfo.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class SpecificationsInfo {
  String specificationsId;
  String specificationsPrice;
  String initSpecificationsPrice;
  String specificationsName;

  SpecificationsInfo({this.specificationsId,
    this.specificationsPrice,
    this.initSpecificationsPrice,
    this.specificationsName});

  SpecificationsInfo.fromJson(Map<String, dynamic> json) {
    specificationsId = json['specificationsId'];
    specificationsPrice = json['specificationsPrice'];
    initSpecificationsPrice = json['initSpecificationsPrice'];
    specificationsName = json['specificationsName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['specificationsId'] = this.specificationsId;
    data['specificationsPrice'] = this.specificationsPrice;
    data['initSpecificationsPrice'] = this.initSpecificationsPrice;
    data['specificationsName'] = this.specificationsName;
    return data;
  }
}