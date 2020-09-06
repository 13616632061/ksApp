
class HomeDataBean {
  int status;
  List<NavClassifiedBean> reslut;

  HomeDataBean({this.status, this.reslut});

  HomeDataBean.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    if (json['reslut'] != null) {
      reslut = new List<NavClassifiedBean>();
      json['reslut'].forEach((v) {
        reslut.add(new NavClassifiedBean.fromJson(v));
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

class NavClassifiedBean {
  String id;
  String imagePath;
  String typeName;

  NavClassifiedBean({this.id, this.imagePath, this.typeName});

  NavClassifiedBean.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    imagePath = json['imagePath'];
    typeName = json['typeName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['imagePath'] = this.imagePath;
    data['typeName'] = this.typeName;
    return data;
  }
}