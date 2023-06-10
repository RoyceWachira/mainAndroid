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

public class CompleteWithdrawal extends Fragment {

    private TextView txtWithReason,txtWithAmount;
    private Button btnDone,btnPrint;
    String chamaId,chamaFlow,chamaName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_complete_withdrawal, container, false);

        Bundle arguments = getArguments();
        if(arguments != null) {
            String id = arguments.getString("withdrawalId");
            chamaId = String.valueOf(arguments.getInt("chama_id"));
            chamaFlow= arguments.getString("chamaFlow");
            chamaName= arguments.getString("chamaName");
            String amount = arguments.getString("withdrawalAmount");
            String reason = arguments.getString("withdrawalReason");


            txtWithAmount = view.findViewById(R.id.txtWithAmount);
            txtWithAmount.setText(amount);

            txtWithReason = view.findViewById(R.id.txtWithReason);
            txtWithReason.setText(reason);

        }

        btnDone=view.findViewById(R.id.btnWithDone);
        btnPrint= view.findViewById(R.id.btnWithPrint);

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