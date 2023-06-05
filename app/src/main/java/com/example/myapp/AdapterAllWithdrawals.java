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

public class AdapterAllWithdrawals extends RecyclerView.Adapter<AdapterAllWithdrawals.MyViewHolder>{
    private List<Withdrawals> withdrawalsList;
    private Context context;

    public AdapterAllWithdrawals(List<Withdrawals>withdrawalsList,Context context){
        this.withdrawalsList=withdrawalsList;
        this.context=context;
    }

    public void setWithdrawalsList(List<Withdrawals> withdrawalsList) {
        this.withdrawalsList = withdrawalsList;

    }

    @Override
    public AdapterAllWithdrawals.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.withdrawals,parent,false);
        return new AdapterAllWithdrawals.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterAllWithdrawals.MyViewHolder holder, int position) {
        Withdrawals withdrawals = withdrawalsList.get(position);

        holder.txtWithdrawalsId.setText(withdrawals.getWithdrawalId());
        holder.txtWithdrawalsReason.setText(withdrawals.getWithdrawalReason());
        holder.txtWithdrawalsAmount.setText(withdrawals.getWithdrawalAmount());

    }

    @Override
    public int getItemCount() {
        int itemCount = withdrawalsList.size();
        Log.d("Adapter", "Withdrawals List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardIndividualWithdrawals;
        TextView txtWithdrawalsId,txtWithdrawalsReason,txtWithdrawalsAmount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWithdrawalsAmount= itemView.findViewById(R.id.withdrawalsAmount);
            cardIndividualWithdrawals=itemView.findViewById(R.id.cardIndividualWithdrawals);
            txtWithdrawalsId= itemView.findViewById(R.id.txtWithdrawalsId);
            txtWithdrawalsReason= itemView.findViewById(R.id.txtWithdrawalsReason);
        }

    }
}



