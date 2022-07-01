package com.example.chap8_2_intentforresult;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvSayHello = findViewById(R.id.tvsayHello);    //　テキスト
        Button btnToSUb = findViewById(R.id.btnToSub);          //　サブ画面へ戻るボタン

        //ランチャーインスタンスの生成...このインスタンスはボタンのonClick()イベントハンドラメソッドの中で使うので、それより前に生成しておく
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),           //　第1引数...IntentでよそのActivityをひらき、Intentで戻りデータをもらってくる場合はこれを指定
                new ActivityResultCallback<ActivityResult>() {                  //　第2引数...ActivityResultCallback<ActivityResult>クラスのインスタンスを指定。匿名クラスバージョン

                    //SubActivityから戻ってきたあとに実行したい処理を書いておく
                    @Override
                    public void onActivityResult(ActivityResult result) {       //　ActivityResultインスタンスの中に、SubActivityでの処理結果データが入ったIntentが入っている

                        //SubActivityでの処理はうまくいったか、どうか。
                        if (result.getResultCode() == RESULT_OK){
                            Intent backIntent = result.getData();                       //　戻りインテントをActivityResultインスタンスを取り出す
                            String name = backIntent.getStringExtra("name");      //　戻りインテントからnameの識別名でセットされた名前データを取り出す
                            tvSayHello.setText("こんにちは" + name + "さん！");

                        }else{
                            tvSayHello.setText("名前が正しく入力されませんでした");
                        }
                    }
                }
        );

        btnToSUb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //メインアクテビティからサブアクテビティへのインテントを作り、
                Intent intent = new Intent(MainActivity.this,SubActivity.class);

                //ランチャーインスタンスのlaunch()メソッドにインテントを指定すると、一連の処理が始まる
                launcher.launch(intent);

            }
        });
    }
}