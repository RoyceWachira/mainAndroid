package com.example.myapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapp.databinding.ActivityChamaBinding;

public class ChamaActivity extends AppCompatActivity   {

    ActivityChamaBinding chamaBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chamaBinding= ActivityChamaBinding.inflate(getLayoutInflater());
        setContentView(chamaBinding.getRoot());
        replaceFragment(new ChamaHomeFragment());

        chamaBinding.bottomChamaNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case  R.id.chamaHome:
                    replaceFragment(new ChamaHomeFragment());
                    break;
                case R.id.chamaNotifications:
                    replaceFragment(new ChamaNotificationsFragment());
                    break;
                case R.id.chamaSettings:
                    replaceFragment(new ChamaSettingsFragment());
                    break;
            }
            return true ;
        });

    }



    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.chamaFrameLayout, fragment);
        fragmentTransaction.commit();

    }






}

