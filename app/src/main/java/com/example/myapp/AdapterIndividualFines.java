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

        holder.txtFineId.setText(fines.getFineId());
        holder.txtFineStatus.setText(fines.getFineStatus());

        holder.btnIndividualFine.setOnClickListener(new View.OnClickListener() {
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
        int itemCount = finesList.size();
        Log.d("Adapter", "Fines List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardIndividualFines;
        TextView txtFineId,txtFineStatus;
        Button btnIndividualFine;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnIndividualFine= itemView.findViewById(R.id.btnIndividualFine);
            cardIndividualFines=itemView.findViewById(R.id.cardIndividualFine);
            txtFineId= itemView.findViewById(R.id.txtFineId);
            txtFineStatus= itemView.findViewById(R.id.txtfineStatus);
        }

    }
}



