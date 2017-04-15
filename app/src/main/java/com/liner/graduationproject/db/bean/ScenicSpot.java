package com.liner.graduationproject.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/4/13/013.
 */
@Entity
public class ScenicSpot {

    @Id(autoincrement = true)
    private Long id;

    //景点名称
    @Property
    private String scenicSpotName;

    //景点简介
    @Property
    private String description;

    @Generated(hash = 1069359151)
    public ScenicSpot(Long id, String scenicSpotName, String description) {
        this.id = id;
        this.scenicSpotName = scenicSpotName;
        this.description = description;
    }

    @Generated(hash = 738183326)
    public ScenicSpot() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScenicSpotName() {
        return this.scenicSpotName;
    }

    public void setScenicSpotName(String scenicSpotName) {
        this.scenicSpotName = scenicSpotName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    



}
