package com.example.chap8_2_intentforresult;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        // 他のアクティビティから呼び出されたなら、

        // レイアウト上のウィジェットをインスタンスとして取得し、
        Button btnBack = findViewById(R.id.btnBack);    //　戻るボタン
        EditText etName = findViewById(R.id.txtName);   //　名前入力

        // この画面を閉じるボタンが押されたなら、
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 名前入力用エディットテキストから名前を取得し
                String name = etName.getText().toString();

                //　戻り用インテントにいれこんで
                Intent intent = new Intent();   //戻り用インテントの場合、引数は不要

                //　もし、入力データが空文字またはnullだったなら・・・結果は良くない
                if (name == null || name.equals("")){

                    //　「処理がうまくいかなかったこと」を記録し
                    intent.putExtra("name","");
                    setResult(RESULT_CANCELED,intent);

                }else{

                    //　「処理がうまくいったこと」を記録し
                    intent.putExtra("name",name);
                    setResult(RESULT_OK,intent);

                }
                //　この画面を閉じる→後ろに隠れていた元のアクティビティが再表示・・・これを戻ると表現
                finish();
            }
        });
    }
}