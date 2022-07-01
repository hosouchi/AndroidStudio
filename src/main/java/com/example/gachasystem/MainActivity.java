package com.example.gachasystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //メイン画面からイメージを見つける
        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView3 = findViewById(R.id.imageView3);

        //メイン画面からボタンを見つける
        Button button_left = findViewById(R.id.button_left);    //1回ガチャ
        Button button_right = findViewById(R.id.button_right);  //3回ガチャ

        //画像を入れ込む
        TextView gachaPoint = findViewById(R.id.textView);

        /*--ガチャ用配列*/
        ImageView[] imageViews = {imageView1,imageView2,imageView3};

        //ノーマル
        int[] normal = {R.drawable.n1, R.drawable.n2, R.drawable.n3, R.drawable.n4};
        //レア
        int[] rare = {R.drawable.ra1, R.drawable.ra2};
        //スーパーレア
        int[] superRare = {R.drawable.sr1, R.drawable.sr2};

        //ガチャポイントの初期値
        gachaPoint.setText("5000");

        /*--1回ガチャ実装--*/
        button_left.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //1回ガチャボタンがタッチされるとメソッド動作
                        int val = Integer.parseInt(gachaPoint.getText().toString());

                        //TextViewにセットされている文字列がゲットできる。

                        //ガチャが500未満になり引けない場合はトースト表示
                        if (val < 500) {
                            Toast toast = Toast.makeText(MainActivity.this, "ガチャポイントがないのでガチャが引けません", Toast.LENGTH_LONG);
                        } else {
                            val = val - 500;

                            //3種類のレアリティのうちどれかが文字悦でくる
                            String result = Gacha.gacha();

                            if (result.equals("スーパーレア")) {
                                imageView2.setImageResource(superRare[new java.util.Random().nextInt(superRare.length)]);

                            } else if (result.equals("レア")) {
                                imageView2.setImageResource(rare[new java.util.Random().nextInt(rare.length)]);

                            } else {
                                imageView2.setImageResource(normal[new java.util.Random().nextInt(normal.length)]);
                            }
                        }
                        gachaPoint.setText(String.valueOf(val));
                    }
                }
        );

        /*--3回ガチャ--*/
        button_right.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int val = Integer.parseInt(gachaPoint.getText().toString());

                        if (val < 1500) {
                            Toast toast = Toast.makeText(MainActivity.this, "ガチャポイントがないのでガチャが引けません", Toast.LENGTH_LONG);
                        } else {
                            val = val - 500;

                            for (int i = 0; i < imageViews.length; i++) {

                                String result = Gacha.gacha();

                                if (result.equals("スーパーレア")) {

                                    imageViews[i].setImageResource(superRare[new java.util.Random().nextInt(superRare.length)]);

                                } else if (result.equals("レア")) {

                                    imageViews[i].setImageResource(rare[new java.util.Random().nextInt(rare.length)]);

                                } else {

                                    imageViews[i].setImageResource(normal[new java.util.Random().nextInt(normal.length)]);
                                }
                            }
                        }
                        gachaPoint.setText(String.valueOf(val));
                    }
                }
        );
    }
}