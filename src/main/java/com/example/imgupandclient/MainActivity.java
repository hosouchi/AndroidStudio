package com.example.imgupandclient;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    //　12.画像をギャラリーから取得するランチャーのコールバックの中で得た画像Uriを他のメソッドでも利用できるようにMainActivityのフィールドに保管
    Uri imgUri;

    //　3.通信用クラスを内部クラスとして作る
    private class BgMultpartReqTask implements Runnable{

        String cont;        //　投稿者名
        String comment;     //　コメント
        byte[] imgdata;     //　写真のバイナリーデータ...MainActivity側では画像のUriインスタンスで保持しているので、どうにかしてbyte[]型データに変換して渡してもらう

        public BgMultpartReqTask(String cont, String comment, byte[] imgdata) {
            this.cont = cont;
            this.comment = comment;
            this.imgdata = imgdata;
        }

        @Override
        @WorkerThread
        public void run() {
            try {
                URL url = new URL("http://172.16.26.12:8080/kawaren/imgupload");                //　接続先を指定して
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();      //  接続用インスタンスを取得
                httpURLConnection.setRequestMethod("POST");                                          //　アップロードなのでPOSTリクエスト
                httpURLConnection.setDoInput(true);                                                  //　画像アップロード＝アプリから見て出力なので

                //　4.マルチパートデータを送る方法。「boundary=」で各データの「区切り文字」を指定することになっている。よく使われるのは「***」
                httpURLConnection.setRequestProperty("Content-Type" , "multipart/form-data;boundary=***");
                httpURLConnection.connect();    //　接続開始

                //　5.マルチパートデータを出力できるストリームの作成
                OutputStream os = httpURLConnection.getOutputStream();          //　プレーンなサーブレットに刺す出力ストロー
                BufferedOutputStream bos = new BufferedOutputStream(os);        //　ブッファリング追加
                DataOutputStream dos = new DataOutputStream(bos);               //　マルチパートデータを出力できるストリーム
                StringBuilder postData = new StringBuilder();                   //　マルチパートデータのうち、テキストデータ部分を「追記」で組み立てられるインスタンス

                //　6.テキストデータ（cont、comment）と画像データのヘッダ情報までをリクエストする文字列データとして組み立てていく

                /*--cont--*/
                postData.append("--***\r\n");                                           //　個々のリクエストパラメータ（マルチパート）の始まり
                postData.append("Content-Disposition: form-data; name=\"cont\"");       //　リクエストパラメーター名「cont」の指定
                postData.append("\r\n\r\n");                                            //　復帰改行をいれる
                postData.append(cont);                                                  //　リクエストパラメーターcontの値を追加
                postData.append("\r\n");                                                //　ひとつのリクエストパラメータ（マルチマート）の終わり

                /*--comment--*/
                postData.append("--***\r\n");
                postData.append("Content-Disposition: form-data; name=\"comment\"");
                postData.append("\r\n\r\n");
                postData.append(comment);
                postData.append("\r\n");

                //　7.画像データのうち、ヘッダ情報までを文字列データとして送る
                postData.append("--***\r\n");
                postData.append("Content-Disposition: form-data; name=\"imgfile\"; filename=\"dummy.jpg\"");    //　サーブレットのリクエストパラメーターの名前(imgfile)と同じにする
                postData.append("\r\n");
                postData.append("Content-Type:image/jpeg");     //　画像のMIMEタイプを追記
                postData.append("\r\n\r\n");                    //　復帰改行を２つでとりあえずテキストデータのリクエストパラメーターと画像のヘッダまでが終了

                //　8.ここまでのデータをサーブレットへの出力ストリームに書き込み
                dos.write(postData.toString().getBytes());      //　StringBuilderインスタンスをStringインスタンスにし、さらにbyte[](バイナリーデータ)にして書き込み

                //　9.画像データそのものを出力ストリームに書き込み
                dos.write(imgdata);

                //　10.全てのリクエストパラメーターの「終端」を表すデータを書き込み
                dos.write("\r\n--***--\r\n".getBytes());        //　「　\r\n--***--\r\n 」がリクエストパラメーターの終端記号
                dos.flush();                                    //　flushしてバッファの中の未送信データを絞り出す

                int resCode = httpURLConnection.getResponseCode();  //レスポンスコードの取得をもって、リクエストの送信とレスポンスの受け取りが完了
                if(resCode == HttpURLConnection.HTTP_OK){
                    Log.v("***","マルチパートのリクエストは完了しました");
                }else{
                    Log.v("****","リクエストは失敗しています");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //　ウイジェットのインスタンス取得
        ImageView ivPhoto = findViewById(R.id.ivPhoto);
        EditText etCont = findViewById(R.id.etCont);
        EditText etComment = findViewById(R.id.etComment);
        Button btnReq = findViewById(R.id.btnReq);

        //　1.（別画面・別アプリの）ギャラリーをひらいて、JPEG画像をもらってくるランチャーインスタンスの生成
        ActivityResultLauncher<String> launcher = registerForActivityResult(  //　ジェネリクスがStringなのは、画像MIMEタイプを文字列Stringで指定するから

                //　契約インスタンス
                new ActivityResultContracts.GetContent(),   //　ギャラリーをひらく契約

                //　コールバックインスタンス
                new ActivityResultCallback<Uri>(){         //　戻ってくるのはコンテンツ（画像）のUri
                    @Override
                    public void onActivityResult(Uri result){
                        ivPhoto.setImageURI(result);
                        Log.v("**********","選択した画像のURI：" + result.toString());

                        //　12.画像のUriをフィールドに保管
                        imgUri = result;
                    }
                }
        );

        //　2.imageビュー　ivphotoのロングタップ
        ivPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                launcher.launch("image/jpeg");  //　今回インテントは使わず、ギャラリーを開く契約とし、文字列でMIMEタイプを指定
                return true;
            }
        });

        //　11.btnReqが押されると、入力データからマルチパートデータとしてimageアップローダに送信する処理を実行
        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //　メインスレッド側に反映する通信結果データは無いので、LooperとHandlerは使わない。
                //　画面から3つのデータを取得
                String cont = etCont.getText().toString();                  //　投稿者名
                String comment = etComment.getText().toString();            //　コメント

                //　13.画像のUriから画像の生データを取得する処理
                byte[] imgdata = new byte[0];                                             //　画像のバイナリーデータを保管する変数をあらかじめ宣言
                ContentResolver contentResolver = getContentResolver();     //　アプリ間でUriをもとに実際のデータのやり取りを仲介してくれるもの。

                try (
                        InputStream inputStream = contentResolver.openInputStream(imgUri);  //　リゾルバは画像のUriから画像データを取得する入力ストリームを得られる
                        BufferedInputStream bis = new BufferedInputStream(inputStream);     //　バッファ機能を追加
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();           //　ここに書き込んだデータはtoByteArray()でbyte[]型データにしてくれる出力ストリーム
                ) {

                    //　入力ストリームから画像データを全て1バイトずつ読み込み出力ストリームに書き込む
                    int b;
                    while ((b = bis.read()) != -1) {
                        baos.write(b);
                    }

                    imgdata = baos.toByteArray();

                }catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                //　別スレッドで通信タスクを実行。コンストラクタ引数でリクエストデータを渡している。
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                BgMultpartReqTask bgMultpartReqTask = new BgMultpartReqTask(cont,comment,imgdata);
                executorService.submit(bgMultpartReqTask);
            }
        });
    }
}