package com.lost.cuthair.model;

/**
 * Created by lost on 2016/4/12.
 */
public class SortModel {

    private String name;   //显示的数据
    private String sortLetters;  //显示数据拼音的首字母
    private String path; //显示图片的路径
    private String number; //每个人的id
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
