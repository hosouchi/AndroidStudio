package com.example.chap6_dialogbasic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ダイアログを開くボタンのインスタンスを取得
        Button btn = findViewById(R.id.btn);

        //イベントリスナを登録
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new MyDialogFragment(); //自分が作ったダイアログ用フラグメントクラスをインスタンス化
                dialog.show(getSupportFragmentManager(),"dialog_basic");
            }
        });
    }
}