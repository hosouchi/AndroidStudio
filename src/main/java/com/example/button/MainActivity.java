package com.example.button;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //activity_main.xmlから画面を作成してくれている

        //ボタンが押された時の処理を記述

        //①メイン画面からボタンを探す

        //これを記述することでbutton1の処理をjavaで記述できるようになる
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);

        //メイン画面からTextviewを見つける
        TextView textView = findViewById(R.id.textView);
        textView.setText("今日は曇りです");

        //②ボタンがクリックされた時の処理を書いていく
        //イベントハンドラー

        //ボタン1（左側）
        button1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //この中にボタンを押したときの処理を書けばOK

                        //トースト表示
                        Toast.makeText(MainActivity.this, "左側のボタンを押しました。", Toast.LENGTH_LONG).show();

                        //ログ出力
                        Log.d("***","左側のボタンを押しました");
                    }
                }
        );

        //ボタン2（右側）
        button2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //トースト表示
                        Toast.makeText(MainActivity.this, "右側のボタンを押しました。", Toast.LENGTH_LONG).show();
                    }
                }
        );

        /*--
        //ラムダ式で記入すると
        buton2.setOnClickListner(
                view -> Toast.makeText(MainActivity.this, "右側のボタンを押しました。", Toast.LENGTH_LONG).show();
        );
         */

        //左側のボタンが長押しされたときに動作するメソッド
        button1.setOnLongClickListener(
                new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View view) {
                        //長押ししたときに動作する処理をここに書く
                        Toast.makeText(MainActivity.this,"長押ししましたね",Toast.LENGTH_LONG).show();
                        return true;

                        //戻り値がfalseの時の動作→長押ししたときにOnclickメソッドが動作する
                        //戻り値がtrueの時の動作→Onclickメソッドが動作しない
                    }
                }
        );


    }
}