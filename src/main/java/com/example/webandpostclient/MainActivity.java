package com.example.webandpostclient;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    EditText etComment;
    Button btnPostReqParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //　各ウィジェットのインスタンスを取得
        this.etName = findViewById(R.id.PersonName);
        this.etComment = findViewById(R.id.etComment);
        this.btnPostReqParam = findViewById(R.id.btnPostReqParam);

        //　ボタンにclickイベント対応のリスナーを登録
        btnPostReqParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*--- イベントハンドラで通信処理を起動...別メソッドに切り出していたが今回はイベントハンドラの中で書く ---*/

                //　まずは名前とコメントの入力データをウィジェットから取得
                String name = etName.getText().toString();
                String comment = etComment.getText().toString();

                //　この二つのデータをコンストラクタ引数にして、通信用クラスをインスタンス生成
                BgPostReqParamTask bgPostReqParamTask = new BgPostReqParamTask(name,comment);

                //　通信クラスのインスタンスを別スレッドで起動してくれるExecutorインスタンスを取得
                ExecutorService es = Executors.newSingleThreadExecutor();

                //　スレッド開始
                es.submit(bgPostReqParamTask);
            }
        });
    }

    //　リクエストパラメーター入りのPostリクエストを実行する処理
    private  class BgPostReqParamTask implements Runnable{

        /*--リクエストパラメーターとして使うデータは、このクラスのコンストラクタ経由で受け取り、フィールドに保管し、run()--*/

        //　フィールド
        String name;
        String comment;

        //　コンストラクタ
        public BgPostReqParamTask(String name, String comment) {
            this.name = name;
            this.comment = comment;
        }

        @Override
        @WorkerThread
        public void run(){
            try {
                URL url = new URL("http://172.16.26.12:8080/WebAnd/post");                     //　接続用のWebアプリのサーブレット起動時URLパターンを指定
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();     //　接続用オブジェクトの取得
                httpURLConnection.setRequestMethod("POST");                                         //　今回パラメーターありなので、POSTリクエスト
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();                                                        //　ここで接続

                /*-----　↓ここから先がリクエストパラメーター送信処理　-----*/

                //　ネットワークを流れるデータはバイナリ（電圧のON/OFFだから）アプリからネットワーク（の先のサーブレット）へ書き出す出力ストリームは以下の方法で得る。
                OutputStream outputStream = httpURLConnection.getOutputStream();

                //　でも、リクエストパラメーターは値のエディットテキストから入力させたテキストデータ。これを出力ストリームに流せるようにするにはテキスト⇒バイナリに変換
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

                //　バッファリング機能を追加
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                //　ここでリクエストパラメーターを用意
                String reqParam = "name=" + this.name + "&" + "comment=" + this.comment;

                //　これをサーブレットへの出力ストリームに文字列のまま書き込む
                bufferedWriter.write(reqParam);
                bufferedWriter.flush();

                /*-----　↑ここまでがリクエスト処理　-----*/

                /*-----　↓ここから先がレスポンスの処理　-----*/

                //　通信処理おしまい。通信がうまくいったのか？いかなかったのか？⇒レスポンスコード（404など）で処理報告
                int resCode = httpURLConnection.getResponseCode();

                //　ここからがレスポンスを処理するコードを書くところ
                if (resCode == HttpURLConnection.HTTP_OK){
                    Log.v("**********","Postリクエスト通信は正常に処理されました");

                    //　URL接続から入力ストリームをとって
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    //　データをもらう
                    String retData = bufferedReader.readLine();     //　1行だけ読み込み

                    //　とりあえずログに出す
                    Log.v("**********",retData);

                    //　ここから後処理クラスを結果データつきでインスタンス化


                    //　ベルトコンベアの操作人に渡す

                }else{
                    Log.v("**********","Postリクエストは失敗しました");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}