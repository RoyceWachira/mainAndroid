package com.example.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MemberDetails extends Fragment {

    private TextView txtUserName,txtfname,txtRole,txtmail,txtphoneNumber,txtDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_member_details, container, false);

        Bundle arguments = getArguments();
        if(arguments != null) {
            String fname = arguments.getString("firstName");
            String uname = arguments.getString("username");
            String role = arguments.getString("chamaRole");
            String phoneNo = arguments.getString("phoneNumber");
            String mail = arguments.getString("email");
            String dateJoined = arguments.getString("dateJoined");

            txtUserName = view.findViewById(R.id.headUserName);
            txtUserName.setText(uname);

            txtfname = view.findViewById(R.id.txtfname);
            txtfname.setText(fname);

            txtRole = view.findViewById(R.id.headChamaRole);
            txtRole.setText(role);

            txtmail = view.findViewById(R.id.mail);
            txtmail.setText(mail);

            txtphoneNumber = view.findViewById(R.id.phone);
            txtphoneNumber.setText(phoneNo);

            txtDate= view.findViewById(R.id.joined);
            txtDate.setText(dateJoined);
        }

        return view;
    }
}