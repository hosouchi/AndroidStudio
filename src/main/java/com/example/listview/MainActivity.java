package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //リスト項目をArrayListとして準備
        ArrayList<String> data = new ArrayList<>();

        data.add("胡椒");
        data.add("ターメリック");
        data.add("コリアンダー");
        data.add("生姜");
        data.add("にんにく");
        data.add("サフラン");

        //配列アダプターを作成・ListViewに登録
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,data);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        //リスト項目をクリックした時の処理を定義
        list.setOnItemClickListener((av,view,position,id) ->{

            //リスト項目を取得＆クリックして削除
            adapter.remove((String) ((TextView)view).getText());
        });
    }
}