package com.pkasemer.takamap;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RootActivity extends AppCompatActivity {

    BottomNavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_activity);

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("My new title"); // set the top title
        String title = actionBar.getTitle().toString(); // get the title
        actionBar.hide();



        //Initialize Bottom Navigation View.
        navView = findViewById(R.id.bottomNav_view);


        //Pass the ID's of Different destinations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_cart, R.id.navigation_profile )
                .build();

        //Initialize NavController.
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navView, navController);
        navView.getOrCreateBadge(R.id.navigation_cart).setBackgroundColor(getResources().getColor(R.color.sweetRed));

        //updating cart counts

    }


    public void switchContent(int id, Fragment fragment, String fragmentname) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment, fragment.toString());
//        ft.addToBackStack(fragmentname);
        ft.commit();
    }








}