package com.example.myapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterAllFines extends RecyclerView.Adapter<AdapterAllFines.MyViewHolder>{
    private List<Fines> finesList;
    private Context context;

    public AdapterAllFines(List<Fines>finesList,Context context){
        this.finesList=finesList;
        this.context=context;
    }

    public void setFinesList(List<Fines> finesList) {
        this.finesList = finesList;

    }

    @Override
    public AdapterAllFines.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fines,parent,false);
        return new AdapterAllFines.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterAllFines.MyViewHolder holder, int position) {
        Fines fines = finesList.get(position);

        holder.txtFineId.setText(fines.getFineId());
        holder.txtFineStatus.setText(fines.getFineStatus());
        holder.txtFineReason.setText(fines.getFineReason());
        holder.txtFineAmount.setText(fines.getFineAmount());
        holder.txtDate.setText(fines.getDateFined());
    }

    @Override
    public int getItemCount() {
        int itemCount = finesList.size();
        Log.d("Adapter", "Fines List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardIndividualFines;
        TextView txtFineId,txtFineStatus,txtFineAmount,txtFineReason,txtDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFineAmount= itemView.findViewById(R.id.txtFineAmount);
            txtFineReason= itemView.findViewById(R.id.txtFineReason);
            cardIndividualFines=itemView.findViewById(R.id.cardIndividualFine);
            txtFineId= itemView.findViewById(R.id.txtFineId);
            txtFineStatus= itemView.findViewById(R.id.txtFineStatus);
            txtDate= itemView.findViewById(R.id.txtDateFined);
        }

    }
}



