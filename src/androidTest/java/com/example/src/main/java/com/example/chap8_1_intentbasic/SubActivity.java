package com.example.chap8_1_intentbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ボタンを押したら戻ると言っていますが、実際はSubActivityを終わらせて閉じる→SubActivityの後ろに隠れていたMainActivityが出てくるだけの話
                finish();
            }
        });
    }
}