package com.example.myapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        holder.chamaDescription.setText(chamasList.get(position).getChamaDescription());
    }

    @Override
    public int getItemCount() {
        int itemCount = chamasList.size();
        Log.d("AdapterSearch", "Chamas List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView chamaName,chamaDescription;
        Button button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            button= itemView.findViewById(R.id.requestButton);
            cardView=itemView.findViewById(R.id.cardJoinChama);
            chamaName= itemView.findViewById(R.id.chamaName);
            chamaDescription= itemView.findViewById(R.id.chamaDescription);
        }
    }
}
