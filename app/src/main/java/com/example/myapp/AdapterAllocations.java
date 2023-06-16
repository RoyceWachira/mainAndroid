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

public class AdapterAllocations extends RecyclerView.Adapter<AdapterAllocations.MyViewHolder>{
    private List<Allocations> allocationsList;
    private Context context;

    public AdapterAllocations(List<Allocations>allocationsList,Context context){
        this.allocationsList=allocationsList;
        this.context=context;
    }

    public void setAllocationsList(List<Allocations> allocationsList) {
        this.allocationsList = allocationsList;

    }

    @Override
    public AdapterAllocations.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.allocations,parent,false);
        return new AdapterAllocations.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterAllocations.MyViewHolder holder, int position) {
        Allocations allocations = allocationsList.get(position);

        holder.txtallocationsId.setText(allocations.getAllocationId());
        holder.allocationAmount.setText(allocations.getAllocationAmount());
        holder.allocationDate.setText(allocations.getDate());

    }

    @Override
    public int getItemCount() {
        int itemCount = allocationsList.size();
        Log.d("Adapter", "Allocations List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardIndividualWithdrawals;
        TextView allocationAmount,txtallocationsId,allocationDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            allocationAmount= itemView.findViewById(R.id.allocationAmount);
            cardIndividualWithdrawals=itemView.findViewById(R.id.cardIndividualWithdrawals);
            txtallocationsId= itemView.findViewById(R.id.txtallocationsId);
            allocationDate= itemView.findViewById(R.id.allocationDate);
        }

    }
}



