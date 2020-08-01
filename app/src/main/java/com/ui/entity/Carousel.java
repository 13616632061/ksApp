package com.ui.entity;

/**
 * Created by Administrator on 2020/3/10.
 */

public class Carousel {


    /**
     * id : 3
     * seller_id : 7006
     * image_id : b55ad8a77521ca898c2172158924a6da
     * url : http://img.zjzccn.com/upload/carousel/b5/5ad8a77521ca898c2172158924a6da.jpg
     * title : 1title
     * href : http://baidu.com
     * sort_num : 1
     * ctime : 1583407355
     * utime : 1583407355
     */

    private int id;
    private int seller_id;
    private String image_id;
    private String url;
    private String title;
    private String href;
    private int sort_num;
    private int ctime;
    private int utime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getSort_num() {
        return sort_num;
    }

    public void setSort_num(int sort_num) {
        this.sort_num = sort_num;
    }

    public int getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }

    public int getUtime() {
        return utime;
    }

    public void setUtime(int utime) {
        this.utime = utime;
    }
}
