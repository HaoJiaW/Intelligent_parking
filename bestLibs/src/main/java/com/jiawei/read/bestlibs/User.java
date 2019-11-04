package com.jiawei.read.bestlibs;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public String name;
    public int userId;
    public boolean isMale;

    public User(String name, int userId, boolean isMale) {
        this.name = name;
        this.userId = userId;
        this.isMale = isMale;
    }

}
