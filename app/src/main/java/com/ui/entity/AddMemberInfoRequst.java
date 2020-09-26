package com.ui.entity;

/**
 * Created by lyf on 2020/9/19.
 */

public class AddMemberInfoRequst {
    String member_id;//会员id
    String member_lv_custom_id;
    String member_lv_custom_key;
    String member_name;//会员名称
    String birthday;
    String remark;
    String is_require_pass;
    String mobile;//会员手机号
    String score;//积分
    String surplus;//余额
    String discount_rate;//折扣
    String fanxian;//返现

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_lv_custom_id() {
        return member_lv_custom_id;
    }

    public void setMember_lv_custom_id(String member_lv_custom_id) {
        this.member_lv_custom_id = member_lv_custom_id;
    }

    public String getMember_lv_custom_key() {
        return member_lv_custom_key;
    }

    public void setMember_lv_custom_key(String member_lv_custom_key) {
        this.member_lv_custom_key = member_lv_custom_key;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIs_require_pass() {
        return is_require_pass;
    }

    public void setIs_require_pass(String is_require_pass) {
        this.is_require_pass = is_require_pass;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }

    public String getFanxian() {
        return fanxian;
    }

    public void setFanxian(String fanxian) {
        this.fanxian = fanxian;
    }
}
