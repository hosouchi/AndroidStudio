package com.example.chap6_dialogbutton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Activity activity = requireActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //ダイアログの設定
        return builder.setTitle("ダイアログの基本")
                .setMessage("AndroidはJavaで開発できますか？")
                .setIcon(R.drawable.ic_launcher_background)

                .setPositiveButton("はい",(dialog,which) -> {
                    Toast.makeText(activity, "正解です", Toast.LENGTH_SHORT).show();
                })

                .setNegativeButton("いいえ",(dialog,which) -> {
                    Toast.makeText(activity, "ミス！", Toast.LENGTH_SHORT).show();
                })

                .setNeutralButton("キャンセル",(dialog,which) -> {})
                .create();
    }
}
