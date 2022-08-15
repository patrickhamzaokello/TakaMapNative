package com.pkasemer.takamap;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.pkasemer.takamap.Apis.MovieApi;
import com.pkasemer.takamap.Apis.MovieService;
import com.pkasemer.takamap.Models.PickupResponse;
import com.pkasemer.takamap.Models.RequestPickupModel;

import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pickup extends AppCompatActivity {

    TextInputEditText inputTextFullname,inputTextPhone,inputTextUserAddress,editTextDescription;
    Button submit_btn;
    ProgressBar progressBar;
    RequestPickupModel requestPickupModel = new RequestPickupModel();
    private MovieService movieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);

        submit_btn = findViewById(R.id.submit_btn);
        inputTextFullname = findViewById(R.id.inputTextFullname);
        inputTextPhone = findViewById(R.id.inputTextPhone);
        inputTextUserAddress = findViewById(R.id.inputTextUserAddress);
        editTextDescription = findViewById(R.id.editTextDescription);
        progressBar = findViewById(R.id.main_progress);
        progressBar.setVisibility(View.GONE);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate_submit_form();
            }
        });
        movieService = MovieApi.getClient(getApplicationContext()).create(MovieService.class);

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

        requestPickupModel.setUserId("122");
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
                        Toast.makeText(getApplicationContext(), "Pickup Request Submitted", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        submit_btn.setEnabled(true);
                        submit_btn.setClickable(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                "Pickup Request Failed: " + createAddressResponse.getError(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Pickup Request Failed", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    submit_btn.setEnabled(true);
                    submit_btn.setClickable(true);
                    return;

                }

            }

            @Override
            public void onFailure(Call<PickupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Request Can't be Submitted now, Try again!", Toast.LENGTH_SHORT).show();
                submit_btn.setEnabled(true);
                submit_btn.setClickable(true);
                t.printStackTrace();

            }
        });

    }

    private Call<PickupResponse> postCreateUserAddress() {
        return movieService.postCreatePickupRequest(requestPickupModel);
    }



    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
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