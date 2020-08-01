package com.ui.entity;

/**
 *
 * Created by Administrator on 2017/3/9.
 */

public class GoodSort {
    private String name;
    private String id;
    private int choose_num;

    public GoodSort(String name, String id, int choose_num) {
        this.name = name;
        this.id = id;
        this.choose_num = choose_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getChoose_num() {
        return choose_num;
    }

    public void setChoose_num(int choose_num) {
        this.choose_num = choose_num;
    }

    @Override
    public String toString() {
        return "GoodSort{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", choose_num=" + choose_num +
                '}';
    }
}
