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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private Animation fabOpen;
    private Animation fabClose;
    private Animation fromBottom;
    private Animation toBottom;
    private boolean isOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        fab = view.findViewById(R.id.floatingButton);
        fab1 = view.findViewById(R.id.floatingButton2);
        fab2 = view.findViewById(R.id.floatingButton3);

        // Initialize animations
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);

        fab.setOnClickListener(v -> onFabClicked());

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateChamaFragment createChamaFragment= new CreateChamaFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, createChamaFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinChamaFragment joinChamaFragment = new JoinChamaFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, joinChamaFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });


        return view;
    }

    private void onFabClicked() {
        if (isOpen) {
            fab.startAnimation(fabClose);
            fab1.startAnimation(toBottom);
            fab2.startAnimation(toBottom);
            fab1.setClickable(false);
            fab2.setClickable(false);

            TextView label2 = getView().findViewById(R.id.label2);
            TextView label3 = getView().findViewById(R.id.label3);
            label2.setVisibility(View.INVISIBLE);
            label3.setVisibility(View.INVISIBLE);
        } else {
            fab.startAnimation(fabOpen);
            fab1.startAnimation(fromBottom);
            fab2.startAnimation(fromBottom);
            fab1.setClickable(true);
            fab2.setClickable(true);

            TextView label2 = getView().findViewById(R.id.label2);
            TextView label3 = getView().findViewById(R.id.label3);
            label2.setVisibility(View.VISIBLE);
            label3.setVisibility(View.VISIBLE);
        }
        isOpen = !isOpen;
    }


}