package com.example.currentdatetime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //メイン画面から探す
        Button button1 = findViewById(R.id.button1);
        TextView textView = findViewById(R.id.textView);

        //ボタンがクリックされた時の処理
        button1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //現在時刻を取得
                        Date date = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                        textView.setText(format.format(date));
                        Toast.makeText(MainActivity.this,"現在の時刻表示しました。",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}