package com.example.chap8_2_intentdata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        //送られてきたインテントは、SubActivityが表示されたと同時にトースト表示

        Intent intent = getIntent();
//      String name = intent.getStringExtra("txtName");   ←名前・年齢を単品でインテントにputExtra()した場合。識別名を指定してデータを取り出す。

        HumanBean humanBean = (HumanBean) intent.getSerializableExtra("humanBean"); //キャストが必要
        String name = humanBean.getName();
        int age = humanBean.getAge();

        Toast.makeText(this, "こんにちは" + name + "さん。10年後の年齢は" + (age+10) + "歳ですね", Toast.LENGTH_SHORT).show();

        Button btnFinish = findViewById(R.id.btnBack);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}