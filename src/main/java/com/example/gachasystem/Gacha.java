package com.example.gachasystem;

public class Gacha {

    public static String gacha(){

        //ランダムな値を発生
        int val = new java.util.Random().nextInt(100);

        if(val < 10){
            return "スーパーレア";
        }

        if (val < 20){
            return "レア";
        }

        return "ノーマル";
    }
}
