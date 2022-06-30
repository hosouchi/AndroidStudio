package com.example.chap6_dialogbutton;

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

        //ダイアログを表示ボタンをクリックしたときに実行されるコード

        //インスタンス取得
        Button btn = findViewById(R.id.btn);

        btn.setOnClickListener(view -> {
                DialogFragment dialog = new MyDialogFragment();
                dialog.show(getSupportFragmentManager(),"dialog_basic");
        });
    }
}