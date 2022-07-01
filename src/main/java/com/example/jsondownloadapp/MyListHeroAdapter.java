package com.example.jsondownloadapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyListHeroAdapter extends RecyclerView.Adapter<MyViewHolder> {
    // リストの元ネタをコンストラクタでもらって、フィールドに保管する
    private ArrayList<Hero> heroes;

    public MyListHeroAdapter(ArrayList<Hero> list){
        this.heroes = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hero, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvName.setText("勇者名:" + this.heroes.get(position).getName());
        holder.tvHp.setText("HP:"+String.valueOf(this.heroes.get(position).getHp()));
        holder.tvMp.setText("MP:" + String.valueOf(this.heroes.get(position).getMp()));
    }

    @Override
    public int getItemCount() {
        return this.heroes.size();
    }
}
