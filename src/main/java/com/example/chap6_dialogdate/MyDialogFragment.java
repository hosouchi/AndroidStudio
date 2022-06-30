package com.example.chap6_dialogdate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MyDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar cal = Calendar.getInstance();  //とりあえず今日の日付でカレンダーを取得
        Activity act = requireActivity();       //このダイアログを呼び出す大元のアクティビティ（=MainActivityのこと）を取得

        DatePickerDialog dpdialog = new DatePickerDialog(

                act,                                         //呼び出し元のアクティビティ。Context型として使用の場合は、「データの一時保存先」
                new DatePickerDialog.OnDateSetListener() {  //日付が選択され、OKボタンが押された時のイベント処理。イベントリスナインスタンスを指定することで対応。
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        TextView tvSelDate = act.findViewById(R.id.tvSelDate);                       //日付を表示する、MainActivity側のテキストビューを取得
                        String date = year + "年" + (monthOfYear + 1 ) + "月" + dayOfMonth + "日";    //表示する日付の文字列表記
                    }
                },
                cal.get(Calendar.YEAR),         //ダイアログを開いた時の初期設定　年
                cal.get(Calendar.MONTH),        //ダイアログを開いた時の初期設定　月
                cal.get(Calendar.DAY_OF_MONTH)  //ダイアログを開いた時の初期設定　日
        );
        return dpdialog;
    }
}
