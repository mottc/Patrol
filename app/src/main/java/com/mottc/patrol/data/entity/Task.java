package com.mottc.patrol.data.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created with Android Studio
 * User: mottc
 * Date: 2017/4/14
 * Time: 20:29
 */
@Entity
public class Task {
    @Id(autoincrement = true)
    private Long id;

    private String executor;
    private String announcer;
    private String time;
    private String location;
    private int status;
    @Generated(hash = 1813469754)
    public Task(Long id, String executor, String announcer, String time,
            String location, int status) {
        this.id = id;
        this.executor = executor;
        this.announcer = announcer;
        this.time = time;
        this.location = location;
        this.status = status;
    }
    @Generated(hash = 733837707)
    public Task() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getExecutor() {
        return this.executor;
    }
    public void setExecutor(String executor) {
        this.executor = executor;
    }
    public String getAnnouncer() {
        return this.announcer;
    }
    public void setAnnouncer(String announcer) {
        this.announcer = announcer;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

}
