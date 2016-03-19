package com.example.wb.calling.entry;

import cn.bmob.v3.BmobUser;

/**
 * Created by wb on 16/1/29.
 */
public class User extends BmobUser {

    //真实姓名
    private String name;
    //1 - student       0- teacher
    private Integer type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
