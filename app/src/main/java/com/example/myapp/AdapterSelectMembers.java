package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterSelectMembers extends RecyclerView.Adapter<AdapterSelectMembers.MyViewHolder> {
    private List<Members> membersList;
    private Context context;
    private List<String> selectedMemberIds = new ArrayList<>();

    public AdapterSelectMembers(List<Members> membersList, Context context) {
        this.membersList = membersList;
        this.context = context;
    }

    public List<String> getSelectedMemberIds() {
        return selectedMemberIds;
    }

    public void setMembersList(List<Members> membersList) {
        this.membersList = membersList;
    }

    @Override
    public AdapterSelectMembers.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selectmembers, parent, false);
        return new AdapterSelectMembers.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterSelectMembers.MyViewHolder holder, int position) {
        Members member = membersList.get(position);

        holder.memberUserName.setText(member.getUserName());
        holder.fName.setText(member.getFirstName());
        holder.bind(member);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedMemberIds.contains(member.getUserId()));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedMemberIds.add(member.getUserId());
                } else {
                    selectedMemberIds.remove(member.getUserId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardMember;
        TextView memberUserName, fName;
        ImageView imageView;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardMember = itemView.findViewById(R.id.cardMember);
            memberUserName = itemView.findViewById(R.id.memberUsername);
            checkBox = itemView.findViewById(R.id.select);
            fName = itemView.findViewById(R.id.fName);
            imageView = itemView.findViewById(R.id.leader);
        }

        public void bind(Members member) {
            if (member.getChamaRole().equals("Chairperson") || member.getChamaRole().equals("Vice Chairperson") || member.getChamaRole().equals("Treasurer") || member.getChamaRole().equals("Secretary")) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }
}
