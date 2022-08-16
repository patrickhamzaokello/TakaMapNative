package com.pkasemer.takamap;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MakeReport extends AppCompatActivity {

    Button btnSelectPhoto;
    ImageView viewImage;
    private final int CAMERA_REQUEST_CODE = 100;
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_report);
        permission();


    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MakeReport.this, new String[]{Manifest.permission.CAMERA}
                    , REQUEST_CODE);
        } else {
            initViewPager();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initViewPager();
            } else {
                ActivityCompat.requestPermissions(MakeReport.this, new String[]{Manifest.permission.CAMERA}
                        , REQUEST_CODE);
            }
        }
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    setImage(result.getResultCode(), result.getData());
                }
            });

    private void initViewPager() {
        viewImage = findViewById(R.id.viewImage);
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto);

        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(iCamera);
            }
        });
    }

    private void setImage(int resultCode, @Nullable Intent data){
        if (resultCode == RESULT_OK) {
                // for camera
                assert data != null;
                Bitmap img = (Bitmap) data.getExtras().get("data");
                viewImage.setImageBitmap(img);
        }
    }

}