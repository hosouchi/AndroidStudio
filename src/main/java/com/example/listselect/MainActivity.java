package com.example.listselect;

import static java.lang.String.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //リストビューを探す
        ListView list = findViewById(R.id.list);

        //リスト項目をArrayListとして準備
        ArrayList<String> data = new ArrayList<>();
        
        data.add("胡椒");
        data.add("ターメリック");
        data.add("コリアンダー");
        data.add("生姜");
        data.add("ニンニク");
        data.add("サフラン");

        //複数選択に対応したレイアウトを適用（simple_list_item_multiple_choice）
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_multiple_choice,data);
        list.setAdapter(adapter);
        
        //リストをすべて取り出して、選択された項目の値を取り出す処理
        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        //トースト用の文字列作成
                        String msg = "選択したのは";

                        //listView.getChildCount()は画面の表示数を取得。今回は6回ループする。
                        for (i = 0; i<list.getChildCount(); i++){

                            //CheckedTextView:チェック判定の機能付きview
                            //list.getChildAt():引数に渡された順番のリストビューの項目をゲットする
                            CheckedTextView checkedTextView = (CheckedTextView) list.getChildAt(i);

                            //チェックが入っていれば文字列を生成。チェックが入っていればtrue
                            if (checkedTextView.isChecked() == true){
                                msg += checkedTextView.getText() + ",";
                            }
                        }

                        //生成された文字列の最後にカンマが入ってしまうため、最後のカンマを取り除く
                        msg = msg.substring(0,msg.length()-1);

                        //トースト表示
                        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}