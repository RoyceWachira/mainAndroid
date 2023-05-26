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

public class AdapterIndividualContributions extends RecyclerView.Adapter<AdapterIndividualContributions.MyViewHolder>{
    private List<Contributions> contributionsList;
    private Context context;

    public AdapterIndividualContributions(List<Contributions>contributionsList,Context context){
        this.contributionsList=contributionsList;
        this.context=context;
    }

    public void setContributionsList(List<Contributions> contributionsList) {
        this.contributionsList = contributionsList;

    }

    @Override
    public AdapterIndividualContributions.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contributions,parent,false);
        return new AdapterIndividualContributions.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterIndividualContributions.MyViewHolder holder, int position) {
        Contributions contributions = contributionsList.get(position);

        holder.txtContributionId.setText(contributions.getContributionId());
        holder.txtContributionDate.setText(contributions.getContributionDate());

        holder.btnIndividualContribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Context context = v.getContext();
//                Intent intent = new Intent(context, ChamaActivity.class);
//                intent.putExtra("username", membersList.get(holder.getAdapterPosition()).getUserName());
//                intent.putExtra("firstName", membersList.get(holder.getAdapterPosition()).getFirstName());
//                intent.putExtra("chamaRole", membersList.get(holder.getAdapterPosition()).getChamaRole());
//                intent.putExtra("dateJoined", membersList.get(holder.getAdapterPosition()).getDateJoined());
//                intent.putExtra("email", membersList.get(holder.getAdapterPosition()).getEmail());
//                intent.putExtra("phoneNumber", membersList.get(holder.getAdapterPosition()).getPhoneNumber());
//                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        int itemCount = contributionsList.size();
        Log.d("Adapter", "Contributions List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardIndividualContributions;
        TextView txtContributionId,txtContributionDate;
        Button btnIndividualContribution;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnIndividualContribution= itemView.findViewById(R.id.btnIndividualContribution);
            cardIndividualContributions=itemView.findViewById(R.id.cardIndividualContribution);
            txtContributionId= itemView.findViewById(R.id.txtContributionId);
            txtContributionDate= itemView.findViewById(R.id.txtContributionDate);
        }

    }
}



