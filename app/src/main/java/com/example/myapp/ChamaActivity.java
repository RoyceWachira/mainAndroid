package com.example.myapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapp.databinding.ActivityChamaBinding;

public class ChamaActivity extends AppCompatActivity {
    private String chamaId;
    private String chamaName;
    private String chamaFlow;

    ActivityChamaBinding chamaBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chamaBinding = ActivityChamaBinding.inflate(getLayoutInflater());
        setContentView(chamaBinding.getRoot());

        // Get the chamaId and chamaName values from the intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chamaId = extras.getString("chamaId");
            chamaName = extras.getString("chamaName");
            chamaFlow = extras.getString("chamaFlow");
        }

        ChamaHomeFragment chamaHomeFragment = new ChamaHomeFragment();
        Bundle arguments = new Bundle();
        arguments.putString("chamaId", chamaId);
        arguments.putString("chamaName", chamaName);
        arguments.putString("chamaFlow",chamaFlow);
        chamaHomeFragment.setArguments(arguments);
        replaceFragment(chamaHomeFragment);

        chamaBinding.bottomChamaNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.chamaHome:
                    replaceFragment(new ChamaHomeFragment());
                    break;
                case R.id.chamaNotifications:
                    replaceFragment(new ChamaNotificationsFragment());
                    break;
                case R.id.chamaSettings:
                    replaceFragment(new ChamaSettingsFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        Bundle arguments = fragment.getArguments();
        if (arguments == null) {
            arguments = new Bundle();
        }
        arguments.putString("chamaId", chamaId);
        arguments.putString("chamaName", chamaName);
        arguments.putString("chamaFlow", chamaFlow);
        fragment.setArguments(arguments);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.chamaFrameLayout, fragment);
        fragmentTransaction.commit();
    }







}

