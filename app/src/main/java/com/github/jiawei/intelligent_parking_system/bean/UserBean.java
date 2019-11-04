package com.github.jiawei.intelligent_parking_system.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

//import cn.bmob.v3.BmobObject;

@Table(name = "user")
public class UserBean /*extends BmobObject*/ {

    @Column(isId = true,name = "id")
    private int id;

    @Column(name = "account")
    private String account;

    @Column(name = "password")
    private String password;

    @Column(name = "image_url")
    private byte[] imageByte;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "hold_count")
    private int holdCount;

    @Column(name = "bill")
    private int bill;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getImageUrl() {
        return imageByte;
    }

    public void setImageUrl(byte[] imageUrl) {
        this.imageByte = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getHoldCount() {
        return holdCount;
    }

    public void setHoldCount(int holdCount) {
        this.holdCount = holdCount;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }
}
