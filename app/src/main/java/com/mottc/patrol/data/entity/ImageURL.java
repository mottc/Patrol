package com.mottc.patrol.data.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created with Android Studio
 * User: mottc
 * Date: 2017/4/20
 * Time: 15:25
 */
@Entity
public class ImageURL {
    @Id(autoincrement = true)
    private Long id;

    private String manager;
    private String url;
    @Generated(hash = 976692161)
    public ImageURL(Long id, String manager, String url) {
        this.id = id;
        this.manager = manager;
        this.url = url;
    }
    @Generated(hash = 661544148)
    public ImageURL() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getManager() {
        return this.manager;
    }
    public void setManager(String manager) {
        this.manager = manager;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
