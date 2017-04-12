package com.liner.graduationproject.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/4/12/012.
 */

@Entity
public class User {


    @Id(autoincrement = true)
    private Long id;

    @Property
    private String userName;

    @Property
    private String password;

    @Property
    private String age;

    @Property
    private String sex;

    @Property
    private String birthday;

    @Property
    private String constellation;

    @Property
    private String residence;

    @Property
    private String introduce;

    @Generated(hash = 2078923483)
    public User(Long id, String userName, String password, String age, String sex,
            String birthday, String constellation, String residence,
            String introduce) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.birthday = birthday;
        this.constellation = constellation;
        this.residence = residence;
        this.introduce = introduce;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getConstellation() {
        return this.constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getResidence() {
        return this.residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getIntroduce() {
        return this.introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

   
}
