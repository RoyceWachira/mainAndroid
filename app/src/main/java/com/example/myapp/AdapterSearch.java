package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.MyViewHolder> {

    private List<Chamas> chamasList;
    private Context context;

    public AdapterSearch(List<Chamas>chamasList,Context context){
        this.chamasList=chamasList;
        this.context=context;
    }

    public void setChamasList(List<Chamas> chamasList) {
        this.chamasList = chamasList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chamas,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.chamaName.setText(chamasList.get(position).getChamaName());
        holder.chamaDescription.setText(chamasList.get(position).getChamaDescripion());
    }

    @Override
    public int getItemCount() {
        return chamasList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView chamaName,chamaDescription;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chamaName= itemView.findViewById(R.id.chamaName);
            chamaDescription= itemView.findViewById(R.id.chamaDescripion);
        }
    }
}
