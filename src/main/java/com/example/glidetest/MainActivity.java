package com.example.glidetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView ivDownloadImg = findViewById(R.id.ivDownloadImg);

        //　Glideの使い方
        Glide.with(ivDownloadImg).load("http://172.16.26.12:8080/kawaren/imgdownload?filename=2022-06-07-10-28-39.jpg").into(ivDownloadImg);

    }
}