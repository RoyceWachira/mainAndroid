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

public class AdapterIndividualLoans extends RecyclerView.Adapter<AdapterIndividualLoans.MyViewHolder>{
    private List<Loans> loansList;
    private Context context;

    public AdapterIndividualLoans(List<Loans>loansList,Context context){
        this.loansList=loansList;
        this.context=context;
    }

    public void setLoansList(List<Loans> loansList) {
        this.loansList = loansList;

    }

    @Override
    public AdapterIndividualLoans.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.loans,parent,false);
        return new AdapterIndividualLoans.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterIndividualLoans.MyViewHolder holder, int position) {
        Loans loans = loansList.get(position);

        holder.txtLoanId.setText(loans.getLoanId());
        holder.txtLoanStatus.setText(loans.getLoanStatus());

        holder.btnIndividualLoan.setOnClickListener(new View.OnClickListener() {
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
        int itemCount = loansList.size();
        Log.d("Adapter", "Loans List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardIndividualLoan;
        TextView txtLoanId,txtLoanStatus;
        Button btnIndividualLoan;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnIndividualLoan= itemView.findViewById(R.id.btnIndividualLoan);
            cardIndividualLoan=itemView.findViewById(R.id.cardIndividualLoan);
            txtLoanId= itemView.findViewById(R.id.txtLoanId);
            txtLoanStatus= itemView.findViewById(R.id.txtloanStatus);
        }

    }
}



