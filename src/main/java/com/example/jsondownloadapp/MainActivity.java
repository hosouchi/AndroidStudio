package com.example.jsondownloadapp;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // まずはただ単にJSONデータをダウンロード⇒LogCatに出力 で確認。
    // ⇒これが出来たら、次はJSONデータのまま、メインスレッド（画面）のテキストビューに表示。
    // ⇒これも出来たら、次はJSONデータをArrayList<Hero>インスタンスに戻し、ListViewまたはRecyclerViewでリスト表示
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnDownloadJson = findViewById(R.id.btnDownloadJson);
        btnDownloadJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Looper bc = Looper.getMainLooper();                         // 「ベルトコンベア」（＝Looperインスタンス）を取得
                Handler op = HandlerCompat.createAsync(bc);                 // 「ベルトコンベア専属オペレータ」（＝Handlerインスタンス）を取得

                BgJsonDownloadTask task = new BgJsonDownloadTask(op);         // 通信用クラスに、オペレータを配置（コンストラクタ引数経由で渡す)
                ExecutorService es = Executors.newSingleThreadExecutor();
                es.submit(task);
            }
        });
    }

    // 後処理クラス：通信の結果、得たJSONデータを画面上に反映する
    private class ShowJsonToTvJson implements Runnable{
        String json;

        public ShowJsonToTvJson(String j){          // 結果データをコンストラクタで貰い、フィールドに保管
            this.json = j;
        }

        @Override
        @UiThread
        public void run() {
            TextView tvJson = findViewById(R.id.tvJson);    // MainActivityのフィールドを利用してもいいし、ここでウィジェットのインスタンスを取得してもよい
            tvJson.setText(this.json);

            // JSONデータをArrayList<Hero>に変換し、ListView/RecyclerViewで表示する処理
            // GSONライブラリの導入方法→解説済
            // Webアプリ側で作った、コレクション対象クラス（Hero.java）の取り込み→済
            // JSONデータ→ArrayList<Hero>インスタンスへの変換処理
            Gson gson = new Gson();
            Type myListHeroType = new TypeToken<ArrayList<Hero>>(){}.getType();     // 自作クラスのコレクションのクラス構造を解析
                                                                                    // …元の「クラス型」が解らないとインスタンスを復元できない
            ArrayList<Hero> heroes = gson.fromJson(this.json, myListHeroType);      // 解析情報をもとに、JSONデータから元のクラス型に復元

            // ちゃんと復元できたか、コレクション中のHeroインスタンスの数をトーストで表示して確認
            Toast.makeText(MainActivity.this, "heroes内のインスタンスの数:" + heroes.size(), Toast.LENGTH_LONG).show();

            // RecyclerViewに表示してみる
            RecyclerView rvHeroes = findViewById(R.id.rvHeroes);
            rvHeroes.setHasFixedSize(true);
            LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            rvHeroes.setLayoutManager(manager);
            RecyclerView.Adapter adapter = new MyListHeroAdapter(heroes);
            rvHeroes.setAdapter(adapter);
        }
    }

    // 通信用クラス
    private class BgJsonDownloadTask implements Runnable {
        Handler op;

        public BgJsonDownloadTask(Handler op) {              // オペレータを引数に貰って、フィールドに保管するコンストラクタを新設
            this.op = op;
        }

        @Override
        @WorkerThread
        public void run() {
            try {
                URL url = new URL("http://172.16.26.12:8080/jsonDownloadApp/json");
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setRequestMethod("GET");
                httpCon.setDoInput(true);

                httpCon.connect();
                int resCode = httpCon.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpCon.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    String json = sb.toString();
                    Log.v("*****", json);

                    // 後処理クラスに通信結果データを渡して、オペレータに処理してもらう→メインスレッドの画面上のテキストビューウィジェットに表示
                    ShowJsonToTvJson atoshori = new ShowJsonToTvJson(json); // 通信結果が入った、「小包」
                    op.post(atoshori);                                      // オペレータが小包をベルトコンベアに乗せた
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}