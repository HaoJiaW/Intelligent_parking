package com.github.jiawei.intelligent_parking_system.event;

public class RefreshUserDataEvent {

    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public RefreshUserDataEvent(String number) {
        this.number = number;
    }
}
