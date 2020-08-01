package com.ui.entity;

/**
 * Created by Administrator on 2020/3/11.
 */

public class MemberSpecifications {


    /**
     * val : 1
     * give : 0
     * recharge_id : 4
     * gids : null
     * type : 1
     * is_show : yes
     */

    private String val;
    private String give;
    private String recharge_id;
    private Object gids;
    private String type;
    private String is_show;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getGive() {
        return give;
    }

    public void setGive(String give) {
        this.give = give;
    }

    public String getRecharge_id() {
        return recharge_id;
    }

    public void setRecharge_id(String recharge_id) {
        this.recharge_id = recharge_id;
    }

    public Object getGids() {
        return gids;
    }

    public void setGids(Object gids) {
        this.gids = gids;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_show() {
        return is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }
}
