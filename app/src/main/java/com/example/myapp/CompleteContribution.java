package com.example.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class CompleteContribution extends Fragment {

    private TextView txtContDate,txtContAmount,txtNextCont;
    private Button btnDone,btnPrint;
    String chamaId,chamaName,chamaFlow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_complete_contribution, container, false);

        Bundle arguments = getArguments();
        if(arguments != null) {
            String id = arguments.getString("contribution_id");
            chamaId = String.valueOf(arguments.getInt("chama_id"));
            chamaFlow= arguments.getString("chamaFlow");
            chamaName= arguments.getString("chamaName");
            String amount = arguments.getString("contribution_amount");
            String date = arguments.getString("contribution_date");
            String next = arguments.getString("contribution_date");


            txtContAmount = view.findViewById(R.id.txtContAmount);
            txtContAmount.setText(amount);

            txtContDate = view.findViewById(R.id.txtContDate);
            txtContDate.setText(date);

            txtNextCont=view.findViewById(R.id.txtContNext);
            txtNextCont.setText(next);
        }

        btnDone=view.findViewById(R.id.btnContDone);
        btnPrint= view.findViewById(R.id.btnContPrint);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChamaHomeFragment chamaHomeFragment= new ChamaHomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("chamaId",chamaId);
                bundle.putString("chamaName",chamaName);
                bundle.putString("chamaFlow",chamaFlow);
                chamaHomeFragment.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, chamaHomeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }


}