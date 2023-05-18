package com.example.myapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ProfileFragment extends Fragment {

    private Button editButton;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        editButton= view.findViewById(R.id.editButton);
        cardView= view.findViewById(R.id.cardLogout);

        String user= SharedPrefManager.getInstance(getContext()).getKeyUserName();

        TextView txtName = view.findViewById(R.id.txtName);
        txtName.setText(user);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileFragment editProfileFragment= new EditProfileFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, editProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Logout");
                    builder.setMessage("Are you sure you want to log out?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPrefManager.getInstance(getContext()).logout();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });


        return view;
    }

}