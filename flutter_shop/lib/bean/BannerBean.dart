class BannerBean {
  int status;
  Reslut reslut;

  BannerBean({this.status, this.reslut});

  BannerBean.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    reslut =
    json['reslut'] != null ? new Reslut.fromJson(json['reslut']) : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    if (this.reslut != null) {
      data['reslut'] = this.reslut.toJson();
    }
    return data;
  }
}

class Reslut {
  List<BannerInfo> bannerInfo;

  Reslut({this.bannerInfo});

  Reslut.fromJson(Map<String, dynamic> json) {
    if (json['bannerInfo'] != null) {
      bannerInfo = new List<BannerInfo>();
      json['bannerInfo'].forEach((v) {
        bannerInfo.add(new BannerInfo.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.bannerInfo != null) {
      data['bannerInfo'] = this.bannerInfo.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class BannerInfo {
  String id;
  String pictureUrl;
  String linkUrl;

  BannerInfo({this.id, this.pictureUrl, this.linkUrl});

  BannerInfo.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    pictureUrl = json['pictureUrl'];
    linkUrl = json['linkUrl'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['pictureUrl'] = this.pictureUrl;
    data['linkUrl'] = this.linkUrl;
    return data;
  }
}