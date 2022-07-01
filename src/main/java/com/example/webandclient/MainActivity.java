package com.example.webandclient;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnConWeb = findViewById(R.id.btnConWeb);
        btnConWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConnect(); //接続開始メソッド
            }
        });
    }

    //　通信処理クラスを起動するメソッド...ひとつ前のアプリではonCreate()内からコールしたけど今回はボタンのイベントハンドラから呼んでみる
    @UiThread
    public void startConnect(){
        BgConnectTask task = new BgConnectTask();   //　通信クラスをインスタンス化。今回はベルトコンベアも操作人もいないので至ってシンプル
        ExecutorService es = Executors.newSingleThreadExecutor();   //別スレッドで動かしている
        es.submit(task);
    }

    //　今回はサーバー側からのダウンロードはとりあえず無いので、通信処理用の内部クラスと、それを起動するUI側メソッドのみでOK
    //　HandlerもLooperも無いので、通信処理のコンストラクタもとりあえずデフォルトのみでOK
    private class BgConnectTask implements Runnable{

        @Override
        @WorkerThread           //無くても動くけど、ワーカースレッドとしてちゃんと動くか？のコンパイラチェックが働かなくなるので注意
        public void run() {
            try {
                URL url = new URL("http://172.16.26.12:8080/WebAnd/connect");
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

                //　リクエストメソッドの設定
                httpCon.setRequestMethod("GET");

                //　リクエストヘッダ　⇒　必ずしなくてはいけないものではない。必要に応じて。
                httpCon.setRequestProperty("User-Agent","Android8.1 LAVIE TAB WebAndClient");
                httpCon.setRequestProperty("Connection" , "Keep-Alive");    //このヘッダ名と値の組み合わせは既定
                httpCon.setRequestProperty("Accept-Charset","UTF-8");

                httpCon.connect();  //接続

                httpCon.getResponseCode();  //接続完了まで実行

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}