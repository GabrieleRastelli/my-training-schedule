package com.example.mytrainingschedules.activities.mainactivity;

import android.os.Bundle;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.mainactivity.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;
import android.view.WindowManager;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        /* shows notification bar */
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_settings, R.id.navigation_premium).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onBackPressed(){ }

}