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

public class AdapterAllContributions extends RecyclerView.Adapter<AdapterAllContributions.MyViewHolder>{
    private List<Contributions> contributionsList;
    private Context context;

    public AdapterAllContributions(List<Contributions>contributionsList,Context context){
        this.contributionsList=contributionsList;
        this.context=context;
    }

    public void setContributionsList(List<Contributions> contributionsList) {
        this.contributionsList = contributionsList;

    }

    @Override
    public AdapterAllContributions.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contributions,parent,false);
        return new AdapterAllContributions.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterAllContributions.MyViewHolder holder, int position) {
        Contributions contributions = contributionsList.get(position);

        holder.txtContributionId.setText(contributions.getContributionId());
        holder.txtContributionDate.setText(contributions.getContributionDate());
        holder.txtContributionAmount.setText(contributions.getContributionAmount());

    }

    @Override
    public int getItemCount() {
        int itemCount = contributionsList.size();
        Log.d("Adapter", "Contributions List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardIndividualContributions;
        TextView txtContributionId,txtContributionDate,txtContributionAmount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContributionAmount= itemView.findViewById(R.id.contributionAmount);
            cardIndividualContributions=itemView.findViewById(R.id.cardIndividualContribution);
            txtContributionId= itemView.findViewById(R.id.txtContributionId);
            txtContributionDate= itemView.findViewById(R.id.txtContributionDate);
        }

    }
}



