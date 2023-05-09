package com.example.myapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapp.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity   {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        binding.navigationView.setItemIconTintList(null);

        Objects.<NavigationView>requireNonNull(binding.navigationView).setNavigationItemSelectedListener(item -> {


            switch (item.getItemId()){
                case  R.id.navDashboard:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.navNotifications:
                    replaceFragment(new NotificationsFragment());
                    break;
                case R.id.navProfile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.navAbout:
                    replaceFragment(new AboutFragment());
                    break;
                case R.id.navSettings:
                    replaceFragment(new SettingsFragment());
                    break;
            }
            return true ;
        });


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case  R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true ;
        });

    }



    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }






}

