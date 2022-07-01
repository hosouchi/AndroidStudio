package com.example.image;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //これでメイン画面からimageViewを見つける
        ImageView imageView = findViewById(R.id.imageView);

        //画像表示
        imageView.setImageResource(R.drawable.apple);

        //画像削除
        imageView.setImageResource(0);      // 方法1
        imageView.setImageDrawable(null);   // 方法2
    }
}