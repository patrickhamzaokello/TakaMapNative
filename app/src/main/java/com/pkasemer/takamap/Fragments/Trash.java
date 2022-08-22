package com.pkasemer.takamap.Fragments;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pkasemer.takamap.Apis.TakaApiBase;
import com.pkasemer.takamap.Apis.TakaApiService;
import com.pkasemer.takamap.HelperClasses.SharedPrefManager;
import com.pkasemer.takamap.Models.PickupResponse;
import com.pkasemer.takamap.Models.RequestPickupModel;
import com.pkasemer.takamap.Models.UserModel;
import com.pkasemer.takamap.R;

import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Trash extends Fragment {

    public Trash() {
        // Required empty public constructor
    }

    MaterialButton request_pickup, make_report;
    View view;
    TextInputEditText inputTextFullname,inputTextPhone,inputTextUserAddress,editTextDescription;
    Button submit_btn;
    ProgressBar progressBar;
    RequestPickupModel requestPickupModel = new RequestPickupModel();
    private TakaApiService takaApiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_trash, container, false);
        submit_btn = view.findViewById(R.id.submit_btn);
        inputTextFullname = view.findViewById(R.id.inputTextFullname);
        inputTextPhone = view.findViewById(R.id.inputTextPhone);
        inputTextUserAddress = view.findViewById(R.id.inputTextUserAddress);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        progressBar = view.findViewById(R.id.main_progress);
        progressBar.setVisibility(View.GONE);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate_submit_form();
            }
        });
        takaApiService = TakaApiBase.getClient(getContext()).create(TakaApiService.class);

        return view;
    }

    private void validate_submit_form() {
        String full_name = inputTextFullname.getText().toString().trim();
        String phone = inputTextPhone.getText().toString().trim();
        String address = inputTextUserAddress.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(full_name)) {
            inputTextFullname.setError("Provide Full name");
            inputTextFullname.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            inputTextPhone.setError("Provide Phone no");
            inputTextPhone.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(address)) {
            inputTextUserAddress.setError("Provide Address");
            inputTextUserAddress.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(description)) {
            editTextDescription.setError("Provide Description");
            editTextDescription.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        UserModel user = SharedPrefManager.getInstance(getContext()).getUser();
        requestPickupModel.setUserId(String.valueOf(user.getId()));
        requestPickupModel.setFullName(full_name);
        requestPickupModel.setPhoneNumber(phone);
        requestPickupModel.setAddress(address);
        requestPickupModel.setTrashDescription(description);

        postCreateUserAddress().enqueue(new Callback<PickupResponse>() {
            @Override
            public void onResponse(Call<PickupResponse> call, Response<PickupResponse> response) {

                PickupResponse createAddressResponse = response.body();
                progressBar.setVisibility(View.VISIBLE);


                if (createAddressResponse != null) {

                    //if no error- that is error = false
                    if (!createAddressResponse.getError()) {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Pickup Request Submitted", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.action_navigation_search_to_navigation_home);


                    } else {
                        submit_btn.setEnabled(true);
                        submit_btn.setClickable(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(),
                                "Pickup Request Failed: " + createAddressResponse.getError(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getContext(), "Pickup Request Failed", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    submit_btn.setEnabled(true);
                    submit_btn.setClickable(true);
                    return;

                }

            }

            @Override
            public void onFailure(Call<PickupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Request Can't be Submitted now, Try again!", Toast.LENGTH_SHORT).show();
                submit_btn.setEnabled(true);
                submit_btn.setClickable(true);
                t.printStackTrace();

            }
        });

    }

    private Call<PickupResponse> postCreateUserAddress() {
        return takaApiService.postCreatePickupRequest(requestPickupModel);
    }



    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }


}