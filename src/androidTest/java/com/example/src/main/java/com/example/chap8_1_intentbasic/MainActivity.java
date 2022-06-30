package com.example.chap8_1_intentbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //ライフサイクルメソッドの全てをオーバーライドし、「○○メソッドが実行されました」とトースト表示せよ


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnToSub = findViewById(R.id.btnSend);
        btnToSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,SubActivity.class);     //MainActivityからSubActivityへのインテントを生成し
                startActivity(i);                                                           //Androidシステムへ投げつける
            }
        });

        Toast.makeText(this, "onCreate()メソッドが実行された", Toast.LENGTH_SHORT).show();
        Log.v("******","onCreate()メソッドが実行された");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart()メソッドが実行された", Toast.LENGTH_SHORT).show();
        Log.v("******","onStart()メソッドが実行された");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop()メソッドが実行された", Toast.LENGTH_SHORT).show();
        Log.v("******","onStop()メソッドが実行された");
        //もしかしたら、このまま画面復帰せずにアプリが落ちる可能性あり。→このタイミングで取り扱うデータの退避処理を実行したほうが良いかも...
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy()メソッドが実行された", Toast.LENGTH_SHORT).show();
        Log.v("******","onDestroy()メソッドが実行された");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause()メソッドが実行された", Toast.LENGTH_SHORT).show();
        Log.v("******","onPause()メソッドが実行された");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume()メソッドが実行された", Toast.LENGTH_SHORT).show();
        Log.v("******","onResume()メソッドが実行された");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart()メソッドが実行された", Toast.LENGTH_SHORT).show();
        Log.v("******","onRestart()メソッドが実行された");
    }
}