package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterIndividualFines extends RecyclerView.Adapter<AdapterIndividualFines.MyViewHolder>{
    private List<Fines> finesList;
    private Context context;

    public AdapterIndividualFines(List<Fines>finesList,Context context){
        this.finesList=finesList;
        this.context=context;
    }

    public void setFinesList(List<Fines> finesList) {
        this.finesList = finesList;

    }

    @Override
    public AdapterIndividualFines.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fines,parent,false);
        return new AdapterIndividualFines.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterIndividualFines.MyViewHolder holder, int position) {
        Fines fines = finesList.get(position);

        holder.bind(fines);
        holder.txtFineId.setText(fines.getFineId());
        holder.txtFineStatus.setText(fines.getFineStatus());
        holder.txtFineReason.setText(fines.getFineReason());
        holder.txtFineAmount.setText(fines.getFineAmount());
        holder.txtDate.setText(fines.getDateFined());

        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayFineFragment payFineFragment = new PayFineFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fineId", fines.getFineId());
                payFineFragment.setArguments(bundle);
                // Replace the current fragment with the new fragment
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.chamaFrameLayout, payFineFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
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
        Button pay;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pay= itemView.findViewById(R.id.btnPay);
            txtFineAmount= itemView.findViewById(R.id.txtFineAmount);
            txtFineReason= itemView.findViewById(R.id.txtFineReason);
            cardIndividualFines=itemView.findViewById(R.id.cardIndividualFine);
            txtFineId= itemView.findViewById(R.id.txtFineId);
            txtFineStatus= itemView.findViewById(R.id.txtFineStatus);
            txtDate= itemView.findViewById(R.id.txtDateFined);
        }
        public void bind(Fines fines) {
            if (fines.getFineStatus().equals("Not Paid") ) {
                pay.setVisibility(View.VISIBLE);
            } else {
                pay.setVisibility(View.GONE);
            }

        }
    }
}



