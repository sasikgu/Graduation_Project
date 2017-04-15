package com.liner.graduationproject.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/4/14/014.
 */
@Entity
public class ReturnMsg {

    @Id(autoincrement = true)
    private Long id;

    @Property
    private String scenicSpot;

    @Property
    private String Addr;

    @Property
    private String classify;

    @Generated(hash = 671519371)
    public ReturnMsg(Long id, String scenicSpot, String Addr, String classify) {
        this.id = id;
        this.scenicSpot = scenicSpot;
        this.Addr = Addr;
        this.classify = classify;
    }

    @Generated(hash = 1075355788)
    public ReturnMsg() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScenicSpot() {
        return this.scenicSpot;
    }

    public void setScenicSpot(String scenicSpot) {
        this.scenicSpot = scenicSpot;
    }

    public String getAddr() {
        return this.Addr;
    }

    public void setAddr(String Addr) {
        this.Addr = Addr;
    }

    public String getClassify() {
        return this.classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }
}
