package com.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by lyf on 2020/7/19.
 */

public class GoodInfoRespone {


    /**
     * response : {"status":"200","message":"ok","data":{"status":"success","msg":"当前分类","goods_info":[{"btn_switch_type":1,"name":"爆米花","good_limit":"0","good_stock":"0","ALTC":"1","increase":"1","unit_id":"1","unit":"yyy","product_id":null,"label":[],"price":"5.000","goods_id":"31876","store":"9907","buy_count":"64","marketable":"true","cost":"4.000","cat_name":"调味植物奶","py":"BMH","GD":"0","bncode":"0","img_src":""}],"total":[{"total":"1"}],"page":"1"}}
     */

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * status : 200
         * message : ok
         * data : {"status":"success","msg":"当前分类","goods_info":[{"btn_switch_type":1,"name":"爆米花","good_limit":"0","good_stock":"0","ALTC":"1","increase":"1","unit_id":"1","unit":"yyy","product_id":null,"label":[],"price":"5.000","goods_id":"31876","store":"9907","buy_count":"64","marketable":"true","cost":"4.000","cat_name":"调味植物奶","py":"BMH","GD":"0","bncode":"0","img_src":""}],"total":[{"total":"1"}],"page":"1"}
         */

        private String status;
        private String message;
        private DataBean data;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * status : success
             * msg : 当前分类
             * goods_info : [{"btn_switch_type":1,"name":"爆米花","good_limit":"0","good_stock":"0","ALTC":"1","increase":"1","unit_id":"1","unit":"yyy","product_id":null,"label":[],"price":"5.000","goods_id":"31876","store":"9907","buy_count":"64","marketable":"true","cost":"4.000","cat_name":"调味植物奶","py":"BMH","GD":"0","bncode":"0","img_src":""}]
             * total : [{"total":"1"}]
             * page : 1
             */

            private String status;
            private String msg;
            private String page;
            private List<GoodsInfoBean> goods_info;
            private List<TotalBean> total;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getPage() {
                return page;
            }

            public void setPage(String page) {
                this.page = page;
            }

            public List<GoodsInfoBean> getGoods_info() {
                return goods_info;
            }

            public void setGoods_info(List<GoodsInfoBean> goods_info) {
                this.goods_info = goods_info;
            }

            public List<TotalBean> getTotal() {
                return total;
            }

            public void setTotal(List<TotalBean> total) {
                this.total = total;
            }

            public static class GoodsInfoBean implements  Parcelable {
                /**
                 * btn_switch_type : 1
                 * name : 爆米花
                 * good_limit : 0
                 * good_stock : 0
                 * ALTC : 1
                 * increase : 1
                 * unit_id : 1
                 * unit : yyy
                 * product_id : null
                 * label : []
                 * price : 5.000
                 * goods_id : 31876
                 * store : 9907
                 * buy_count : 64
                 * marketable : true
                 * cost : 4.000
                 * cat_name : 调味植物奶
                 * py : BMH
                 * GD : 0
                 * bncode : 0
                 * img_src :
                 */

                private int btn_switch_type;
                private String name;
                private String good_limit;
                private String good_stock;
                private String ALTC;
                private String increase;
                private String unit_id;
                private String unit;
                private Object product_id;
                private String price;
                private String goods_id;
                private String store;
                private String buy_count;
                private String marketable;
                private String cost;
                private String cat_name;
                private String py;
                private String GD;
                private String bncode;
                private String img_src;
                private List<?> label;

                protected GoodsInfoBean(Parcel in) {
                    btn_switch_type = in.readInt();
                    name = in.readString();
                    good_limit = in.readString();
                    good_stock = in.readString();
                    ALTC = in.readString();
                    increase = in.readString();
                    unit_id = in.readString();
                    unit = in.readString();
                    price = in.readString();
                    goods_id = in.readString();
                    store = in.readString();
                    buy_count = in.readString();
                    marketable = in.readString();
                    cost = in.readString();
                    cat_name = in.readString();
                    py = in.readString();
                    GD = in.readString();
                    bncode = in.readString();
                    img_src = in.readString();
                }

                public static final Creator<GoodsInfoBean> CREATOR = new Creator<GoodsInfoBean>() {
                    @Override
                    public GoodsInfoBean createFromParcel(Parcel in) {
                        return new GoodsInfoBean(in);
                    }

                    @Override
                    public GoodsInfoBean[] newArray(int size) {
                        return new GoodsInfoBean[size];
                    }
                };

                public int getBtn_switch_type() {
                    return btn_switch_type;
                }

                public void setBtn_switch_type(int btn_switch_type) {
                    this.btn_switch_type = btn_switch_type;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getGood_limit() {
                    return good_limit;
                }

                public void setGood_limit(String good_limit) {
                    this.good_limit = good_limit;
                }

                public String getGood_stock() {
                    return good_stock;
                }

                public void setGood_stock(String good_stock) {
                    this.good_stock = good_stock;
                }

                public String getALTC() {
                    return ALTC;
                }

                public void setALTC(String ALTC) {
                    this.ALTC = ALTC;
                }

                public String getIncrease() {
                    return increase;
                }

                public void setIncrease(String increase) {
                    this.increase = increase;
                }

                public String getUnit_id() {
                    return unit_id;
                }

                public void setUnit_id(String unit_id) {
                    this.unit_id = unit_id;
                }

                public String getUnit() {
                    return unit;
                }

                public void setUnit(String unit) {
                    this.unit = unit;
                }

                public Object getProduct_id() {
                    return product_id;
                }

                public void setProduct_id(Object product_id) {
                    this.product_id = product_id;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getGoods_id() {
                    return goods_id;
                }

                public void setGoods_id(String goods_id) {
                    this.goods_id = goods_id;
                }

                public String getStore() {
                    return store;
                }

                public void setStore(String store) {
                    this.store = store;
                }

                public String getBuy_count() {
                    return buy_count;
                }

                public void setBuy_count(String buy_count) {
                    this.buy_count = buy_count;
                }

                public String getMarketable() {
                    return marketable;
                }

                public void setMarketable(String marketable) {
                    this.marketable = marketable;
                }

                public String getCost() {
                    return cost;
                }

                public void setCost(String cost) {
                    this.cost = cost;
                }

                public String getCat_name() {
                    return cat_name;
                }

                public void setCat_name(String cat_name) {
                    this.cat_name = cat_name;
                }

                public String getPy() {
                    return py;
                }

                public void setPy(String py) {
                    this.py = py;
                }

                public String getGD() {
                    return GD;
                }

                public void setGD(String GD) {
                    this.GD = GD;
                }

                public String getBncode() {
                    return bncode;
                }

                public void setBncode(String bncode) {
                    this.bncode = bncode;
                }

                public String getImg_src() {
                    return img_src;
                }

                public void setImg_src(String img_src) {
                    this.img_src = img_src;
                }

                public List<?> getLabel() {
                    return label;
                }

                public void setLabel(List<?> label) {
                    this.label = label;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(btn_switch_type);
                    dest.writeString(name);
                    dest.writeString(good_limit);
                    dest.writeString(good_stock);
                    dest.writeString(ALTC);
                    dest.writeString(increase);
                    dest.writeString(unit_id);
                    dest.writeString(unit);
                    dest.writeString(price);
                    dest.writeString(goods_id);
                    dest.writeString(store);
                    dest.writeString(buy_count);
                    dest.writeString(marketable);
                    dest.writeString(cost);
                    dest.writeString(cat_name);
                    dest.writeString(py);
                    dest.writeString(GD);
                    dest.writeString(bncode);
                    dest.writeString(img_src);
                }
            }

            public static class TotalBean {
                /**
                 * total : 1
                 */

                private String total;

                public String getTotal() {
                    return total;
                }

                public void setTotal(String total) {
                    this.total = total;
                }
            }
        }
    }
}
