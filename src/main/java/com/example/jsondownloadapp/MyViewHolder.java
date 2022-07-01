package com.example.jsondownloadapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView tvName;
    TextView tvHp;
    TextView tvMp;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.tvName = this.view.findViewById(R.id.tvName);
        this.tvHp = this.view.findViewById(R.id.tvHp);
        this.tvMp = this.view.findViewById(R.id.tvMp);
    }
}
