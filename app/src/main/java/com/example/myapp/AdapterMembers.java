package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterMembers extends RecyclerView.Adapter<AdapterMembers.MyViewHolder>{
    private List<Members> membersList;
    private Context context;

    public AdapterMembers(List<Members>membersList,Context context){
        this.membersList=membersList;
        this.context=context;
    }

    public void setMembersList(List<Members> membersList) {
        this.membersList = membersList;

    }

    @Override
    public AdapterMembers.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.members,parent,false);
        return new AdapterMembers.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterMembers.MyViewHolder holder, int position) {
        Members member = membersList.get(position);

        holder.memberUserName.setText(member.getUserName());
        holder.fName.setText(member.getFirstName());
        holder.bind(member);

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberDetails memberDetails = new MemberDetails();
                Bundle bundle = new Bundle();
                bundle.putString("username", member.getUserName());
                bundle.putString("firstName", member.getFirstName());
                bundle.putString("chamaRole", member.getChamaRole());
                bundle.putString("dateJoined", member.getDateJoined());
                bundle.putString("email", member.getEmail());
                bundle.putString("phoneNumber", member.getPhoneNumber());
                memberDetails.setArguments(bundle);

                // Replace the current fragment with the new fragment
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.chamaFrameLayout, memberDetails)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        int itemCount = membersList.size();
        Log.d("Adapter", "Members List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardMember;
        TextView memberUserName,fName;
        Button viewButton;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            viewButton= itemView.findViewById(R.id.viewButton);
            cardMember=itemView.findViewById(R.id.cardMember);
            memberUserName= itemView.findViewById(R.id.memberUsername);
            fName= itemView.findViewById(R.id.fName);
            imageView=itemView.findViewById(R.id.leader);
        }
        public void bind(Members member) {
            if (member.getChamaRole().equals("Chairperson") || member.getChamaRole().equals("Vice Chairperson") || member.getChamaRole().equals("Treasurer") || member.getChamaRole().equals("Secretary") ) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }
}


