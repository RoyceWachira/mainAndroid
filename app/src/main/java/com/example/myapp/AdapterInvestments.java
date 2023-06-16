package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterInvestments extends RecyclerView.Adapter<AdapterInvestments.MyViewHolder> {
    private List<Investments> investmentsList;
    private Context context;

    public AdapterInvestments(List<Investments> investmentsList, Context context) {
        this.investmentsList = investmentsList;
        this.context = context;
    }

    public void setInvestmentsList(List<Investments> investmentsList) {
        this.investmentsList = investmentsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investments, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Investments investment = investmentsList.get(position);

        holder.txtInvestmentId.setText(investment.getInvestmentId());
        holder.txtInvestmentAmount.setText(investment.getInvestmentAmount());
        holder.txtInvestmentArea.setText(investment.getInvestmentArea());
        holder.txtExpectedReturns.setText(investment.getExpectedReturns());
        holder.txtInvestmentDuration.setText(investment.getInvestmentDuration());
    }

    @Override
    public int getItemCount() {
        return investmentsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtInvestmentId, txtInvestmentAmount, txtInvestmentArea, txtExpectedReturns, txtInvestmentDuration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInvestmentId = itemView.findViewById(R.id.txtinvId);
            txtInvestmentAmount = itemView.findViewById(R.id.txtinvamount);
            txtInvestmentArea = itemView.findViewById(R.id.txtinvarea);
            txtExpectedReturns = itemView.findViewById(R.id.txtexp);
            txtInvestmentDuration = itemView.findViewById(R.id.txtDue);
        }
    }
}
