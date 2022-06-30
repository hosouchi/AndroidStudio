package com.example.chap8_2_intentdata;

import java.io.Serializable;

public class HumanBean implements Serializable {

    /*--フィールド--*/
    String name;
    int age;

    /*--コンストラクタ--*/
    public HumanBean(){};
    public HumanBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /*--アクセサ--*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
