package com.example.chap6_dialogbasic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        //AlertDialogのBuilderインスタンス...表示するダイアログの各種設定を行っていくモノ
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        //ダイアログの設定
        //メソッドチェーンでのインスタンスへの属性追加...setXxxx()メソッドが、AlertDialogのBuilderインスタンスそのものを返す。
        builder.setTitle("ダイアログの基本")
                .setMessage("こんにちは、世界！")
                .setIcon(R.drawable.ic_launcher_background);

        //各種設定が終わったAlerDialogのBuilderインスタンスからダイアログを生成して返却
        return builder.create();
    }
}
