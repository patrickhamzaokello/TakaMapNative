package com.pkasemer.takamap.BottomDialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.pkasemer.takamap.ManageOrders;
import com.pkasemer.takamap.R;
import com.pkasemer.takamap.RootActivity;

import io.reactivex.annotations.NonNull;

//make sure your class extends RoundedBottomSheet.kt class created in second step.
public class ShowRoundDialogFragment extends BottomSheetDialogFragment {
    MaterialButton btnViewOrder,btnStartOrder;
    public static ShowRoundDialogFragment newInstance() {
        return new ShowRoundDialogFragment();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_round_dialog, container,
                false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnViewOrder = getView().findViewById(R.id.btnViewOrder);
        btnStartOrder = getView().findViewById(R.id.btnStartOrder);


        btnStartOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), RootActivity.class);
                startActivity(i);
            }
        });

        btnViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ManageOrders.class);
                startActivity(i);
            }
        });

    }
}