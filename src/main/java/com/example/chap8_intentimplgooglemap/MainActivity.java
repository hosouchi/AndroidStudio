package com.example.chap8_intentimplgooglemap;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
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

        //　各ウィジェットをインスタンス取得
        TextView tvLoc = findViewById(R.id.tvLoc);              //テキスト
        Button btnOpenGMap = findViewById(R.id.btnOpenGMap);    //GoogleMapをひらくボタン

        //　暗黙インテントでよそのアプリを開き、戻ってきたところでtvLocにクリップボード内の情報を貼り付けて見せるランチャーを生成
        //　（インテントのインスタンス自体はボタンのclickイベント処理の中で作る）
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(

                new ActivityResultContracts.StartActivityForResult(),   //第1引数
                new ActivityResultCallback<ActivityResult>() {          //第2引数

                    @Override
                    public void onActivityResult(ActivityResult result) {

                        /*--Androidシステムが全アプリ共通で利用できる、データの一時保存場所「クリップボード」を提供してくれているので、これを使用する--*/

                        //　1.ClipboardManagerという、クリップボードを管理しているインスタンスを取得　
                        ClipboardManager cbmanager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);      //ダウンキャストが必要

                        //　2.管理者の力を借りて、クリップボードの中のデータを「ClipData」インスタンスとして取得
                        ClipData loc = cbmanager.getPrimaryClip();

                        //　3.クリップデータの中の最初のデータをClipData.Itemインスタンスとして取得
                        ClipData.Item item = loc.getItemAt(0);

                        //　4.ClipData.Itemインスタンスを文字列に変換してTextViewへ表示
                        tvLoc.setText(item.getText().toString());

                        //改良：URLアドレスだけを抜き出して
                        int index = addData.indexOf("http")

                    }
                }
        );

        //　ボタンのクリックイベントで暗黙インテントを作り、ランチャーを起動する処理を実行
        btnOpenGMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //　GoogleMapをひらく暗黙的インテントを生成
                Intent intent = new Intent(Intent.ACTION_VIEW);          //　この時点では、「このインテントで開けるアプリを選びなさい」程度の情報しかない。
                intent.setPackage("com.google.android.apps.maps");       //　GoogleMapをアプリの識別名であるパッケージ名を指定することでご指名。インテントにセット。

                //　そのインテントを使って動くランチャーのlaunch()メソッドを実行
                launcher.launch(intent);
            }
        });
    }
}