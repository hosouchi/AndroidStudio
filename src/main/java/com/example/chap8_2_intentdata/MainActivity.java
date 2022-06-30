package com.example.chap8_2_intentdata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.SubscriptSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //データを取得
        EditText etName = findViewById(R.id.txtName);   //名前
        EditText etAgeStr = findViewById(R.id.txtAge);  //年齢
        Button btnToSub = findViewById(R.id.btnSend);   //サブ画面へのボタン

        /*--ボタンが押された時の処理--*/
        //入力データの取得→データの型変換→インテント生成→データいれる→Androidシステムへ投げる
        btnToSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etName.getText().toString();
                String ageStr = etAgeStr.getText().toString();  //　年齢データを文字列で取得
                int age = Integer.parseInt(ageStr);             //　文字列の年齢をint型へ変換

                //サブアクテビティを起動させるインテント
                Intent intent = new Intent(MainActivity.this, SubActivity.class);

//                intent.putExtra("name",name);
//                intent.putExtra("age",age);   これでもいいけど、SerializableインターフェースをかぶせたJavaBeansを作成してもOK

                HumanBean humanBean = new HumanBean(name,age);
                intent.putExtra("humanBean",humanBean);
                startActivity(intent);
            }
        });
    }
}