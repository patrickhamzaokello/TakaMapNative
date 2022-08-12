package com.pkasemer.takamap.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.pkasemer.takamap.HelperClasses.SharedPrefManager;
import com.pkasemer.takamap.LoginMaterial;
import com.pkasemer.takamap.ManageOrders;
import com.pkasemer.takamap.Models.UserModel;
import com.pkasemer.takamap.R;


public class Profile extends Fragment {

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);





        return view;
    }
}