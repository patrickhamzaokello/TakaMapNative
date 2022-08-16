package com.pkasemer.takamap.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.pkasemer.takamap.MakeReport;
import com.pkasemer.takamap.Pickup;
import com.pkasemer.takamap.R;

public class Trash extends Fragment {

    public Trash() {
        // Required empty public constructor
    }

    MaterialButton request_pickup, make_report;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trash, container, false);
        request_pickup = view.findViewById(R.id.request_pickup);
        make_report = view.findViewById(R.id.request_report);

        request_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Pickup.class);
                getContext().startActivity(intent);
            }
        });
        make_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MakeReport.class);
                getContext().startActivity(intent);
            }
        });


        return view;
    }


}