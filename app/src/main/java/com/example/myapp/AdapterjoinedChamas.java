package com.example.myapp;

import android.content.Context;
import android.content.Intent;
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

public class AdapterjoinedChamas extends RecyclerView.Adapter<AdapterjoinedChamas.MyViewHolder>{
    private List<Chamas> chamasList;
    private Context context;

    public AdapterjoinedChamas(List<Chamas>chamasList,Context context){
        this.chamasList=chamasList;
        this.context=context;
    }

    public void setChamasList(List<Chamas> chamasList) {
        this.chamasList = chamasList;

    }

    @Override
    public AdapterjoinedChamas.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chamas_joined,parent,false);
        return new AdapterjoinedChamas.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterjoinedChamas.MyViewHolder holder, int position) {

        holder.chamaName.setText(chamasList.get(position).getChamaName());
        holder.chamaDescription.setText(chamasList.get(position).getChamaDescription());

        holder.btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ChamaActivity.class);
                intent.putExtra("chamaId", chamasList.get(holder.getAdapterPosition()).getChamaId());
                intent.putExtra("chamaName", chamasList.get(holder.getAdapterPosition()).getChamaName());
                intent.putExtra("chamaFlow", chamasList.get(holder.getAdapterPosition()).getSystemFlow());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        int itemCount = chamasList.size();
        Log.d("Adapter", "Chamas List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView chamaName,chamaDescription;
        Button btnSet;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnSet= itemView.findViewById(R.id.setButton);
            cardView=itemView.findViewById(R.id.cardJoinChama);
            chamaName= itemView.findViewById(R.id.chamaName);
            chamaDescription= itemView.findViewById(R.id.chamaDescription);
        }
    }
}


