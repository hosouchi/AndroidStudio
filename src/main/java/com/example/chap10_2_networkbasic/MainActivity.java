package com.example.chap10_2_networkbasic;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    //　通信結果を反映するウィジェットの変数宣言はフィールドとして行う
    TextView tvHtml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //　インスタンスを取得
        tvHtml = findViewById(R.id.tvHtml);

        //★★★のスレッドで書いた開始！という処理を書いたメソッドをコール
        startHtmlDl();
    }

    //★★★
    //　通信処理を始める処理を単独のメソッドとして切り出して作っておく。...このメソッドはonCreate()メソッドから呼ぼう。このメソッドはメインスレッド側の処理。
    @UiThread   //　この処理はメインスレッド側の処理。「ベルトコンベア」と「操作人」の生成を行う。「メインスレッドに近い処理」をさせてますよという表明。＝保険。コンパイラにチェックさせている。
    public void startHtmlDl(){
        Looper beltconveyer = Looper.getMainLooper();                   // ベルトコンベアの生成。作られた場所がメインスレッド側なので、載せた荷物が届く先はメインスレッド側。
        Handler bcOperator = HandlerCompat.createAsync(beltconveyer);   // ベルトコンベアに不随して、操作人インスタンスを生成。

        //　通信処理クラスをインスタンス生成し、コンストラクタ引数にベルトコンベア操作人を指定することによって、「ベルトコンベアの送り元となるベルトコンベア操作人」を常駐させるイメージ
        BgHtmlDlTask task = new BgHtmlDlTask(bcOperator);

        //　これでベルトコンベアの先端はメインスレッド（MainActivity）、ベルトコンベアの根っこはワーカースレッド（通信処理クラス＝BgHtmlDlTaskクラス）になったということ

        //　通信処理クラスを別スレッドとして動かす準備...実践編でみたスレッドセーフなプログラミングを代行してくれるサービス
        ExecutorService es = Executors.newSingleThreadExecutor();

        //　通信処理クラスのインスタンスよ、別スレッドとして動け！
        es.submit(task);
    }

    //　「メインスレッド側」と「通信スレッド」のラベルをメソッドごとに貼り付ける（アノテーション）→JVM（正確には違うのだけど）に教える

    //　2.通信用スレッドはメインスレッドの内部クラスとして作る
    private class BgHtmlDlTask implements Runnable{

        private final Handler bcOperator;   //ベルトコンベアに結果データ入りのPostExecutorインスタンスを載せてくれる操作人を宣言。いつでも雇用できるように準備しておく。

        //　コンストラクタでHandlerインスタンスをもらってくる⇒フィールドへ保管
        public BgHtmlDlTask(Handler h){
            this.bcOperator = h;
        }

        //　通信処理。ワーカースレッドである旨、ARTに通知⇒コンパイラがUIThread（画面を操作する処理）とは別のスレッドとして働けるか、チェックしてくれるようになる。
        @Override
        @WorkerThread
        public void run(){

            //　実際のHTMLデータのダウンロード処理
            try {
                URL url = new URL("http://172.16.26.12:8080");  //　ネットワークアクセサ用高水準API

                //　サーバーに接続するためのインスタンスをURLインスタンスを使って取得
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

                //　Http接続用インスタンスに各種設定を追加
                httpCon.setRequestMethod("GET");    //　今回はサーバー側リソース（TomcatトップページのHTML)をもらうのでGETリクエストメソッドとします
                httpCon.setDoInput(true);           //　接続先からの入力を得られるようにする設定...これはダウンロードするということ。入力ストリームを得られる設定
                httpCon.connect();                  //　以上の設定で、接続を開始

                //　ここから入力ストリームの作成
                InputStream is = httpCon.getInputStream();   //　ベースとなる入力ストリームはHttp接続インスタンスからとれる。ネットを流れるデータは電圧のON/OFF...バイナリ
                InputStreamReader isr = new InputStreamReader(is,"UTF-8");  //　ダウンロードするデータは、元テキスト。バイナリ⇒テキスト変換フィルタ
                BufferedReader br = new BufferedReader(isr);                          //　バッファリング機能を付加⇒行単位で読み込み可能に

                //　1行ずつ読み込んだ文字列データを追記できるオブジェクトを用意
                StringBuilder sb = new StringBuilder();
                String line;

                //　入力ストリームから1行HTMLデータを読み込み...これがnullでないなら
                while((line = br.readLine())!= null){
                    sb.append(line);    //　読み込んだ1行を追記
                }

                //　ここまで処理が到達したということは、Tomcatトップページの全てのHTMLデータをStringBuilderインスタンスに書き込めたということ
                String html = sb.toString();    //　HTMLテキストデータに変換...最終的な結果データ...画面のtextビューに表示したい

                //　結果データを後処理クラスのインスタンスに含める
                PostExecutor pe = new PostExecutor(html);

                //　ベルトコンベアの操作人に、PostExecutorインスタンスをベルトコンベアに乗せてもらう...そのうちメインスレッドに届いて、適当なタイミングで処理してくれるはず
                bcOperator.post(pe);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //　1.通信で得られた結果データを画面上のウィジェットに反映する処理も、別スレッド・内部クラスとして作る
    private class PostExecutor implements Runnable{

        private String html;    //　通信処理の結果、得られるデータを保存するフィールド。今回はHTMLなので、String型として。

        //　結果データはコンストラクタの引数として渡してもらおう
        public PostExecutor(String kekka){
            this.html = kekka;
        }

        //　run()メソッドのオーバーライド。処理結果データを、画面上のウィジェット用インスタンスにセットする。この処理もスレッドプログラミング。
        //　この処理はメインスレッド側の「都合の良い時、タイミングがあったとき」に実行される。いつ実行されるか解らない。だから別スレッドとして作らざるを得ない。
        //　さらに、この処理は「ユーザーインターフェース（画面）に絡む処理」なので、ART（JVMのGoogle版）にその旨教えてやるとコンパイラが妥当なプログラミングになっているかチェック
        @Override
        @UiThread
        public void run(){
            tvHtml.setText(this.html);
        }
    }
}