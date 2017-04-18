package com.mottc.patrol.data.entity;

/**
 * Created with Android Studio
 * User: mottc
 * Date: 2017/4/17
 * Time: 15:16
 */
public class PatrolDate {
    private int year;
    private int month;
    private int day;

    public PatrolDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return year + "年" + month + "月" + day + "日";
    }
}
