package com.example.chap6_dialogdate;

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

        //各ウィジェット用変数、onCreate()メソッドの中で作成していますが、今後onCreate()以外からも使用する可能性あり→フィールド化するよ
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new MyDialogFragment();
                dialog.show(getSupportFragmentManager(),"datepicker");
            }
        });
    }
}