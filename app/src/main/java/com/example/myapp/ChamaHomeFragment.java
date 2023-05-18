package com.example.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChamaHomeFragment extends Fragment {

    private FloatingActionButton fab,fab2,fab1,fab3,fab4,fab5;
    private Animation fabOpen;
    private Animation fabClose;
    private Animation fromBottom;
    private Animation toBottom;
    private boolean isOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chama_home, container, false);

        // Initialize views
        fab = view.findViewById(R.id.chamaFloatingButton);
        fab1 = view.findViewById(R.id.floatingContribute);
        fab2 = view.findViewById(R.id.floatingWithdraw);
        fab3= view.findViewById(R.id.floatingFine);
        fab4= view.findViewById(R.id.floatingLoan);
        fab5= view.findViewById(R.id.floatingMeet);

        // Initialize animations
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);


        fab.setOnClickListener(v -> onFabClicked());

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        return view;
    }

    private void onFabClicked() {
        if (isOpen) {
            fab.startAnimation(fabClose);
            fab1.startAnimation(toBottom);
            fab2.startAnimation(toBottom);
            fab3.startAnimation(toBottom);
            fab4.startAnimation(toBottom);
            fab5.startAnimation(toBottom);

            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);
            fab5.setClickable(false);

            TextView labelContribute = getView().findViewById(R.id.labelContribute);
            TextView labelWithdraw = getView().findViewById(R.id.labelWithdraw);
            TextView labelLoan = getView().findViewById(R.id.labelLoan);
            TextView labelFine = getView().findViewById(R.id.labelFine);
            TextView labelMeet = getView().findViewById(R.id.labelMeet);

            labelContribute.setVisibility(View.INVISIBLE);
            labelWithdraw.setVisibility(View.INVISIBLE);
            labelFine.setVisibility(View.INVISIBLE);
            labelLoan.setVisibility(View.INVISIBLE);
            labelMeet.setVisibility(View.INVISIBLE);

        } else {
            fab.startAnimation(fabOpen);
            fab1.startAnimation(fromBottom);
            fab2.startAnimation(fromBottom);
            fab3.startAnimation(fromBottom);
            fab4.startAnimation(fromBottom);
            fab5.startAnimation(fromBottom);

            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            fab5.setClickable(true);

            TextView labelContribute = getView().findViewById(R.id.labelContribute);
            TextView labelWithdraw = getView().findViewById(R.id.labelWithdraw);
            TextView labelLoan = getView().findViewById(R.id.labelLoan);
            TextView labelFine = getView().findViewById(R.id.labelFine);
            TextView labelMeet = getView().findViewById(R.id.labelMeet);

            labelContribute.setVisibility(View.VISIBLE);
            labelWithdraw.setVisibility(View.VISIBLE);
            labelFine.setVisibility(View.VISIBLE);
            labelLoan.setVisibility(View.VISIBLE);
            labelMeet.setVisibility(View.VISIBLE);
        }
        isOpen = !isOpen;
    }
}