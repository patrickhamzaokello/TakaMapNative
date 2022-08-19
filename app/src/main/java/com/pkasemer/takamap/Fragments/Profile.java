package com.pkasemer.takamap.Fragments;

import android.content.Intent;
import android.net.Uri;
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
import com.pkasemer.takamap.Models.UserModel;
import com.pkasemer.takamap.R;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Profile extends Fragment {

    public Profile() {
        // Required empty public constructor
    }


    TextView full_name_text, username, user_details;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        if (!SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
//            finish();
            startActivity(new Intent(getContext(), LoginMaterial.class));
        }

        full_name_text = view.findViewById(R.id.full_name_text);
        username = view.findViewById(R.id.username);
        user_details = view.findViewById(R.id.user_details);

        UserModel user = SharedPrefManager.getInstance(getContext()).getUser();
        full_name_text.setText(user.getFullname());
        username.setText(user.getUsername());
        user_details.setText(user.getEmail() + " | " + user.getPhone());

        view.findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Before you go!")
                        .setContentText("You will be required to Sign in next time")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                SharedPrefManager.getInstance(getContext()).logout();
                            }
                        }).show();

            }
        });

        view.findViewById(R.id.about_us_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.takamap.com/about.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return view;
    }
}